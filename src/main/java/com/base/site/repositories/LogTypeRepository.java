package com.base.site.repositories;

import com.base.site.models.LogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("LogTypeRepository")
public interface LogTypeRepository extends JpaRepository<LogType, Long> {
}
