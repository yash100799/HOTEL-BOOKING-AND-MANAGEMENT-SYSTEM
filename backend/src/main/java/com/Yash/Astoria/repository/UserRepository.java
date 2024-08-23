package com.Yash.Astoria.repository;

import com.Yash.Astoria.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Object name, Primary key dataType
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
