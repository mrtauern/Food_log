package com.base.site.repositories;

import com.base.site.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("UsersRepo")
public interface UsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findUserByEmail(String email);
}
