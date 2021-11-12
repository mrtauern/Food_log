package com.base.site.services;

import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UsersService")
public class UsersServiceImpl implements UsersService {

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
}
