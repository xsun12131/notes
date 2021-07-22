package com.fatpanda.notes.repository;

import com.fatpanda.notes.pojo.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xyy
 * @date 2021/7/22
 */
public interface LogRepository extends JpaRepository<Log, String> {
}
