package com.base.site.services;

import com.base.site.models.*;
import com.base.site.repositories.AllFoodsRepository;
import com.base.site.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.data.domain.PageRequest.of;

@Service("RecipeService")
public class RecipeServiceImpl implements RecipeService{
    Logger log = Logger.getLogger(RecipeServiceImpl.class.getName());

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeFoodService recipeFoodService;

    @Autowired
    AllFoodsRepository allFoodsRepository;

    @Autowired
    AllFoodsService allFoodsService;

    @Autowired
    UsersService usersService;

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public List<Recipe> findAllByKeyword(String keyword) {
        if (keyword != null) {
            return recipeRepository.search(keyword);
        }
        return (List<Recipe>) recipeRepository.findAll();
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id).get();
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Override
    public List<Recipe> findAllFkUser(Users loggedInUser) {
        return recipeRepository.findAllByFkUser(loggedInUser);
    }

    @Override
    public Recipe findRecipeById(long id) {
        Recipe recipe = findById(id);

        return recipe;
    }

    @Override
    public List<Recipe> getRecipesForUserAndSearch(Users loggedInUser, String keyword) {
        List<Recipe> recipesAndSearch;
        if(keyword == null || keyword.equals("") || keyword.equals(" ")) {
            recipesAndSearch = findAllFkUser(loggedInUser);
        } else {
            recipesAndSearch = recipeRepository.findAllByFkUserAndSearch(loggedInUser.getId(), keyword);
        }
        return recipesAndSearch;
    }

    @Override
    public RedirectAttributes setArchivedAndGetAttributes(RedirectAttributes redAt, Long userId, Long id, boolean status) {
        log.info("ID:: "+id);
        Recipe recipe = findById(id);
        if (recipe.getFkUser().getId().equals(userId)) {
            recipe.setArchived(status);
            save(recipe);

            if(status == true){
                redAt.addFlashAttribute("showMessage", true);
                redAt.addFlashAttribute("messageType", "success");
                redAt.addFlashAttribute("message", "Recipe is successfully archived");
            } else {
                redAt.addFlashAttribute("showMessage", true);
                redAt.addFlashAttribute("messageType", "success");
                redAt.addFlashAttribute("message", "Recipe is successfully restored");
            }
        }
        return redAt;
    }

    @Override
    public Page<AllFoods> findPaginatedFood(int pageNo, int pageSize, String sortField, String sortDirection, String keyword) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        //sort = Sort.by("asc");
        //-------------for testing---------------
        log.info("page numer: "+pageNo);
        Pageable pageable;

        if(pageNo-1<0) {
            pageable = of(pageNo-1, pageSize, sort);
        }
        else {
            pageable = of(pageNo-1, pageSize, sort);
        }
        int count = 1;
        for (AllFoods pageTest: allFoodsRepository.findAll(keyword,pageable)) {

            log.info("result count: "+count+ " name: "+pageTest.getName());
            count++;
        }
        //-------------for testing---------------
        if (keyword != null) {
            return allFoodsRepository.findAll(keyword, pageable);
        }
        return this.allFoodsRepository.findAll(pageable);
    }

    @Override
    public Model getPaginatedAddFoodModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword, HttpSession session) {
        int pageSize = 15;

        Page<AllFoods> page = findPaginatedFood(pageNo,pageSize, sortField, sortDir, keyword);
        List<AllFoods> listFood = page.getContent();
        List<AllFoods> foodlistSearched =allFoodsService.findAllByKeyword(keyword);
        log.info("pages = "+page.getTotalPages());

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalFood", page.getTotalElements());

        model.addAttribute("listFood", listFood);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("foodlistSearched", foodlistSearched);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return model;
    }

}
