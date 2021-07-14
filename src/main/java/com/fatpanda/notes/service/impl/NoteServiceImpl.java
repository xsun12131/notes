package com.fatpanda.notes.service.impl;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.entity.PageResult;
import com.fatpanda.notes.common.utils.FileUtil;
import com.fatpanda.notes.common.utils.StringUtil;
import com.fatpanda.notes.esRepository.NoteEsRepository;
import com.fatpanda.notes.pojo.dto.NoteDto;
import com.fatpanda.notes.pojo.entity.Note;
import com.fatpanda.notes.pojo.esEntity.EsNote;
import com.fatpanda.notes.pojo.vo.NoteListVo;
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
     * 新增或修改Note
     *
     * @param noteDto noteDto
     * @return Note
     */
    @Override
    public Note save(NoteDto noteDto) {
        Note note = Note.builder()
                .id(noteDto.getId())
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .build();
        note.setSummary(StringUtil.replaceMarkDown(note.getContent()));
        note = noteRepository.save(note);
        noteEsRepository.save(EsNote.byNote(note));
        noteTagService.save(noteDto.getTags(), note.getId());
        return note;
    }

    /**
     * 根据查询条件查询所有Note
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页查询结果
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
     * 根据查询条件查询所有Note
     *
     * @return 所有结果
     */
    @Override
    public List<Note> findAll() {
        List<Note> all = noteRepository.findAll();
        return all;
    }

    /**
     * 根据ids批量删除Note
     *
     * @param ids 要删除的id数组
     * @return 删除的id
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
     * 通过id查询
     *
     * @param id
     * @return
     */
    @Override
    public Note getById(String id) {
        return noteRepository.findById(id).orElse(null);
    }

    /**
     * 解析md文件
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
        //todo 分页
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 构建布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtil.isNotBlank(searchDto.getQuery())) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchDto.getQuery()));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchDto.getQuery()));
        }
        // 查询
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        // 排序
        String sortField = searchDto.getSort();      // 排序字段
        String sortRule = StringUtil.isNotBlank(searchDto.getSortRule()) ? searchDto.getSortRule() : "ASC";        // 排序规则 - 顺序(ASC)/倒序(DESC)
        if (StringUtil.isNotBlank(sortField)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }
        // 构建分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(searchDto.getPageNum() - 1, searchDto.getPageSize()));

        // 构建高亮查询
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("title").preTags("<font style='color:red'>").postTags("</font>");
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("content").preTags("<font style='color:red'>").postTags("</font>");
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);  // 名字高亮
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
}