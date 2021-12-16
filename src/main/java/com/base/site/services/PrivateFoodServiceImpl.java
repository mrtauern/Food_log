package com.base.site.services;

import com.base.site.controllers.PrivateFoodController;
import com.base.site.models.*;
import com.base.site.repositories.PrivateFoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.logging.Logger;

import static org.springframework.data.domain.PageRequest.of;

@Service("PrivateFoodService")
public class PrivateFoodServiceImpl implements PrivateFoodService{
    Logger log = Logger.getLogger(PrivateFoodServiceImpl.class.getName());

    @Autowired
    PrivateFoodRepo privateFoodRepo;

    @Autowired
    UsersService usersService;

    @Override
    public List<PrivateFood> findAll() {
        return (List<PrivateFood>) privateFoodRepo.findAll();
    }

    @Override
    public List<PrivateFood> findAllByKeyword(String keyword) {
        if (keyword != null) {
            //return privateFoodRepo.search(keyword);
            return privateFoodRepo.findAll();
        }
        return (List<PrivateFood>) privateFoodRepo.findAll();    }

    @Override
    public PrivateFood save(PrivateFood privateFood) {
        return privateFoodRepo.save(privateFood);
    }

    @Override
    public PrivateFood findById(Long id) {

        return privateFoodRepo.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        this.privateFoodRepo.deleteById(id);
    }

    @Override
    public List<PrivateFood> findAllByFkUser(Users loggedInUser) {
        return privateFoodRepo.findAllByFkUser(loggedInUser);
    }


    @Override
    public Page<PrivateFood> findPaginatedAddFood(int pageNo, int pageSize, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();

        Pageable pageable = of(pageNo - 1, pageSize, sort);

        if (keyword != null) {
            return privateFoodRepo.findAll(keyword, pageable);
        }
        return this.privateFoodRepo.findAll(pageable);
    }

    @Override
    public List<PrivateFood> getPrivateFoodForUser(Users loggedInUser, String keyword) {
        List<PrivateFood> privateFoods;
        if(keyword == null || keyword.equals("") || keyword.equals(" ")) {
            privateFoods = findAllByFkUser(loggedInUser);
        } else {
            privateFoods = privateFoodRepo.findAllByFkUserAndKeyword(loggedInUser.getId(), keyword);
        }
        return privateFoods;
    }
}
