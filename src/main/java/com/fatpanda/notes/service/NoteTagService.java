package com.fatpanda.notes.service;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.entity.PageResult;
import com.fatpanda.notes.pojo.entity.NoteTag;
import com.fatpanda.notes.pojo.vo.NoteListVo;

import java.util.List;

/**
 * @author xyy
 * @date 2021/7/9
 */
public interface NoteTagService {

    List<NoteTag> findAll();

    boolean save(String[] tags, String noteId);

    NoteTag save(NoteTag noteTag);

    PageResult<NoteListVo> findNoteByTag(SearchDto searchDto);
}
