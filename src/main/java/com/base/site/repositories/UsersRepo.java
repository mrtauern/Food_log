package com.base.site.repositories;

import com.base.site.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("UsersRepo")
public interface UsersRepo extends JpaRepository<Users, Long> {
    Users findUsersByUsername(String username);

    @Query("SELECT u FROM Users u WHERE CONCAT(u.firstname , u.lastname, u.username, u.registerDate)  LIKE %?1%")
    public Page<Users> findAll(String key, Pageable pageable);
}
