package com.base.site.services;

import com.base.site.models.*;
import com.base.site.repositories.DailyLogRepo;
import com.base.site.repositories.LogTypeRepository;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.data.domain.PageRequest.of;

@Service("UsersService")
public class UsersServiceImpl implements UsersService {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final int LEN = 10; //length
    static SecureRandom rnd = new SecureRandom();

    Logger log = Logger.getLogger(UsersServiceImpl.class.getName());

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    DailyLogRepo dailyLogRepo;

    @Autowired
    LogTypeRepository logTypeRepo;

    @Autowired
    UserTypeService userTypeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UPRCService uprcService;

    @Autowired
    EmailService emailService;

    @Override
    public List<Users> findAll(){
        return usersRepo.findAll();
    }

    @Override
    public Users findById(Long id){
        return usersRepo.findById(id).get();
    }

    @Override
    public Users save(Users user){
        return usersRepo.save(user);
    }

    @Override
    public void deleteById(Long id){
        usersRepo.deleteById(id);
    }

    @Override
    public void delete(Users user){
        usersRepo.delete(user);
    }

    @Override
    public Users findByUserName(String name) {
        return usersRepo.findUsersByUsername(name);
    }

    @Override
    public String generatePassword() {
        StringBuilder sb = new StringBuilder(LEN);
        for(int i = 0; i < LEN; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    @Override
    public Users findUsersByUsername(String username) {
        return usersRepo.findUsersByUsername(username);
    }

    @Override
    public DailyLog getLatestWeight(LocalDate date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = findByUserName(auth.getName());

        List<DailyLog> logList = dailyLogRepo.findAll();

        //ArrayList<DailyLog> weight = new ArrayList<DailyLog>();
        DailyLog weightLog = new DailyLog("1900-01-01");
        for (DailyLog logdate: logList) {
            if((logdate.getDatetime().isBefore(date) || logdate.getDatetime().equals(date))
                    && loggedInUser.getId() == logdate.getFkUser().getId()
                    && logdate.getFkLogType().getType().equals("Weight")) {

                if(logdate.getDatetime() != null
                        && logdate.getDatetime().isAfter(weightLog.getDatetime())) {

                    weightLog.setDatetime(logdate.getDatetime());
                    weightLog.setFkLogType(logdate.getFkLogType());
                    weightLog.setAmount(logdate.getAmount());
                    weightLog.setId(logdate.getId());

                }
            }
        }

        if(weightLog.getAmount() != 0.0) {
            return weightLog;
        } else {
            LogType log_type = logTypeRepo.findByType("Weight");
            weightLog.setAmount(loggedInUser.getStartWeight());
            weightLog.setFkLogType(log_type);
            return weightLog;
        }
    }

    @Override
    public Page<Users> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();

        Pageable pageable = of(pageNo - 1, pageSize, sort);

        if (keyword != null) {
            return usersRepo.findAll(keyword, pageable);
        }
        return this.usersRepo.findAll(pageable);
    }

    public Users getLoggedInUser(HttpSession session) {

        Users loggedInUser = new Users();
        Users user = new Users();
        if(session.getAttribute("loggedInUser") == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            user = usersRepo.findUsersByUsername(auth.getName());

            loggedInUser.setUserType(user.getUserType());
            loggedInUser.setId(user.getId());
            loggedInUser.setRoles(user.getRoles());
            loggedInUser.setBirthday(user.getBirthday());
            loggedInUser.setAccountNonLocked(user.getAccountNonLocked());
            loggedInUser.setKcal_modifier(user.getKcal_modifier());
            loggedInUser.setFirstname(user.getFirstname());
            loggedInUser.setLastname(user.getLastname());
            loggedInUser.setGoalWeight(user.getGoalWeight());
            loggedInUser.setHeight(user.getHeight());
            loggedInUser.setStartWeight(user.getStartWeight());
            loggedInUser.setRegisterDate(user.getRegisterDate());
            loggedInUser.setDailyLogs(user.getDailyLogs());
            loggedInUser.setPrivateFoods(user.getPrivateFoods());
            loggedInUser.setRecipies(user.getRecipies());
            loggedInUser.setBmi(user.getBmi());

            session.setAttribute("loggedInUser", loggedInUser);
        }
        else if (session.getAttribute("loggedInUser") != null) {
            loggedInUser = (Users)session.getAttribute("loggedInUser");
        }


        return loggedInUser;
    }

    public LocalDate getBirthdayFromString(String birthdayString) {
        Date birthday = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthday = dateFormat.parse(birthdayString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Timestamp ts=new Timestamp(birthday.getTime());
        LocalDate bday = Instant.ofEpochMilli(birthday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

        return bday;
    }

    @Override
    public void setAndSaveUserData(Users user) {
        user.setBirthday(getBirthdayFromString(user.getSBirthday()));
        Users userData = findById(user.getId());
        //user.setUserType(userData.getUserType());
        user.setPassword(userData.getPassword());
        user.setRegisterDate(userData.getRegisterDate());
        user.setKcal_modifier(userData.getKcal_modifier());
        user.setAccountNonLocked(userData.getAccountNonLocked());
        user.setRoles(userData.getRoles());
        save(user);
    }

    @Override
    public Users findUserByIdAndSetBdayString(long id) {
        Users user = findById(id);

        String sBirthday = user.getBirthday().toString();

        user.setSBirthday(sBirthday);
        return user;
    }

    @Override
    public Users setAndSaveNewUser(Users user, String userTypeString) {
        UserType userTypeObject = userTypeService.findByType(userTypeString);
        user.setUserType(userTypeObject);
        user.setRoles("USER");
        user.setAccountNonLocked(1);
        String pass = passwordEncoder.encode(user.getPassword());
        user.setPassword(pass);
        user.setBirthday(LocalDate.parse("1900-01-01"));
        save(user);
        return user;
    }

    @Override
    public String updateUserPassword(UserPassResetCode resetCode) {
        UserPassResetCode foundResetCode = uprcService.findByUsername(resetCode.getUsername());
        Users foundUser = findUsersByUsername(resetCode.getUsername());

        if(resetCode.getCode().equals(foundResetCode.getCode()) && foundResetCode != null && foundUser != null && foundResetCode.isUsed() == false) {
            log.info("Username and code checks out, saving new data...");
            foundUser.setPassword(passwordEncoder.encode(resetCode.getPassword()));
            save(foundUser);
            uprcService.delete(foundResetCode);
            log.info("Sending email to user informing that password have been changed...");
            Mail mail = new Mail();

            mail.setRecipient(resetCode.getUsername());
            mail.setTopic("Your password on Food Log has been changed!");
            mail.setContent("If you didnt request this change please contact up immediately on email: foodlog.dk@gmail.com");
            try {
                emailService.sendmail(mail);
                return "redirect:/login";
            } catch (Exception e) {
                log.info("an error occured during sending of mail un method updateUserPassword (UserServiceImpl)");
            }


        }

        return "redirect:/password-reset";
    }

    @Override
    public void saveEditUserData(Users user) {
        Users userData = findById(user.getId());
        //user.setUserType(userData.getUserType());
        user.setPassword(userData.getPassword());
        user.setRegisterDate(userData.getRegisterDate());
        user.setKcal_modifier(userData.getKcal_modifier());
        save(user);
    }

    @Override
    public RedirectAttributes generateUserAndSave(Users user, String userType, RedirectAttributes redAt) throws MessagingException, IOException {
        String genPass = generatePassword();
        String encPass = passwordEncoder.encode(genPass);
        user.setPassword(encPass);

        user.setBirthday(getBirthdayFromString(user.getSBirthday()));

        String emailMessage = "We have created a new user for you.\n\n";
        emailMessage += "Your new password is: " + genPass;

        user.setUserType(userTypeService.findByType(userType));

        if(findByUserName(user.getUsername()) == null) {
            user.setAccountNonLocked(1);
            save(user);
            Mail mail = new Mail();
            mail.setRecipient(user.getUsername());
            mail.setTopic("Your user have been created on food log");
            mail.setContent(emailMessage);
            emailService.sendmail(mail);

            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "User is successfully created");

        } else {
            log.info("something went wrong when creating the user");

            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "error");
            redAt.addFlashAttribute("message", "User with this e-mail already exists");
        }

        return redAt;
    }

    @Override
    public Model getPaginatedModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword, HttpSession session) {
        int pageSize = 5;
        Page<Users> page = findPaginated(pageNo,pageSize, sortField, sortDir, keyword);
        List<Users> listUser = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalUser", page.getTotalElements());

        model.addAttribute("listUser", listUser);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("loggedInUser", getLoggedInUser(session));

        return model;
    }


    /*@Override
    public Model getEditModels(Model model) {

        model.addAttribute("pageTitle", "Edit user");
        model.addAttribute("selectedPage", "user");
        model.addAttribute("loggedInUser", getLoggedInUser(session));
        return model;
    }*/

}
