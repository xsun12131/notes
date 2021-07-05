package com.fatpanda.notes.repository;

import com.fatpanda.notes.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select ur.id.roleId from UserAndRole ur left join User u on ur.id.userId = u.id where u.username = ?1")
    List<String> getRoleByUserName(String username);

    User getUserByUsername(String username);

}
