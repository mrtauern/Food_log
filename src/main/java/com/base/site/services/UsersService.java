package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    Users findUsersByUsername(String username);
    DailyLog getLatestWeight(LocalDate date);
    Page<Users> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection, String keyword);
    Users getLoggedInUser();

    LocalDate getBirthdayFromString(String birthdayString);
}
