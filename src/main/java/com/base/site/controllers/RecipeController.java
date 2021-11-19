package com.base.site.controllers;

import com.base.site.services.DailyLogService;
import com.base.site.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

@Controller
public class RecipeController {
    Logger log = Logger.getLogger(DailyLogController.class.getName());

    @Autowired
    RecipeService recipeService;
}
