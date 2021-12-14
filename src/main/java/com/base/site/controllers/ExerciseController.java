package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.services.DailyLogService;
import com.base.site.services.ExerciseService;
import com.base.site.services.LogTypeService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class ExerciseController {

    @Autowired
    ExerciseService exerciseService;
    @Autowired
    UsersService usersService;
    @Autowired
    DailyLogService dailyLogService;
    @Autowired
    LogTypeService logTypeService;

    Logger log = Logger.getLogger(ExerciseController.class.getName());

    private final String REDIRECT = "redirect:/";
    private final String EXERCISE = "exercise";
    private final String CREATE_EXERCISE = "createExercise";
    private final String UPDATE_EXERCISE = "updateExercise";
    private final String DAILYLOG = "dailyLog";
    private final String ADD_EXERCISE_TO_DAILYLOG = "addExerciseToDailyLog";
    private final String CREATE_EXERCISE_IN_DAILYLOG = "createExerciseInDailyLog";
    private final String UPDATE_EXERCISE_IN_DAILYLOG = "updateExerciseInDailyLog";

    public ExerciseController() {}

    @GetMapping("/exercise")
    public String exercise(Model model, Exercise exercise, @Param("keyword") String keyword, HttpSession session) {
        log.info("  get mapping exercise is called");
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);
        List<DailyLog> edList = dailyLogService.findAllByKeyword(keyword);

        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("edList", edList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Exercise list");
        model.addAttribute("selectedPage", "exercise");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return EXERCISE;
    }



    @GetMapping("/createExercise")
    public String createExercise(Model model, HttpSession session) {
        log.info("  createExercise is called ");

        Exercise exercise = new Exercise();
        model.addAttribute("exercise", exercise);
        model.addAttribute("pageTitle", "Create exercise");
        model.addAttribute("selectedPage", "exercise");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return CREATE_EXERCISE;
    }

    @PostMapping("/saveExercise")
    public String saveExercise(@ModelAttribute("food") Exercise exercise, Model model, RedirectAttributes redAt) {
        log.info("  PostMapping saveExercise is called ");

        Long exerciseId = exercise.getId();

        exerciseService.save(exercise);

        if(exerciseId == null) {
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Exercise successfully created");
        } else {
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Exercise successfully updated");
        }

        return  REDIRECT + EXERCISE;
    }

    @GetMapping("/updateExercise/{id}")
    public String updateExercise(@PathVariable(value = "id") Long id, Model model, HttpSession session) {
        log.info("  GetMapping updateExercise is called ");
        Exercise exercise = exerciseService.findById(id);
        model.addAttribute("exercise", exercise);
        model.addAttribute("pageTitle", "Edit exercise");
        model.addAttribute("selectedPage", "exercise");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return UPDATE_EXERCISE;
    }

    @GetMapping("/deleteExercise/{id}")
    public String deleteExercise(@PathVariable(value = "id") Long id, Model model, RedirectAttributes redAt) {
        log.info("  GetMapping deleteExercise is called ");
        this.exerciseService.deleteById(id);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Exercise successfully deleted");

        return REDIRECT + EXERCISE;
    }


    @GetMapping({"/addExerciseToDailyLog", "/addExerciseToDailyLog/{date}"})
    public String addExerciseToDailyLog(Model model, @Param("keyword") String keyword,
                                        @PathVariable(required = false, value = "date") String dateString,
                                        HttpSession session) {
        log.info("  get mapping addExerciseToDailyLog is called");
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        model.addAttribute("date", date.toString());

        model.addAttribute("pageTitle", "Add exercise to daily log");
        model.addAttribute("selectedPage", "dailyLog");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("keyword", keyword);

        return ADD_EXERCISE_TO_DAILYLOG;
    }
    @GetMapping({"/createExerciseInDailyLog/{id}", "/createExerciseInDailyLog/{id}/{date}"})
    public String createExerciseInDailyLog(@PathVariable(value = "id") Long id, Model model, DailyLog dailyLog,
                                            @PathVariable(required = false, value = "date") String dateString,
                                            HttpSession session) {
        log.info("  Get mapping createExerciseInDailyLog is called ");

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);
        Exercise exercise = exerciseService.findById(id);

        model.addAttribute("date", date.toString());

        model.addAttribute("logType", logTypeService.findAll());
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("exercise", exercise);
        model.addAttribute("pageTitle", "Add exercise to daily log");
        model.addAttribute("selectedPage", "dailyLog");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return CREATE_EXERCISE_IN_DAILYLOG;
    }
    @PostMapping({"/saveExerciseInDailyLog", "/saveExerciseInDailyLog/{date}"})
    public String saveExerciseInDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog, Exercise exercise, Model model,
                                         @PathVariable(required = false, value = "date") String dateString, RedirectAttributes redAt,
                                         HttpSession session) {
        log.info("  Post Mapping saveExerciseInDailyLog is called ");

        Exercise exerciseId = exerciseService.findById(exercise.getId());

        dailyLog.setDatetime(dateString == null ? LocalDate.now() : LocalDate.parse(dateString));
        String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        dailyLog.setFkExercise(exerciseId);
        dailyLog.setFkUser(usersService.getLoggedInUser(session));
        dailyLog.setFkLogType(logTypeService.findByType("Exercise"));

        dailyLogService.save(dailyLog);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Exercise successfully added to daily log");

        return  REDIRECT + DAILYLOG +"/"+sDatetime;

    }
    @GetMapping({"/updateExerciseInDailyLog/{id}", "/updateExerciseInDailyLog/{id}/{date}"})
    public String updateExerciseInDailyLog(@PathVariable(value = "id") Long id, Model model,
                                           @PathVariable(required = false, value = "date") String dateString,
                                           HttpSession session) {
        log.info("  GetMapping updateExerciseInDailyLog is called ");
        DailyLog dailyLog= dailyLogService.findById(id);
        model.addAttribute("date", dateString);

        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("pageTitle", "Edit exercise in daily log");
        model.addAttribute("selectedPage", "dailyLog");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return UPDATE_EXERCISE_IN_DAILYLOG;
    }

    @PostMapping({"/updateExerciseInDailyLog", "/updateExerciseInDailyLog/{date}"})
    public String updateExerciseInDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog,
                                           @PathVariable(required = false, value = "date") String dateString, RedirectAttributes redAt,
                                           HttpSession session) {
        log.info("  Post Mapping updateExerciseInDailyLog is called ");

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        dailyLog.setDatetime(date);

        dailyLog.setFkUser(usersService.getLoggedInUser(session));
        dailyLog.setFkLogType(logTypeService.findByType("Exercise"));

        dailyLogService.save(dailyLog);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Exercise successfully updated in daily log");

        return  REDIRECT + DAILYLOG+"/"+dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    }

    @GetMapping({"/deleteExerciseFromDailyLog/{id}", "/deleteExerciseFromDailyLog/{id}/{date}"})
    public String deleteExerciseFromDailyLog(@PathVariable(value = "id") Long id,
                                             @PathVariable(required = false, value = "date") String dateString, RedirectAttributes redAt,
                                             HttpSession session) {
        log.info("  GetMapping deleteEExerciseFromDailyLog is called ");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        this.dailyLogService.deleteById(id);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Exercise successfully deleted from daily log");

        return REDIRECT + DAILYLOG +"/"+date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
