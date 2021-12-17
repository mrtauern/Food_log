package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


    private final String ADD_RECIPE_TO_DAILYLOG = "addRecipeToDailyLog";
    private final String CREATE_RECIPE_IN_DAILYLOG = "createRecipeInDailyLog";
    private final String UPDATE_RECIPE_IN_DAILYLOG = "updateRecipeInDailyLog";
    //private final String CREATE_RECIPE_IN_DAILYLOG = "createRecipeInDailyLog";
    private final String DAILYLOG = "dailyLog";

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

    @Autowired
    LogTypeService logTypeService;

    @Autowired
    DailyLogService dailyLogService;

    @GetMapping("/recipes")
    public String recipes(Model model, HttpSession session,
                          @Param("keyword") String keyword) {

        log.info("Recipes getmapping called...");

        model.addAttribute("recipes", recipeService.getRecipesForUserAndSearch(usersService.getLoggedInUser(session), keyword ));
        model.addAttribute("keyword", keyword);
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

    @PostMapping("/saveFoodInRecipe/{type}/{foodId}/{recipeId}")
    public String saveFoodInRecipe(@PathVariable(required = false, value = "foodId")long foodId,
                                   @PathVariable(required = false, value = "type") String type,
                                   @PathVariable("recipeId")long recipeId, RecipeFood recipeFood, RedirectAttributes redAt, HttpSession session) {
        log.info("saveFoodInRecipe Postmapping is called with recipeId: "+recipeId+" and foodId: "+foodId+" recipeFood Amount:::"+recipeFood.getAmount());

        if(usersService.getLoggedInUser(session).getId().equals(recipeService.findById(recipeId).getFkUser().getId())) {
            if (type.equals("foods")) {
                log.info("hhhhhhhhhhhhhhhhh ...."+ type);
                recipeFoodService.saveRecipeFoodData(recipeFood, foodId, recipeId);
            }else if (type.equals("foodp")){
                log.info("22222222222222222 ....."+ type);

                recipeFoodService.saveRecipePrivateFoodData(recipeFood, foodId, recipeId);
            }
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Food is successfully saved in recipe");

            return REDIRECT+EDITRECIPE+"/"+recipeId;
        }
        else {
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "error");
            redAt.addFlashAttribute("message", "You can only edit your own recipe");

            return REDIRECT+RECIPES;
        }
    }


/*
// -.-.-.-.-. Test
    @PostMapping({"/saveFoodInRecipe/{type}/{recipeFoodId}/{recipeId}/{foodId}", "/saveFoodInRecipe/{type}/{recipeFoodId}/{recipeId}/{pfoodId}"})
    public String saveFoodInRecipe(RecipeFood recipeFood, Food food, PrivateFood privateFood,
                                   RedirectAttributes redAt, HttpSession session ,
                                   @PathVariable(required = false, value = "type") String type,
                                   @PathVariable(required = false, value= "foodId")long foodId,
                                   @PathVariable(required = false, value= "pfoodId")long pfoodId,
                                   @PathVariable("recipeFoodId")long recipeFoodId,
                                   @PathVariable("recipeId")long recipeId) {
        log.info("saveFoodInRecipe Postmapping is called with  recipeFoodId : "+recipeFoodId +  " and recipeId: "+recipeId+" and FoodId: "+foodId+" recipeFood Amount:::"+recipeFood.getAmount());
        log.info("klijhgfghjklkjhgfdgh"+ type);
        if(usersService.getLoggedInUser(session).getId().equals(recipeService.findById(recipeId).getFkUser().getId())) {
            recipeFood.setRecipe(recipeService.findById(recipeId));

            if (type.equals("foods")) {
                log.info("klijhgfghjklkjhjhgfgfdfghgfdfggfdgh"+ type);

                Food foodIds = foodService.findById(food.getId());
                recipeFood.setFood(foodIds);
                recipeFood.setAmount(recipeFood.getAmount());
            }
            if (type.equals("foodp")) {
                PrivateFood foodIdp = privateFoodService.findById(foodId);
                recipeFood.setPrivateFood(foodIdp);
                recipeFood.setAmount(recipeFood.getAmount());

            }
            recipeFood.setRecipe(recipeService.findById(recipeId));
            recipeFoodService.save(recipeFood);

            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Food is successfully saved in recipe");
        }
            return REDIRECT+EDITRECIPE+"/"+recipeId;
        }
 */


        /* else {
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "error");
            redAt.addFlashAttribute("message", "You can only edit your own recipe");

            return REDIRECT+RECIPES;
        }
    }
*/

    @GetMapping("/createRecipe")
    public String createRecipe(Model model, HttpSession session) {
        log.info("  createRecipe getmapping is called...");

        model.addAttribute("recipe", new Recipe());
        model.addAttribute("pageTitle", "Create recipe");
        model.addAttribute("selectedPage", "recipe");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

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

        if(usersService.getLoggedInUser(session).getId().equals(recipeService.findById(recipeId).getFkUser().getId())) {
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
    public String showRecipeArchive(Model model, HttpSession session, String keyword) {
        log.info("showRecipeArchive getmapping called...");
        List<Recipe> recipes = recipeService.getRecipesForUserAndSearch(usersService.getLoggedInUser(session), keyword);

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


    @GetMapping({"/addRecipeToDailyLog", "/addRecipeToDailyLog/{date}"})
    public String addRecipeToDailyLog(Model model, @Param("keyword") String keyword,HttpSession session,
                                        @PathVariable(required = false, value = "date") String dateString) {
        log.info("  get mapping addRecipeToDailyLog is called");

        List<Recipe> recipeList =  recipeService.getRecipesForUserAndSearch(usersService.getLoggedInUser(session), keyword );

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        model.addAttribute("date", date.toString());
        model.addAttribute("pageTitle", "Add recipe to daily log");
        model.addAttribute("selectedPage", "dailyLog");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("recipeList", recipeList);
        model.addAttribute("keyword", keyword);

        return ADD_RECIPE_TO_DAILYLOG;
    }
    @GetMapping({"/createRecipeInDailyLog/{id}", "/createRecipeInDailyLog/{id}/{date}"})
    public String createRecipeInDailyLog(@PathVariable(value = "id") Long id, Model model, DailyLog dailyLog,HttpSession session,
                                         @PathVariable(required = false, value = "date") String dateString
                                        ) {
        log.info("  Get mapping createRecipeInDailyLog is called ");

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);
        Recipe recipe = recipeService.findById(id);

        model.addAttribute("date", date.toString());
        model.addAttribute("logType", logTypeService.findAll());
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("recipe", recipe);
        model.addAttribute("pageTitle", "Add Recipe to daily log");
        model.addAttribute("selectedPage", "dailyLog");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return CREATE_RECIPE_IN_DAILYLOG;
    }
    @PostMapping({"/saveRecipeInDailyLog/", "/saveRecipeInDailyLog/{date}"})
    public String saveRecipeInDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog, Recipe recipe,HttpSession session,
                                       @PathVariable(required = false, value = "date") String dateString,
                                       @RequestParam("log_type") String logType,
                                       RedirectAttributes redAt) {
        log.info("  Post Mapping saveRecipeInDailyLog is called ");

        dailyLog.setFkLogType(logTypeService.findByType(logType));

        Recipe recipeId = recipeService.findById(recipe.getId());

        dailyLog.setDatetime(dateString == null ? LocalDate.now() : LocalDate.parse(dateString));
        String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        dailyLog.setRecipe(recipeId);
        dailyLog.setFkUser(usersService.getLoggedInUser(session));
        dailyLogService.save(dailyLog);


        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Recipe successfully added to daily log");

        return  REDIRECT + DAILYLOG +"/"+sDatetime;

    }


    @GetMapping({"/updateRecipeInDailyLog/{id}", "/updateRecipeInDailyLog/{id}/{date}"})
    public String updateRecipeInDailyLog(@PathVariable(value = "id") Long id, Model model,HttpSession session,
                                           @PathVariable(required = false, value = "date") String dateString) {
        log.info("  GetMapping updateRecipeInDailyLog is called ");
        DailyLog dailyLog= dailyLogService.findById(id);
        model.addAttribute("date", dateString);

        //model.addAttribute("logType", logTypeService.findAll());

        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("pageTitle", "Edit exercise in daily log");
        model.addAttribute("selectedPage", "dailyLog");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("logType", dailyLog.getFkLogType().getType());

        return UPDATE_RECIPE_IN_DAILYLOG;
    }

    @PostMapping({"/updateRecipeInDailyLog", "/updateRecipeInDailyLog/{date}"})
    public String updateRecipeInDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog,HttpSession session,
                                         @RequestParam(value = "log_type") String logType,
                                           @PathVariable(required = false, value = "date") String dateString, RedirectAttributes redAt) {
        log.info("  Post Mapping updateRecipeInDailyLog is called ");

        dailyLog.setFkLogType(logTypeService.findByType(logType));

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        dailyLog.setDatetime(date);

        dailyLog.setFkUser(usersService.getLoggedInUser(session));
        //dailyLog.setFkLogType(logTypeService.findByType("Exercise"));

        dailyLogService.save(dailyLog);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Exercise successfully updated in daily log");

        return  REDIRECT + DAILYLOG+"/"+dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    }

    @GetMapping({"/deleteRecipeFromDailyLog/{id}", "/deleteRecipeFromDailyLog/{id}/{date}"})
    public String deleteRecipeFromDailyLog(@PathVariable(value = "id") Long id,
                                             @PathVariable(required = false, value = "date") String dateString, RedirectAttributes redAt) {
        log.info("  GetMapping deleteRecipeFromDailyLog is called ");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        this.dailyLogService.deleteById(id);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Recipe successfully deleted from daily log");

        return REDIRECT + DAILYLOG +"/"+date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
