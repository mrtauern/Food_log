package com.base.site.services;

import com.base.site.models.UserType;
import com.base.site.repositories.UserTypeRepo;
import com.base.site.repositories.UsersRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserTypeService")
public class UserTypeServiceImpl implements UserTypeService{
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
}
