package com.base.site.services;

import com.base.site.models.Exercise;
import com.base.site.models.PrivateFood;
import com.base.site.models.Recipe;
import com.base.site.models.Users;
import com.base.site.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.logging.Logger;

@Service("RecipeService")
public class RecipeServiceImpl implements RecipeService{
    Logger log = Logger.getLogger(RecipeServiceImpl.class.getName());

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeFoodService recipeFoodService;

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

}
