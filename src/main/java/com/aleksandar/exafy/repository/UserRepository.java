package com.aleksandar.exafy.repository;

import com.aleksandar.exafy.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
