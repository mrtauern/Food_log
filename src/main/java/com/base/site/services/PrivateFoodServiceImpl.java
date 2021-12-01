package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import com.base.site.models.PrivateFood;
import com.base.site.repositories.PrivateFoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("PrivateFoodService")
public class PrivateFoodServiceImpl implements PrivateFoodService{

    @Autowired
    PrivateFoodRepo privateFoodRepo;

    @Override
    public List<PrivateFood> findAll() {
        return (List<PrivateFood>) privateFoodRepo.findAll();
    }

    @Override
    public List<PrivateFood> findAllByKeyword(String keyword) {
        if (keyword != null) {
            return privateFoodRepo.search(keyword);
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

}
