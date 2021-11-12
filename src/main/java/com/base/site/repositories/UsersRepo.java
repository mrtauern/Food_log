package com.base.site.repositories;

import com.base.site.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UsersRepo")
public interface UsersRepo extends JpaRepository<Users, Long> {
    Users findUsersByUsername(String username);
}
