package com.fatpanda.notes.repository;

import com.fatpanda.notes.pojo.entity.NoteTag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xyy
 * @date 2021/7/9
 */
public interface NoteTagRepository extends JpaRepository<NoteTag, String> {

    NoteTag findByName(String name);

}
