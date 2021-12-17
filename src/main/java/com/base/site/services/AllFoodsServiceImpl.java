package com.base.site.services;

import com.base.site.models.AllFoods;
import com.base.site.repositories.AllFoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@Service("AllFoodsService")
public class AllFoodsServiceImpl implements AllFoodsService{

    @Autowired
    AllFoodsRepository allFoodsRepository;

    @Autowired
    UsersService usersService;

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

    @Override
    public Model getPaginatedModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword, HttpSession session) {
        int pageSize = 15;

        Page<AllFoods> page = findPaginatedFoods(pageNo, pageSize, sortField, sortDir, keyword);
        List<AllFoods> listFood = page.getContent();
        List<AllFoods> foodlistSearched = findAllByKeyword(keyword);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalFood", page.getTotalElements());

        model.addAttribute("foodlist", listFood);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        //model.addAttribute("foodlistSearched", foodlistSearched);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return model;
    }
}
