package com.fatpanda.notes.repository;

import com.fatpanda.notes.pojo.entity.NoteAndNoteTag;
import com.fatpanda.notes.pojo.entity.NoteAndNoteTagKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xyy
 * @date 2021/7/9
 */
public interface NoteAndNoteTagRepository extends JpaRepository<NoteAndNoteTag, NoteAndNoteTagKey> {

    @Query(" select nan.id.noteId from NoteAndNoteTag nan where nan.id.noteTagId = ?1")
    List<String> findByNoteTagId(String noteTagId);

}
