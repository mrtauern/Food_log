package com.base.site.services;

import com.base.site.models.UserType;
import com.base.site.repositories.UserTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service("UserTypeService")
public class UserTypeServiceImpl implements UserTypeService{

    @Autowired
    UserTypeRepo userTypeRepo;

    @Override
    public List<UserType> findAll(){
        return userTypeRepo.findAll();
    }

    @Override
    public UserType findById(Long id){
        return userTypeRepo.findById(id).get();
    }

    @Override
    public UserType save(UserType user){
        return userTypeRepo.save(user);
    }

    @Override
    public void deleteById(Long id){
        userTypeRepo.deleteById(id);
    }

    @Override
    public void delete(UserType user){
        userTypeRepo.delete(user);
    }

    @Override
    public UserType findByType(String type) {
        return userTypeRepo.findByType(type);
    }
}
