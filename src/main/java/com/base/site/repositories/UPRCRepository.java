package com.base.site.repositories;

import com.base.site.models.UserPassResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UPRCRepository extends JpaRepository<UserPassResetCode, Long> {
    UserPassResetCode findByUsername(String username);
}
