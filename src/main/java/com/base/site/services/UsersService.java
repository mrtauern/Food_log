package com.base.site.services;

import com.base.site.models.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UsersService")
public interface UsersService {
    List<Users> findAll();

    Users findById(Long id);

    Users save(Users user);

    void deleteById(Long id);

    void delete(Users user);

    Users findByUserName(String name);

    String generatePassword();
}
