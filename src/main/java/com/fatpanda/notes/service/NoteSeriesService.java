package com.fatpanda.notes.service;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.entity.PageResult;
import com.fatpanda.notes.pojo.entity.NoteSeries;
import com.fatpanda.notes.pojo.vo.NoteListVo;

import java.util.List;

/**
 * @author xyy
 * @date 2021/7/14
 */
public interface NoteSeriesService {

    /**
     * 根据noteId查找和这篇note相同系列的note
     * @param searchDto
     * @return
     */
    PageResult<NoteListVo> findSameSeries(SearchDto searchDto);

    /**
     * 新增或修改noteSeries
     * @param noteSeries
     * @return
     */
    NoteSeries save(NoteSeries noteSeries);

}
