package com.base.site.services;

import com.base.site.models.UserType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserTypeService")
public interface UserTypeService {
    List<UserType> findAll();

    UserType findById(Long id);

    UserType save(UserType user);

    void deleteById(Long id);

    void delete(UserType user);
}
