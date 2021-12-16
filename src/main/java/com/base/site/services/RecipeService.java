package com.base.site.services;

import com.base.site.models.AllFoods;
import com.base.site.models.Exercise;
import com.base.site.models.Recipe;
import com.base.site.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service("RecipeService")
public interface RecipeService {
    List<Recipe> findAll();

    List<Recipe> findAllByKeyword(String keyword);

    Recipe findById(Long id);

    Recipe save(Recipe recipe);

    void deleteById(Long id);

    void delete(Recipe recipe);

    List<Recipe> findAllFkUser(Users loggedInUser);

    Recipe findRecipeById(long id);

    List<Recipe> getRecipesForUserAndSearch(Users loggedInUser, String keyword);

    RedirectAttributes setArchivedAndGetAttributes(RedirectAttributes redAt, Long userId, Long id, boolean status);

    Model getPaginatedAddFoodModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword, HttpSession session);

    Page<AllFoods> findPaginatedFood(int pageNo, int pageSize, String sortField, String sortDirection, String keyword);
}

