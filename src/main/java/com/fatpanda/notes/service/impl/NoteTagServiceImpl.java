package com.fatpanda.notes.service.impl;

import com.fatpanda.notes.pojo.entity.NoteAndNoteTag;
import com.fatpanda.notes.pojo.entity.NoteAndNoteTagKey;
import com.fatpanda.notes.pojo.entity.NoteTag;
import com.fatpanda.notes.pojo.vo.NoteListVo;
import com.fatpanda.notes.repository.NoteAndNoteTagRepository;
import com.fatpanda.notes.repository.NoteTagRepository;
import com.fatpanda.notes.service.NoteService;
import com.fatpanda.notes.service.NoteTagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author xyy
 * @date 2021/7/9
 */
@Service
public class NoteTagServiceImpl implements NoteTagService {

    @Resource
    private NoteTagRepository noteTagRepository;
    @Resource
    private NoteAndNoteTagRepository noteAndNoteTagRepository;
    @Resource
    private NoteService noteService;

    @Override
    public NoteTag save(NoteTag noteTag) {
        NoteTag oldNoteTag = noteTagRepository.findByName(noteTag.getName());
        if (null != oldNoteTag) {
            return oldNoteTag;
        }
        return noteTagRepository.save(noteTag);
    }

    @Override
    public List<NoteTag> findAll() {
        return noteTagRepository.findAll();
    }

    @Override
    public List<NoteListVo> findNoteByTag(String tagName) {
        NoteTag noteTag = noteTagRepository.findByName(tagName);
        if (null == noteTag) {
            return Collections.emptyList();
        }
        List<String> noteIdList = noteAndNoteTagRepository.findByNoteTagId(noteTag.getId());
        return noteService.findIdIn(noteIdList);
    }

    @Override
    public boolean save(String[] tags, String noteId) {

        for (String tag : tags) {
            NoteTag noteTag = save(NoteTag.builder().name(tag).build());
            noteAndNoteTagRepository.save(NoteAndNoteTag.builder().id(new NoteAndNoteTagKey(noteId, noteTag.getId())).build());
        }

        return true;
    }
}
