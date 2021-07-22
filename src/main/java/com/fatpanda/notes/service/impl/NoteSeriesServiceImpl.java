package com.fatpanda.notes.service.impl;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.entity.PageResult;
import com.fatpanda.notes.pojo.entity.NoteSeries;
import com.fatpanda.notes.pojo.vo.NoteListVo;
import com.fatpanda.notes.repository.NoteSeriesRepository;
import com.fatpanda.notes.service.NoteSeriesService;
import com.fatpanda.notes.service.NoteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xyy
 * @date 2021/7/14
 */
@Service
public class NoteSeriesServiceImpl implements NoteSeriesService {

    @Resource
    private NoteSeriesRepository noteSeriesRepository;

    @Resource
    private NoteService noteService;

    @Override
    public NoteSeries save(NoteSeries noteSeries) {
        return noteSeriesRepository.save(noteSeries);
    }

    @Override
    public PageResult<NoteListVo> findSameSeries(SearchDto searchDto) {
        Optional<NoteSeries> noteSeries = noteSeriesRepository.findById(searchDto.getQuery());
        List<NoteListVo> noteListVoList = Collections.emptyList();
        noteSeries.ifPresent(x -> {
            List<NoteSeries> noteSeriesList = noteSeriesRepository.findByParentId(x.getParentId());
            noteSeriesList.add(noteSeriesRepository.findById(x.getParentId()).get());
            noteListVoList.addAll(noteService.findIdIn(noteSeriesList.stream().map(NoteSeries::getId).collect(Collectors.toList())));
        });
        return new PageResult<NoteListVo>().listToPageResult(noteListVoList, searchDto.getPageNum(), searchDto.getPageSize());
    }

}
