package com.base.site.services;

import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service("UsersService")
public class UsersServiceImpl implements UsersService {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final int LEN = 10; //length
    static SecureRandom rnd = new SecureRandom();

    @Autowired
    UsersRepo usersRepo;

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
}
