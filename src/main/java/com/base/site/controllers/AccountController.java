package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.UPRCRepository;
import com.base.site.security.SecurityConfig;
import com.base.site.services.EmailService;
import com.base.site.services.UPRCService;
import com.base.site.services.UserTypeService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class AccountController {

    public AccountController(){

    }

    Logger log = Logger.getLogger(AccountController.class.getName());

    private final String USER_LIST = "userList";
    private final String USER_INFO = "userInfo";
    private final String CREATE_USER = "createUser";
    private final String EDIT_USER = "editUser";
    private final String EDIT_USER_PROFILE = "editUserProfile";
    private final String DELETE_USER_CONFIRM = "delete_user_confirm";
    private final String DASHBOARD = "dashboard";
    private final String LOGIN = "login";
    private final String INDEX = "index";

    private final String REDIRECT = "redirect:/";

    @Autowired
    UsersService usersService;

    @Autowired
    UserTypeService userTypeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UPRCService uprcService;
  
    @Autowired
    LoginController loginController;

    @Autowired
    EmailService emailService;

    @GetMapping("/userList")
    public String userList(Model model , @Param("keyword") String keyword) {
        if (keyword != null) {
            log.info("userList is called with Search String :: " + keyword);
        }else {
            log.info("userList is called");
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("users", usersService.findAll());
        model.addAttribute("pageTitle", "User list");
        model.addAttribute("selectedPage", "user");
        model.addAttribute("user", new Users());

        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return findPaginated(model ,1 ,"firstname", "asc", keyword );
    }

    @GetMapping("/pageUser/{pageNo}")
    public String findPaginated(Model model, @PathVariable(value = "pageNo")int pageNo, @RequestParam("sortField")String sortField,
                                             @RequestParam("sortDir")String sortDir, @Param("keyword") String keyword ){
        int pageSize = 5;
        Page<Users> page = usersService.findPaginated(pageNo,pageSize, sortField, sortDir, keyword);
        List<Users> listUser = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalUser", page.getTotalElements());

        model.addAttribute("listUser", listUser);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return USER_LIST;
    }

    @GetMapping("/adminActionAccountNonLocked")
    public String accountLockUnlock(@RequestParam("id") long id, @RequestParam(required = false, value = "locked") boolean locked, RedirectAttributes redAt) {
        log.info("Getmapping adminActionAccountNonLocked called with id: "+id+" and status locked: "+locked);

        if(usersService.getLoggedInUser().getId() != id) {

            Users user = usersService.findById(id);

            if (locked == false) {
                log.info("account unlock requested");
                user.setAccountNonLocked(1);

                redAt.addFlashAttribute("showMessage", true);
                redAt.addFlashAttribute("messageType", "success");
                redAt.addFlashAttribute("message", "User is successfully unlocked");
            } else {
                log.info("account lock requested");
                user.setAccountNonLocked(0);

                redAt.addFlashAttribute("showMessage", true);
                redAt.addFlashAttribute("messageType", "success");
                redAt.addFlashAttribute("message", "User is successfully Locked");
            }
            usersService.save(user);


        }

        return REDIRECT+USER_LIST;
    }


    @GetMapping({"/createUser","createUser/{userExists}"})
    public String createUser(Model model, @PathVariable(required = false, value = "userExists") String userExists){
        log.info("createUser get called");

        model.addAttribute("users", new Users());
        model.addAttribute("userExists", userExists);
        model.addAttribute("pageTitle", "Create user");
        model.addAttribute("selectedPage", "user");

        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return CREATE_USER;
    }

    @PostMapping("/createUser")

    public String createUser(@ModelAttribute("users") Users user, @RequestParam(value = "user_type") String userType, RedirectAttributes redAt){
        log.info("createUser post called");

        try {
            redAt = usersService.generateUserAndSave(user, userType, redAt);

        } catch (Exception e){
            log.info("Something went wrong with crating an user");
            log.info(e.toString());
        }

        return REDIRECT + USER_LIST;
    }


    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model){
        log.info("editUser get called");

        if(usersService.getLoggedInUser().getId() != id) {

            model.addAttribute("users", usersService.findUserByIdAndSetBdayString(id));
            model.addAttribute("pageTitle", "Edit user");
            model.addAttribute("selectedPage", "user");
            model.addAttribute("loggedInUser", usersService.getLoggedInUser());

            return EDIT_USER;

        } else {
            return REDIRECT + USER_LIST;
        }
    }

    @PostMapping("/editUser")
    public String editUser(@ModelAttribute("users") Users user, RedirectAttributes redAt){
        log.info("editUser post called");


        user.setBirthday(usersService.getBirthdayFromString(user.getSBirthday()));

        try {
            //move to servicelayer?
            usersService.saveEditUserData(user);

        } catch (Exception e){
            log.info("Something went wrong with crating an user");
            log.info(e.toString());
        }

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "User is successfully updated");

        return REDIRECT + USER_LIST;
    }
  
    @GetMapping("/password_reset_user/{id}")
    public String passwordResetUser(@PathVariable(value = "id") long id, Model model) throws MessagingException, IOException {
        uprcService.adminResetUserPassword(id);

        return REDIRECT + USER_LIST;
    }

    @GetMapping("/delete_own_user")
    public String deleteOwnUser( Model model) {
        log.info("delete_own_user called userId: ");

            model.addAttribute("pageTitle", "Delete user");
            model.addAttribute("selectedPage", "user");
            model.addAttribute("loggedInUser", usersService.getLoggedInUser());

            return "/delete_own_user";
    }



    @GetMapping("/delete_user_confirm/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        log.info("delete_user_confirm called userId: "+id);

        if(usersService.getLoggedInUser().getId() != id) {

            model.addAttribute("user", usersService.findById(id));
            model.addAttribute("pageTitle", "Delete user");
            model.addAttribute("selectedPage", "user");
            model.addAttribute("loggedInUser", usersService.getLoggedInUser());

            return DELETE_USER_CONFIRM;
        } else {
            return REDIRECT + USER_LIST;
        }
    }

    @PostMapping("/delete_user_confirm/{id}")
    public String deleteUser(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
        log.info("delete_user_confirm confirmed... deleting user with  userId: "+id);

        usersService.deleteById(id);
        loginController.fetchSignoutSite(request, response);

        log.info("delete_user_confirm confirmed...  Sessio and cookies is deletet: "+id);

        return REDIRECT + LOGIN;
    }

    @GetMapping("/dashboard")
    public String admin_dashboard(Model model) {
        log.info("dashboard getmapping called...");

        model.addAttribute("pageTitle", "User list");
        model.addAttribute("selectedPage", "dashboard");

        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return DASHBOARD;
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model){
        log.info("userInfo called");

        model.addAttribute("users", usersService.findAll());
        model.addAttribute("pageTitle", "User Personal Info");
        model.addAttribute("loggedIn", usersService.getLoggedInUser());
        model.addAttribute("selectedPage", "user");

        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return USER_INFO;
    }

    @GetMapping("/editUserProfile/{id}")
    public String editUserProfile(@PathVariable( value ="id") Long id, Model model) {
        log.info("editUserProfile get called");

        model.addAttribute("user", usersService.findUserByIdAndSetBdayString(id));
        model.addAttribute("pageTitle", "Edit user Profile");
        model.addAttribute("selectedPage", "user");

        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return EDIT_USER_PROFILE;
    }
    @PostMapping("/editUserProfile")
    public String editUserProfile(@ModelAttribute("users") Users user){
        log.info("editUserProfile post called");

        try {
            usersService.setAndSaveUserData(user);
        } catch (Exception e){
            log.info("Something went wrong with crating an user");
            log.info(e.toString());
        }

        return REDIRECT + USER_INFO;
    }
}
