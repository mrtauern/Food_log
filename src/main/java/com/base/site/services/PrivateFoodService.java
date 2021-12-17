package com.base.site.services;

import com.base.site.models.Food;
import com.base.site.models.PrivateFood;
import com.base.site.models.Recipe;
import com.base.site.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service("PrivateFoodService")
public interface PrivateFoodService {

    List<PrivateFood> findAll();
    List<PrivateFood> findAllByKeyword(String keyword);
    PrivateFood save(PrivateFood privateFood);
    PrivateFood findById(Long id);
    void deleteById(Long id);
    List<PrivateFood> findAllByFkUser(Users loggedInUser);

    Page<PrivateFood> findPaginatedAddFood(int pageNo, int pageSize, String sortField, String sortDirection, String keyword);
    List<PrivateFood> getPrivateFoodForUser(Users loggedInUser, String keyword);

    RedirectAttributes setArchivedAndGetAttributes(RedirectAttributes redAt, Long userId, Long id, boolean status);

    void setPfoodArchived(boolean status, PrivateFood food);
}
