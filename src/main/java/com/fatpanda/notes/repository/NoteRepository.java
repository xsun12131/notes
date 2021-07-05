package com.fatpanda.notes.repository;

import com.fatpanda.notes.pojo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, String>, JpaSpecificationExecutor {

    /**
     * 批量删除User及更新删除标识
     *
     * @param idList idList
     */
    @Transactional(rollbackOn = Exception.class)
    void deleteByIdIn(List<String> idList);

    /**
     * 根据idList和删除标识查询Note
     *
     * @param idList 要查询的id列表
     * @return NoteList
     */
    List<Note> findByIdIn(List<String> idList);
}