package com.base.site.repositories;

import com.base.site.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UserTypeRepo")
public interface UserTypeRepo extends JpaRepository<UserType, Long> {
    UserType findByType(String type);
}
