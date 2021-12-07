package com.base.site.services;

import com.base.site.models.Food;
import com.base.site.models.PrivateFood;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("PrivateFoodService")
public interface PrivateFoodService {

    List<PrivateFood> findAll();
    List<PrivateFood> findAllByKeyword(String keyword);
    PrivateFood save(PrivateFood privateFood);
    PrivateFood findById(Long id);
    void deleteById(Long id);
    Page<PrivateFood> findPaginatedAddFood(int pageNo, int pageSize, String sortField, String sortDirection, String keyword);

}
