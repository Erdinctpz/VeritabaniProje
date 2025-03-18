package com.veriTabaniProje.demo.veriTabaniProje.repository;

import com.veriTabaniProje.demo.veriTabaniProje.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}