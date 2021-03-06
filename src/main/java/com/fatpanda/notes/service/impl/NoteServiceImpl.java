package com.fatpanda.notes.service.impl;

import com.fatpanda.notes.common.model.entity.BasePageDto;
import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.entity.PageResult;
import com.fatpanda.notes.common.utils.FileUtil;
import com.fatpanda.notes.common.utils.StringUtil;
import com.fatpanda.notes.esRepository.NoteEsRepository;
import com.fatpanda.notes.pojo.dto.NoteDto;
import com.fatpanda.notes.pojo.entity.Note;
import com.fatpanda.notes.pojo.esEntity.EsNote;
import com.fatpanda.notes.pojo.vo.NoteListVo;
import com.fatpanda.notes.pojo.vo.NoteVo;
import com.fatpanda.notes.repository.NoteRepository;
import com.fatpanda.notes.service.NoteService;
import com.fatpanda.notes.service.NoteTagService;
import com.github.wenhao.jpa.Specifications;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    @Resource
    private NoteRepository noteRepository;
    @Resource
    private NoteEsRepository noteEsRepository;
    @Resource
    private NoteTagService noteTagService;
    @Resource
    private ElasticsearchRestTemplate template;

    /**
     * ???????????????Note
     *
     * @param noteDto noteDto
     * @return Note
     */
    @Override
    public NoteVo save(NoteDto noteDto) {
        Optional<Note> noteOptional = noteRepository.findById(noteDto.getId());

        Note note = Note.builder()
                .id(noteDto.getId())
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .build();
        if (noteOptional.isPresent()) {
            note.setCreateTime(noteOptional.get().getCreateTime());
        }
        note.setSummary(StringUtil.replaceMarkDown(note.getContent()));
        note = noteRepository.save(note);
        noteEsRepository.save(EsNote.byNote(note));
        noteTagService.save(noteDto.getTags(), note.getId());
        return NoteVo.byNoteAndTags(note, noteDto.getTags());
    }

    /**
     * ??????????????????????????????Note
     *
     * @param pageNum  ??????
     * @param pageSize ????????????
     * @return ??????????????????
     */
    @Override
    public PageResult<NoteListVo> findAll(Integer pageNum, Integer pageSize) {
        Specification<Note> specifications = Specifications.<Note>and().build();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "updateTime"));
        Page<Note> all = noteRepository.findAll(specifications, pageable);
        PageResult<NoteListVo> pageResult =
                PageResult.pageToPageResult(all, all.stream().map(note -> NoteListVo.builder()
                        .id(note.getId())
                        .title(note.getTitle())
                        .summary(note.getSummary())
                        .build()).collect(Collectors.toList()));
        return pageResult;
    }

    /**
     * ??????????????????????????????Note
     *
     * @return ????????????
     */
    @Override
    public List<Note> findAll() {
        List<Note> all = noteRepository.findAll();
        return all;
    }

    /**
     * ??????ids????????????Note
     *
     * @param ids ????????????id??????
     * @return ?????????id
     */
    @Override
    public List<String> deleteNoteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<Note> noteList
                = noteRepository.findByIdIn(idList);
        noteRepository.deleteByIdIn(idList);
        noteEsRepository.deleteByIdIn(idList);
        List<String> hasIds
                = noteList.stream().map(Note::getId).collect(Collectors.toList());
        return hasIds;
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @Override
    public NoteVo getById(String id) {
        Note note = noteRepository.findById(id).orElse(null);
        if(Objects.isNull(note)) {
            return null;
        }
        List<String> tags = noteRepository.findTagsOfNote(note.getId());
        return NoteVo.byNoteAndTags(note, tags.toArray(new String[0]));
    }

    /**
     * ??????md??????
     *
     * @param multipartFile
     * @return
     */
    @Override
    public Note parseMd(MultipartFile multipartFile) {
        Note note = new Note();
        String originalFilename = multipartFile.getOriginalFilename();
        note.setTitle(originalFilename.contains(".") ? StringUtil.substringBeforeLast(originalFilename, ".") : originalFilename);
        note.setContent(FileUtil.readFile(multipartFile));
        note.setSummary(StringUtil.replaceMarkDown(note.getContent()));
        return note;
    }

    @Override
    public PageResult<NoteListVo> search(SearchDto searchDto) {
        //todo ??????
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // ??????????????????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //?????????
        if (StringUtil.isNotBlank(searchDto.getQuery())) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchDto.getQuery()));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchDto.getQuery()));
        }
        // ??????
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        // ??????
        // ????????????
        String sortField = searchDto.getSort();
        // ???????????? - ??????(ASC)/??????(DESC)
        String sortRule = (StringUtil.isNotBlank(searchDto.getSortRule()) && StringUtil.equalsAny(searchDto.getSortRule(), "ASC","DESC")) ? searchDto.getSortRule() : "ASC";
        if (StringUtil.isNotBlank(sortField)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }
        // ????????????
        nativeSearchQueryBuilder.withPageable(PageRequest.of(searchDto.getPageNum() - 1, searchDto.getPageSize()));

        // ??????????????????
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("title").preTags("<font style='color:red'>").postTags("</font>");
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("content").preTags("<font style='color:red'>").postTags("</font>");
        // ????????????
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);
        NativeSearchQuery build = nativeSearchQueryBuilder.build();

        SearchHits<EsNote> esNoteSearchHits = template.search(build, EsNote.class);
        List<NoteListVo> noteListVoList = esNoteSearchHits.get().map(esNoteSearchHit -> {
            EsNote esNote = esNoteSearchHit.getContent();
            NoteListVo noteListVo = NoteListVo.builder().id(esNote.getId())
                    .title(esNote.getTitle())
                    .summary(esNote.getSummary())
                    .build();
            if (esNoteSearchHit.getHighlightField("title").size() > 0) {
                noteListVo.setTitle(esNoteSearchHit.getHighlightField("title").get(0));
            }
            if (esNoteSearchHit.getHighlightField("content").size() > 0) {
                List<String> content = esNoteSearchHit.getHighlightField("content");
                noteListVo.setSummary(esNoteSearchHit.getHighlightField("content").get(0));
            }
            return noteListVo;
        }).collect(Collectors.toList());

        PageResult pageResult = new PageResult();
        pageResult.setContent(noteListVoList);
        return pageResult;
    }


    @Override
    public List<NoteListVo> findIdIn(List<String> noteIdList) {
        List<Note> noteList = noteRepository.findByIdIn(noteIdList);
        return noteList.stream().map(note -> NoteListVo.builder()
                .id(note.getId())
                .summary(note.getSummary())
                .title(note.getTitle())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<NoteListVo> findIdIn(BasePageDto basePageDto, List<String> noteIdList) {
        List<Note> noteList = noteRepository.findByIdIn(noteIdList);
        List<NoteListVo> collect = noteList.stream().map(note -> NoteListVo.builder()
                .id(note.getId())
                .summary(note.getSummary())
                .title(note.getTitle())
                .build())
                .collect(Collectors.toList());
        return new PageResult<NoteListVo>().listToPageResult(collect, basePageDto.getPageNum(), basePageDto.getPageSize());
    }

    /**
     * ??????es?????????
     */
    @Override
    public void refreshEsNote() {
        List<Note> all = noteRepository.findAll();
        noteEsRepository.saveAll(all.stream().map(note -> EsNote.byNote(note)).collect(Collectors.toList()));
    }


}