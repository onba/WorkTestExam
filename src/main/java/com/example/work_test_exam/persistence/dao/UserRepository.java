package com.example.work_test_exam.persistence.dao;

import com.example.work_test_exam.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
