package org.testTask.databases.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.testTask.databases.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
}
