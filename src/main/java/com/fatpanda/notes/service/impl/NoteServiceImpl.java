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
import com.github.wenhao.jpa.Specifications;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
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
        noteEsRepository.save(EsNote.byNote(note));
        return noteRepository.save(note);
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
        Pageable pageable = PageRequest.of(pageNum, pageSize);
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
        //todo 高亮搜索
        /**
         * 创建查询体
         */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
//        /**
//         * 设置聚合条件
//         */
//        RangeQueryBuilder query = QueryBuilders.rangeQuery("age").from("30").to("60");
        /**
         * 将聚合条件设置入查询体之中
         */
//        builder.must(query);
        MatchQueryBuilder queryTitle = QueryBuilders.matchQuery("title", searchDto.getQuery()).analyzer("ik_smart");
        MatchQueryBuilder queryContent = QueryBuilders.matchQuery("content", searchDto.getQuery()).analyzer("ik_smart");
        //设置高亮
//        queryBuilder.
//        queryBuilder.withHighlightFields(new HighlightBuilder.Field("title").preTags("<font color='red'>").postTags("</font>"));
        builder.should(queryTitle);
        builder.should(queryContent);

        Pageable pageable = PageRequest.of(searchDto.getPageNum() -1, searchDto.getPageSize());
        Page<EsNote> esNotePage = noteEsRepository.search(builder, pageable);

        return PageResult.pageToPageResult(esNotePage,
                esNotePage.getContent().stream()
                        .map(esNote -> NoteListVo.builder().id(esNote.getId()).title(esNote.getTitle()).summary(esNote.getSummary()).build())
                        .collect(Collectors.toList()));
    }
}