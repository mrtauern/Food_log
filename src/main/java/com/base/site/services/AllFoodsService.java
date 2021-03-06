package com.base.site.services;

import com.base.site.models.AllFoods;
import com.base.site.models.Food;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service("AllFoodsService")
public interface AllFoodsService {
    List<AllFoods> findAll();
    List<AllFoods> findAllByKeyword(String keyword);
    AllFoods save(AllFoods food);
    AllFoods findById(Long id);
    void deleteById(Long id);
    Page<AllFoods> findPaginatedFoods(int pageNo, int pageSize, String sortField, String sortDirection, String keyword);

    Model getPaginatedModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword, HttpSession session);
}
