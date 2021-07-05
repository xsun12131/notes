package com.fatpanda.notes.repository;

import com.fatpanda.notes.pojo.entity.UserAndRole;
import com.fatpanda.notes.pojo.entity.UserAndRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAndRoleRepository extends JpaRepository<UserAndRole, UserAndRoleKey> {
}
