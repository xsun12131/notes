package com.fatpanda.notes.repository;

import com.fatpanda.notes.pojo.entity.NoteSeries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xyy
 * @date 2021/7/14
 */
public interface NoteSeriesRepository extends JpaRepository<NoteSeries, String> {

        List<NoteSeries> findByParentId(String parentId);
}
