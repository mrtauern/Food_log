package com.base.site.controllers;

import com.base.site.models.PrivateFood;
import com.base.site.models.Recipe;
import com.base.site.services.PrivateFoodService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class PrivateFoodController {
    Logger log = Logger.getLogger(PrivateFoodController.class.getName());

    @Autowired
    PrivateFoodService privateFoodService;

    @Autowired
    UsersService usersService;

    private final String REDIRECT = "redirect:/";
    private final String PRIVATEFOOD = "privateFood";
    private final String UPDATE_PRIVATEFOOD = "updatePrivateFood";
    private final String CREATE_PRIVATEFOOD = "createPrivateFood";
    private final String ARCHIVED_PRIVATE_FOOD = "showPrivateFoodArchive";

    @GetMapping("/privateFood")
    public String privateFood(Model model, @Param("keyword") String keyword, HttpSession session) {
        log.info("  get mapping private food is called");

        model.addAttribute("pfood", privateFoodService.getPrivateFoodForUser(usersService.getLoggedInUser(session), keyword));

        model.addAttribute("keyword", keyword);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Private food list");
        model.addAttribute("selectedPage", "privateFood");

        return PRIVATEFOOD;
    }

    @GetMapping("/createPrivateFood")
    public String createPrivateFood(Model model, HttpSession session) {
        log.info("  createPrivateFood is called ");

        model.addAttribute("privateFood", new PrivateFood());
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Create private food");
        model.addAttribute("selectedPage", "privateFood");

        return CREATE_PRIVATEFOOD;
    }

    @PostMapping("/savePrivateFood")
    public String savePrivateFood(@ModelAttribute("privateFood") PrivateFood privateFood, RedirectAttributes redAt, HttpSession session) {
        log.info("  PostMapping savePrivateFood is called ");

        Long privateFoodId = privateFood.getId();

        log.info("Object: "+privateFood);
        log.info("Id: "+privateFood.getId());
        log.info("Name: "+privateFood.getName());
        log.info("Kilojoule: "+privateFood.getEnergy_kilojoule());

        privateFood.setFkUser(usersService.getLoggedInUser(session));
        privateFoodService.save(privateFood);

        log.info("Size: "+privateFoodService.findAll().size());

        if(privateFoodId == null) {
            log.info("Create private food");
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Private food is successfully created");
        } else {
            log.info("Update private food");
            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "Private food is successfully updated");
        }

        return REDIRECT + PRIVATEFOOD;
    }

    @GetMapping("/updatePrivateFood/{id}")
    public String updatePrivateFood(@PathVariable(value = "id") Long id, Model model, HttpSession session) {
        log.info("  GetMapping updatePrivateFood is called ");

        model.addAttribute("privateFood", privateFoodService.findById(id));
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Edit private food");
        model.addAttribute("selectedPage", "privateFood");

        return UPDATE_PRIVATEFOOD;
    }

    @GetMapping("/deletePrivateFood/{id}")
    public String deletePrivateFood(@PathVariable(value = "id") Long id, Model model, RedirectAttributes redAt) {
        log.info("  GetMapping deletePrivateFood by id is called ");

        this.privateFoodService.deleteById(id);

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "success");
        redAt.addFlashAttribute("message", "Private food is successfully deleted");

        return REDIRECT + PRIVATEFOOD;
    }

    @GetMapping("/archivePrivateFood/{status}/{id}")
    public String archivePrivateFood(@PathVariable("id")long id, @PathVariable("status")boolean status, RedirectAttributes redAt, HttpSession session) {
        log.info("archivePrivateFood getmapping called with id:"+id);

        redAt = privateFoodService.setArchivedAndGetAttributes(redAt, usersService.getLoggedInUser(session).getId(), id, status);

        return REDIRECT+PRIVATEFOOD;

    }

    @GetMapping("/showPrivateFoodArchive")
    public String showPrivateFoodArchive(Model model, HttpSession session, String keyword) {
        log.info("showPrivateFoodArchive getmapping called...");
        List<PrivateFood> privateFoods = privateFoodService.getPrivateFoodForUser(usersService.getLoggedInUser(session), keyword);

        if(privateFoods.size() <= 0) {
            log.info("no PrivateFood Archive found for user ");
            PrivateFood noPrivateFood = new PrivateFood();
            noPrivateFood.setName("You have no Archived  PrivateFood ");

            privateFoods.add(noPrivateFood);
        }

        model.addAttribute("privateFoods", privateFoods);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("pageTitle", "Archived Private Food list");
        model.addAttribute("selectedPage", "Private Food");


        return ARCHIVED_PRIVATE_FOOD;
    }

}
