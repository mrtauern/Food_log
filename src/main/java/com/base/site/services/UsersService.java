package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.UserPassResetCode;
import com.base.site.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
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

    void setAndSaveUserData(Users user);

    Users findUserByIdAndSetBdayString(long id);

    Users setAndSaveNewUser(Users user, String userTypeString);

    String updateUserPassword(UserPassResetCode resetCode);
    void saveEditUserData(Users user);

    RedirectAttributes generateUserAndSave(Users user, String userType, RedirectAttributes redAt) throws MessagingException, IOException;

    Model getPaginatedModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword);

    Model getEditModels(Model model);
}
