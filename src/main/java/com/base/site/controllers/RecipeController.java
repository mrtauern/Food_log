package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class RecipeController {
    Logger log = Logger.getLogger(DailyLogController.class.getName());

    private final String RECIPES = "recipes";
    private final String RECIPEINFO = "recipeInfo";
    private final String EDITRECIPE = "editRecipe";
    private final String EDITRECIPETEST = "editRecipeTest";
    private final String EDIT_FOOD_IN_RECIPE = "EditFoodInRecipe";


    private final String CREATE_RECIPE = "createRecipe";
    private final String ADD_FOOD_TO_RECIPE = "addFoodToRecipe";
    private final String SAVE_FOOD_TO_RECIPE = "saveFoodToRecipe";
    private final String REDIRECT = "redirect:/";
    private final String ARCHIVED_RECIPES = "showRecipeArchive";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeFoodService recipeFoodService;

    @Autowired
    UsersService usersService;

    @Autowired
    FoodService foodService;

    @Autowired
    PrivateFoodService privateFoodService;


    @GetMapping("/recipes")
    public String recipes(Model model) {

        log.info("Recipes getmapping called...");
        List<Recipe> recipes = recipeService.findAllFkUser(usersService.getLoggedInUser());

        if(recipes.size() > 0) {
            /*
            for (Recipe recipe: recipes) {
                log.info("Recipe name: "+recipe.getName()+" Total weight: "+recipe.getTotal_weight());
                for (RecipeFood recipeFood: recipe.getAmounts()) {
                    log.info("Amount: "+recipeFood.getAmount()+" foodname: "+recipeFood.getFood().getName());
                }
            }*/
        }else {
            log.info("no recipes found for user ");
            Recipe noRecipe = new Recipe();
            noRecipe.setName("You have no recipe's, you can create one by clicking the create recipe button.");
            noRecipe.setTotal_weight(0);
            recipes.add(noRecipe);
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser());


        return RECIPES;
    }

    @GetMapping("/recipeInfo/{id}")
    public String recipeInfo(Model model,Recipe recipe,@PathVariable( value ="id") Long id) {
        log.info("  RecipeInfo getmapping is called... with id :: " + id);

        model.addAttribute("recipe", recipeService.findRecipeById(id));
        model.addAttribute("recipeFood", recipeFoodService.findByRecipe(recipe));
        model.addAttribute("totalCalories", recipeService.calculateCaloriesInRecipe(recipeFoodService.findByRecipe(recipe), recipeService.findRecipeById(id)));
        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return RECIPEINFO;
    }
    @GetMapping("/editRecipe/{id}")
    public String editRecipe(@PathVariable(value = "id") long id, Model model, Recipe recipe) {
        log.info("editRecipe /edit/id getmapping called... id: "+id);

        model.addAttribute("recipe", recipeService.findRecipeById(id));
        model.addAttribute("recipeFood", recipeFoodService.findByRecipe(recipe));
        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return EDITRECIPE;
    }

    @PostMapping("/editRecipe")
    public String saveRecipe(@ModelAttribute("recipe") Recipe recipe, Model model) {
        log.info("  PostMapping editRecipe is called...");

        recipe.setFkUser(usersService.getLoggedInUser());
        recipeService.save(recipe);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return  REDIRECT+RECIPES;
    }

    @GetMapping("removeFoodFromRecipe/{recipeId}/{id}")
    public String removeStudentFromClass(@PathVariable("id")long id,@PathVariable("recipeId")long recipeId ) {
        log.info("removeFoodFromRecipe getmapping called with RecipeFoodID = :: " + id);
        recipeFoodService.deleteById(id);

        return REDIRECT + EDITRECIPE+"/"+ recipeId;
    }

    @GetMapping("/editFoodInRecipe/{recipeId}/{recipeFoodId}")
    public String editFoodInRecipe(@PathVariable("recipeId")long recipeId,@PathVariable("recipeFoodId")long recipeFoodId, Recipe recipe, Model model) {
        log.info("editFoodInRecipe Getmapping is called with recipeId: "+recipeId);

        model.addAttribute("recipeId", recipeId);

        return EDIT_FOOD_IN_RECIPE;
    }

    @PostMapping("/saveFoodInRecipe/{foodId}/{recipeId}")
    public String saveFoodInRecipe(@PathVariable("foodId")long foodId, @PathVariable("recipeId")long recipeId, RecipeFood recipeFood) {
        log.info("saveFoodInRecipe Postmapping is called with recipeId: "+recipeId+" and foodId: "+foodId+" recipeFood:::"+recipeFood.getAmount());

        if(usersService.getLoggedInUser().getId() == recipeService.findById(recipeId).getFkUser().getId()) {
            recipeFoodService.saveRecipeFoodData(recipeFood, foodId, recipeId);

            return REDIRECT+EDITRECIPE+"/"+recipeId;
        } else {
            return REDIRECT+RECIPES;
        }
    }

    @GetMapping("/createRecipe")
    public String createRecipe(Model model) {
        log.info("  createRecipe getmapping is called...");

        model.addAttribute("recipe", new Recipe());

        return CREATE_RECIPE;
    }

    @PostMapping("/createRecipe")
    public String createRecipe(@ModelAttribute("recipe") Recipe recipe) {
        log.info("  PostMapping createRecipe is called...");

        recipe.setFkUser(usersService.getLoggedInUser());
        recipe = recipeService.save(recipe);

        log.info("Recipe id: "+recipe.getId());

        return REDIRECT+ADD_FOOD_TO_RECIPE+"/"+recipe.getId();
    }

    //Might need to be moved to food controller
    @GetMapping("/addFoodToRecipe/{recipeId}")
    public String addFoodToRecipe(@PathVariable("recipeId")long recipeId, @Param("keyword") String keyword, Model model) {
        log.info("addFoodToRecipe Getmapping is called with recipeId: "+recipeId);

        model.addAttribute("recipeId", recipeId);
        model.addAttribute("foodlist", foodService.findAllByKeyword(keyword));
        model.addAttribute("pfoodlist", privateFoodService.findAllByKeyword(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("recipeFood", new RecipeFood());

        return ADD_FOOD_TO_RECIPE;
    }

    @PostMapping("/saveRecipeFood/{foodId}/{recipeId}")
    public String saveRecipeFood(@PathVariable("foodId")long foodId, @PathVariable("recipeId")long recipeId, RecipeFood recipeFood) {
        log.info("saveRecipeFood Postmapping is called with recipeId: "+recipeId+" and foodId: "+foodId+" recipeFood:::"+recipeFood.getAmount());

        if(usersService.getLoggedInUser().getId() == recipeService.findById(recipeId).getFkUser().getId()) {
            recipeFood.setRecipe(recipeService.findById(recipeId));
            recipeFood.setFood(foodService.findById(foodId));
            recipeFoodService.save(recipeFood);

            return REDIRECT+ADD_FOOD_TO_RECIPE+"/"+recipeId;
        } else {
            return REDIRECT+RECIPES;
        }

    }

    @GetMapping("/archiveRecipe/{status}/{id}")
    public String archiveRecipe(@PathVariable("id")long id, @PathVariable("status")boolean status) {
        log.info("Archive recipe getmapping called with id:"+id);

        Recipe recipe = recipeService.findById(id);
        if (recipe.getFkUser().getId() == usersService.getLoggedInUser().getId()) {
            recipe.setArchived(status);
            recipeService.save(recipe);
        }
        return REDIRECT+RECIPES;

    }

    @GetMapping("/showRecipeArchive")
    public String showRecipeArchive(Model model) {
        log.info("showRecipeArchive getmapping called...");
        List<Recipe> recipes = recipeService.findAllFkUser(usersService.getLoggedInUser());

        if(recipes.size() <= 0) {
            log.info("no archived recipes found for user ");
            Recipe noRecipe = new Recipe();
            noRecipe.setName("You have no archived recipe's");
            noRecipe.setTotal_weight(0);
            recipes.add(noRecipe);
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser());

        return ARCHIVED_RECIPES;
    }
}
