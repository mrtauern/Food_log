package com.base.site.services;

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
    public List<Recipe> getRecipesForUser(Users loggedInUser) {
        List<Recipe> recipes = findAllFkUser(loggedInUser);

        if(recipes.size() > 0) {

        }else {
            log.info("no recipes found for user ");
            Recipe noRecipe = new Recipe();
            noRecipe.setName("You have no recipe's, you can create one by clicking the create recipe button.");
            noRecipe.setTotal_weight(0);
            recipes.add(noRecipe);
        }
        return recipes;
    }

    @Override
    public RedirectAttributes setArchivedAndGetAttributes(RedirectAttributes redAt, Long userId, Long id, boolean status) {
        log.info("ID:: "+id);
        Recipe recipe = findById(id);
        if (recipe.getFkUser().getId() == userId) {
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
