package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import com.base.site.repositories.FoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.PageRequest.of;

@Service("FoodService")
public class FoodServiceImpl implements FoodService {

    @Autowired
    FoodRepo foodRepo;

    @Override
    public List<Food> findAll() {
        return (List<Food>) foodRepo.findAll();
    }


    @Override
    public List<Food> findAllByKeyword(String keyword) {
        if (keyword != null) {
            //return foodRepo.search(keyword);
            return foodRepo.findAll();
        }
        return (List<Food>) foodRepo.findAll();
    }

    @Override
        public Food save(Food food) {
            return foodRepo.save(food);
        }

    @Override
    public Food findById(Long id) {

        return foodRepo.findById(id).get();
    }

    @Override
    public void deleteById(Long Id) {
        this.foodRepo.deleteById(Id);
    }

    @Override
    public Page<Food> findPaginatedFood(int pageNo, int pageSize,String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();

        Pageable pageable = of(pageNo - 1, pageSize, sort);

        if (keyword != null) {
            return foodRepo.findAll(keyword, pageable);
        }
        return this.foodRepo.findAll(pageable);
    }

}


