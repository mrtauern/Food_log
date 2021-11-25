package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.LogType;
import com.base.site.models.Users;
import com.base.site.repositories.DailyLogRepo;
import com.base.site.repositories.LogTypeRepository;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
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
}
