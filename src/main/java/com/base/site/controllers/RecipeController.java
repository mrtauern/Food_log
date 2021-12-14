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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
    public String recipes(Model model, HttpSession session) {

        log.info("Recipes getmapping called...");

        model.addAttribute("recipes", recipeService.getRecipesForUser(usersService.getLoggedInUser(session)));
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Recipe list");
        model.addAttribute("selectedPage", "recipe");

        return RECIPES;
    }

    @GetMapping("/recipeInfo/{id}")
    public String recipeInfo(Model model,Recipe recipe,@PathVariable( value ="id") Long id, HttpSession session) {
        log.info("  RecipeInfo getmapping is called... with id :: " + id);

        model.addAttribute("recipe", recipeService.findRecipeById(id));
        model.addAttribute("recipeFood", recipeFoodService.findByRecipe(recipe));
        //model.addAttribute("totalCalories", recipeService.calculateCaloriesInRecipe(recipeFoodService.findByRecipe(recipe), recipeService.findRecipeById(id)));
        model.addAttribute("totalCalories", recipeService.findRecipeById(id).getCalculateCaloriesInRecipe());
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Recipe info");
        model.addAttribute("selectedPage", "recipe");


        return RECIPEINFO;
    }
    @GetMapping("/editRecipe/{id}")
    public String editRecipe(@PathVariable(value = "id") long id, Model model, Recipe recipe, HttpSession session) {
        log.info("editRecipe /edit/id getmapping called... id: "+id);

        model.addAttribute("recipe", recipeService.findRecipeById(id));
        model.addAttribute("recipeFood", recipeFoodService.findByRecipe(recipe));
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Edit recipe");
        model.addAttribute("selectedPage", "recipe");


        return EDITRECIPE;
    }

    @PostMapping("/editRecipe")
    public String saveRecipe(@ModelAttribute("recipe") Recipe recipe, Model model, RedirectAttributes redAt, HttpSession session) {
        log.info("  PostMapping editRecipe is called...");

        recipe.setFkUser(usersService.getLoggedInUser(session));
        recipeService.save(recipe);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Recipe is successfully updated");

        return  REDIRECT+RECIPES;
    }

    @GetMapping("removeFoodFromRecipe/{recipeId}/{id}")
    public String removeFoodFromRecipe(@PathVariable("id")long id, @PathVariable("recipeId")long recipeId, RedirectAttributes redAt) {
        log.info("removeFoodFromRecipe getmapping called with RecipeFoodID = :: " + id);
        recipeFoodService.deleteById(id);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Food is successfully removed from recipe");

        return REDIRECT + EDITRECIPE+"/"+ recipeId;
    }
/*
    @GetMapping("/editFoodInRecipe/{recipeId}/{recipeFoodId}")
    public String editFoodInRecipe(@PathVariable("recipeId")long recipeId,@PathVariable("recipeFoodId")long recipeFoodId, Recipe recipe, Model model) {
        log.info("editFoodInRecipe Getmapping is called with recipeId: "+recipeId);

        model.addAttribute("recipeId", recipeId);
        model.addAttribute("pageTitle", "Edit food in recipe");
        model.addAttribute("selectedPage", "recipe");


        return EDIT_FOOD_IN_RECIPE;
    }*/

    @PostMapping("/saveFoodInRecipe/{foodId}/{recipeId}")
    public String saveFoodInRecipe(@PathVariable("foodId")long foodId, @PathVariable("recipeId")long recipeId, RecipeFood recipeFood, RedirectAttributes redAt, HttpSession session) {
        log.info("saveFoodInRecipe Postmapping is called with recipeId: "+recipeId+" and foodId: "+foodId+" recipeFood:::"+recipeFood.getAmount());

        if(usersService.getLoggedInUser(session).getId() == recipeService.findById(recipeId).getFkUser().getId()) {
            recipeFoodService.saveRecipeFoodData(recipeFood, foodId, recipeId);

            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Food is successfully saved in recipe");

            return REDIRECT+EDITRECIPE+"/"+recipeId;
        } else {
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "error");
            redAt.addFlashAttribute("message", "You can only edit your own recipe");

            return REDIRECT+RECIPES;
        }
    }

    @GetMapping("/createRecipe")
    public String createRecipe(Model model) {
        log.info("  createRecipe getmapping is called...");

        model.addAttribute("recipe", new Recipe());
        model.addAttribute("pageTitle", "Create recipe");
        model.addAttribute("selectedPage", "recipe");


        return CREATE_RECIPE;
    }

    @PostMapping("/createRecipe")
    public String createRecipe(@ModelAttribute("recipe") Recipe recipe, RedirectAttributes redAt, HttpSession session) {
        log.info("  PostMapping createRecipe is called...");

        recipe.setFkUser(usersService.getLoggedInUser(session));
        recipe = recipeService.save(recipe);

        log.info("Recipe id: "+recipe.getId());

        return REDIRECT+ADD_FOOD_TO_RECIPE+"/"+recipe.getId();
    }

    //Might need to be moved to food controller
    @GetMapping("/addFoodToRecipe/{recipeId}")
    public String addFoodToRecipe(@PathVariable("recipeId")long recipeId, @Param("keyword") String keyword, Model model, HttpSession session) {
        log.info("addFoodToRecipe Getmapping is called with recipeId: "+recipeId);

        model.addAttribute("recipeId", recipeId);
        model.addAttribute("foodlist", foodService.findAllByKeyword(keyword));
        model.addAttribute("pfoodlist", privateFoodService.findAllByKeyword(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("recipeFood", new RecipeFood());
        model.addAttribute("pageTitle", "Add food to recipe");
        model.addAttribute("selectedPage", "recipe");


        return ADD_FOOD_TO_RECIPE;
    }

    @PostMapping("/saveRecipeFood/{type}/{id}/{recipeId}")
    public String saveRecipeFood(@PathVariable(required = false, value = "type") String type,
                                 @PathVariable("recipeId")long recipeId,
                                 @PathVariable("id")long id,
                                 RecipeFood recipeFood, Food food, PrivateFood privateFood,
                                 RedirectAttributes redAt,
                                 HttpSession session) {
        log.info("saveRecipeFood Postmapping is called with recipeId: "+recipeId+" and foodId: "+id+" recipeFood:::"+recipeFood.getAmount());

        if(usersService.getLoggedInUser(session).getId() == recipeService.findById(recipeId).getFkUser().getId()) {
            recipeFood.setRecipe(recipeService.findById(recipeId));

            if (type.equals("foods")) {
                Food foodIds = foodService.findById(food.getId());
                recipeFood.setFood(foodIds);
            }
            if (type.equals("foodp")) {
                PrivateFood foodIdp = privateFoodService.findById(privateFood.getId());
                recipeFood.setPrivateFood(foodIdp);
            }

            recipeFoodService.save(recipeFood);

            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Food is successfully saved in recipe");

            return REDIRECT+ADD_FOOD_TO_RECIPE+"/"+recipeId;
        } else {
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "error");
            redAt.addFlashAttribute("message", "You can only edit your own recipe");

            return REDIRECT+RECIPES;
        }

    }

    @GetMapping("/archiveRecipe/{status}/{id}")
    public String archiveRecipe(@PathVariable("id")long id, @PathVariable("status")boolean status, RedirectAttributes redAt, HttpSession session) {
        log.info("Archive recipe getmapping called with id:"+id);

        redAt = recipeService.setArchivedAndGetAttributes(redAt, usersService.getLoggedInUser(session).getId(), id, status);

        return REDIRECT+RECIPES;

    }

    @GetMapping("/showRecipeArchive")
    public String showRecipeArchive(Model model, HttpSession session) {
        log.info("showRecipeArchive getmapping called...");
        List<Recipe> recipes = recipeService.findAllFkUser(usersService.getLoggedInUser(session));

        if(recipes.size() <= 0) {
            log.info("no archived recipes found for user ");
            Recipe noRecipe = new Recipe();
            noRecipe.setName("You have no archived recipe's");
            noRecipe.setTotal_weight(0);
            recipes.add(noRecipe);
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Archived recipe list");
        model.addAttribute("selectedPage", "recipe");


        return ARCHIVED_RECIPES;
    }
}
