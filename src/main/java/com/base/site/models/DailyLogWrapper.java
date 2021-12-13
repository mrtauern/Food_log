package com.base.site.models;

import com.base.site.services.DailyLogService;
import com.base.site.services.DailyLogServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyLogWrapper {
    DailyLogService dailyLogService;

    List<DailyLog> dailyLogs = new ArrayList<>();
    List<DailyLog> dailyLogsFoods = new ArrayList<>();
    List<DailyLog> dailyLogsBreakfast = new ArrayList<>();
    List<DailyLog> dailyLogsLunch = new ArrayList<>();
    List<DailyLog> dailyLogsDinner = new ArrayList<>();
    List<DailyLog> dailyLogsMiscellaneous = new ArrayList<>();
    List<DailyLog> dailyLogsPrivateFoods = new ArrayList<>();
    List<DailyLog> dailyLogsExercises = new ArrayList<>();
    List<DailyLog> dailyLogsRecipes = new ArrayList<>();

    DailyLog weight;
    //DailyLog recipe;

    Food nutrition;

    public DailyLogWrapper(List<DailyLog> dailyLogs) {
        this.dailyLogs = dailyLogs;

    }

    public DailyLogService getDailyLogService() {
        return dailyLogService;
    }

    public void setDailyLogService(DailyLogService dailyLogService) {
        this.dailyLogService = dailyLogService;
    }

    public List<DailyLog> getDailyLogs() {
        return dailyLogs;
    }

    public void setDailyLogs(List<DailyLog> dailyLogs) {
        this.dailyLogs = dailyLogs;
    }

    public List<DailyLog> getDailyLogsFoods() {
        return dailyLogsFoods;
    }

    public void setDailyLogsFoods(List<DailyLog> dailyLogsFoods) {
        this.dailyLogsFoods = dailyLogsFoods;
    }

    public void addToDailyLogsFoods(DailyLog dailyLog) {
        dailyLogsFoods.add(dailyLog);
    }

    public List<DailyLog> getDailyLogsBreakfast() {
        return dailyLogsBreakfast;
    }

    public void setDailyLogsBreakfast(List<DailyLog> dailyLogsBreakfast) {
        this.dailyLogsBreakfast = dailyLogsBreakfast;
    }

    public void addToDailyLogsBreakfast(DailyLog dailyLog) {
        dailyLogsBreakfast.add(dailyLog);
    }

    public List<DailyLog> getDailyLogsLunch() {
        return dailyLogsLunch;
    }

    public void setDailyLogsLunch(List<DailyLog> dailyLogsLunch) {
        this.dailyLogsLunch = dailyLogsLunch;
    }

    public void addToDailyLogsLunch(DailyLog dailyLog) {
        dailyLogsLunch.add(dailyLog);
    }

    public List<DailyLog> getDailyLogsDinner() {
        return dailyLogsDinner;
    }

    public void setDailyLogsDinner(List<DailyLog> dailyLogsDinner) {
        this.dailyLogsDinner = dailyLogsDinner;
    }

    public void addToDailyLogsDinner(DailyLog dailyLog) {
        dailyLogsDinner.add(dailyLog);
    }

    public List<DailyLog> getDailyLogsMiscellaneous() {
        return dailyLogsMiscellaneous;
    }

    public void setDailyLogsMiscellaneous(List<DailyLog> dailyLogsMiscellaneous) {
        this.dailyLogsMiscellaneous = dailyLogsMiscellaneous;
    }

    public void addToDailyLogsMiscellaneous(DailyLog dailyLog) {
        dailyLogsMiscellaneous.add(dailyLog);
    }

    public List<DailyLog> getDailyLogsPrivateFoods() {
        return dailyLogsPrivateFoods;
    }

    public void setDailyLogsPrivateFoods(List<DailyLog> dailyLogsPrivateFoods) {
        this.dailyLogsPrivateFoods = dailyLogsPrivateFoods;
    }

    public void addToDailyLogsPrivateFoods(DailyLog dailyLog) {
        dailyLogsPrivateFoods.add(dailyLog);
    }

    public List<DailyLog> getDailyLogsExercises() {
        return dailyLogsExercises;
    }

    public void setDailyLogsExercises(List<DailyLog> dailyLogsExercises) {
        this.dailyLogsExercises = dailyLogsExercises;
    }

    public void addToDailyLogsExercises(DailyLog dailyLog) {
        dailyLogsExercises.add(dailyLog);
    }

    public DailyLog getWeight() {
        return weight;
    }

    public void setWeight(DailyLog weight) {
        this.weight = weight;
    }

    public Food getNutrition() {
        return nutrition;
    }

    public void setNutrition(Food nutrition) {
        this.nutrition = nutrition;
    }



    public List<DailyLog> getDailyLogsRecipes() {
        return dailyLogsRecipes;
    }

    public void setDailyLogsRecipes(List<DailyLog> dailyLogsRecipes) {
        this.dailyLogsExercises = dailyLogsExercises;
    }
    public void addToDailyLogsRecipes(DailyLog dailyLog) {
        dailyLogsRecipes.add(dailyLog);
    }


}
