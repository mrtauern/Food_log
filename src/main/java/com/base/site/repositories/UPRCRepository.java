package com.base.site.repositories;

import com.base.site.models.UserPassResetCode;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UPRCRepository extends JpaRepository<UserPassResetCode, Long> {
    UserPassResetCode findByUsername(String username);
}
