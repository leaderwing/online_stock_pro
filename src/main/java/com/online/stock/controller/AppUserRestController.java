package com.online.stock.controller;

import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for authentication and user details. All the web services of
 * this rest controller will be only accessible for ADMIN users only
 *
 * @author Hendi Santika
 */
@RestController
@RequestMapping(value = "/api")
public class AppUserRestController {
    @Autowired
    private AfmastRepository afmastRepository;

    /**
     * Web service for getting all the appUsers in the application.
     *
     * @return list of all AppUser
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<Afmast> users() {
        return afmastRepository.findAll();
    }

    /**
     * Web service for getting a user by his ID
     *
     * @param id appUser ID
     * @return appUser
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<Afmast> userById(@PathVariable Long id) {
        Afmast appUser = afmastRepository.findOne(id);
        if (appUser == null) {
            return new ResponseEntity<Afmast>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Afmast>(appUser, HttpStatus.OK);
        }
    }

    /**
     * Method for deleting a user by his ID
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Afmast> deleteUser(@PathVariable Long id) {
        Afmast appUser = afmastRepository.findOne(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (appUser == null) {
            return new ResponseEntity<Afmast>(HttpStatus.NO_CONTENT);
        } else if (appUser.getUsername().equalsIgnoreCase(loggedUsername)) {
            throw new RuntimeException("You cannot delete your account");
        } else {
            afmastRepository.delete(appUser);
            return new ResponseEntity<Afmast>(appUser, HttpStatus.OK);
        }

    }

    /**
     * Method for adding a appUser
     *
     * @param appUser
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Afmast> createUser(@RequestBody Afmast appUser) {
        if (afmastRepository.findOneByUsername(appUser.getUsername()) != null) {
            throw new RuntimeException("Username already exist");
        }
        return new ResponseEntity<Afmast>(afmastRepository.save(appUser), HttpStatus.CREATED);
    }

}
