package com.example.work_test_exam.persistence.dao;

import com.example.work_test_exam.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
