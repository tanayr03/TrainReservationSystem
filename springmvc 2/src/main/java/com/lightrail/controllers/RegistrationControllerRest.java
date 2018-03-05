package com.lightrail.controllers;

import com.domain.User;
import com.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationControllerRest {

    @Autowired
    private ItineraryService itineraryService;

    /* LOGIN AND REGISTRATION */

    @CrossOrigin
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody User user ) {
        try {
            String result = "Success";
            result = itineraryService.insertIntoUsers(user);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error, user already exists"+e, HttpStatus.OK);
        }
    }

    @CrossOrigin
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/loginUser", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody User user ) {
        try {
            String result = "Success";
            User user1 = itineraryService.checkLoginCredentials(user);
            return new ResponseEntity(user1, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error, Login Failed!"+e, HttpStatus.BAD_REQUEST);
        }
    }
}
