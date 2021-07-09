package com.fatpanda.notes.service;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.entity.PageResult;
import com.fatpanda.notes.pojo.dto.NoteDto;
import com.fatpanda.notes.pojo.entity.Note;
import com.fatpanda.notes.pojo.vo.NoteListVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoteService {

    /**
     * 新增或修改Note
     *
     * @param noteDto note
     * @return Note
     */
    Note save(NoteDto noteDto);

    /**
     * 根据查询条件查询所有Note
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页查询结果
     */
    PageResult<NoteListVo> findAll(Integer pageNum, Integer pageSize);

    /**
     * 根据查询条件查询所有Note
     *
     * @return 所有结果
     */
    List<Note> findAll();

    /**
     * 根据ids批量删除Note
     *
     * @param ids 要删除的id数组
     * @return 删除的id
     */
    List<String> deleteNoteBatch(String[] ids);

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    Note getById(String id);

    /**
     * 解析markdown文档并返回note
     *
     * @param multipartFile
     * @return
     */
    Note parseMd(MultipartFile multipartFile);

    /**
     * 搜索
     *
     * @param searchDto
     * @return
     */
    PageResult<NoteListVo> search(SearchDto searchDto);

    List<NoteListVo> findIdIn(List<String> noteIdList);
}