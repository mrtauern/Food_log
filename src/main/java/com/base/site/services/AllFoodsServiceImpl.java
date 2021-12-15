package com.base.site.services;

import com.base.site.models.AllFoods;
import com.base.site.models.Food;
import com.base.site.repositories.AllFoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@Service("AllFoodsService")
public class AllFoodsServiceImpl implements AllFoodsService{

    @Autowired
    AllFoodsRepository allFoodsRepository;


    @Override
    public List<AllFoods> findAll() {
        return allFoodsRepository.findAll();
    }

    //not needed?
    @Override
    public List<AllFoods> findAllByKeyword(String keyword) {
        return allFoodsRepository.findAll();
    }

    @Override
    public AllFoods save(AllFoods food) {
        return allFoodsRepository.save(food);
    }

    @Override
    public AllFoods findById(Long id) {
        return allFoodsRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        this.allFoodsRepository.deleteById(id);
    }

    @Override
    public Page<AllFoods> findPaginatedFoods(int pageNo, int pageSize, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();

        Pageable pageable = of(pageNo - 1, pageSize, sort);

        if (keyword != null) {
            return allFoodsRepository.findAll(keyword, pageable);
        }
        return this.allFoodsRepository.findAll(pageable);
    }
}
