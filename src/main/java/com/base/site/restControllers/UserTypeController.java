package com.base.site.restControllers;

import com.base.site.models.UserType;
import com.base.site.services.UserTypeService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/userType")
public class UserTypeController {

    public UserTypeController(){

    }

    Logger log = Logger.getLogger(UserTypeController.class.getName());

    @Autowired
    UserTypeService userTypeService;

    @GetMapping
    public ResponseEntity<List<UserType>> allUserTypes(){
        try {
            List<UserType> userTypes = userTypeService.findAll();
            if (userTypes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(userTypes, HttpStatus.OK);
        } catch (NoSuchElementException e){
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserType> oneUserType(@PathVariable Long id){
        try {
            return new ResponseEntity<>(userTypeService.findById(id), HttpStatus.OK);
        } catch (NoSuchElementException e){
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<UserType> addUser(@RequestBody UserType userType){
        log.info(userType.toString());
        /*user.setCountry(countriesService.findById(58L));
        user.setOrders(null);
        user.setReviews(null);*/
        try {
            return new ResponseEntity<>(userTypeService.save(userType), HttpStatus.CREATED);
        } catch (NoSuchElementException e){
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserType> editUser(@RequestBody UserType userType, @PathVariable Long id){
        try {
            UserType _userType = userTypeService.findById(id);
            userType.setId(_userType.getId());
            return new ResponseEntity<>(userTypeService.save(userType), HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e){
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserType(@PathVariable Long id){
        try {
            userTypeService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e){
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
