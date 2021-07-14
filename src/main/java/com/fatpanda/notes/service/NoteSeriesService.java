package com.fatpanda.notes.service;

import com.fatpanda.notes.pojo.entity.NoteSeries;
import com.fatpanda.notes.pojo.vo.NoteListVo;

import java.util.List;

/**
 * @author xyy
 * @date 2021/7/14
 */
public interface NoteSeriesService {

    List<NoteListVo> findSameSeries(String noteId);

    NoteSeries save(NoteSeries noteSeries);
}
