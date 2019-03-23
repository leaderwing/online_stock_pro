package com.online.stock.controller;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.model.Afmast;
import com.online.stock.model.Cfmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.repository.CfMastRepository;
import com.online.stock.services.IAccountService;
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
public class AppUserRestController {
    @Autowired
    private AfmastRepository afmastRepository;
    @Autowired
    private CfMastRepository cfMastRepository;
    @Autowired
    private IAccountService accountService;

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
     * @return appUser
     */
    @RequestMapping(value = "/users/name", method = RequestMethod.GET)
    public ResponseEntity<String> getAccName(@RequestParam String custId) {
        Cfmast appUser = cfMastRepository.findByCustid(custId);
        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(appUser.getFullname(), HttpStatus.OK);
        }
    }

    /**
     * get user information
     *
     * @return
     */
    @RequestMapping(value = "/users/info", method = RequestMethod.GET)
    public ResponseEntity<RegisterRequest> getUser() {
        RegisterRequest info = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (loggedUsername == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
           info = accountService.getUserInfo(loggedUsername);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    /**
     * Method for adding a appUser
     *
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody RegisterRequest  request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (!loggedUsername.equals(request.getAcctno())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String result = accountService.updateUserInfo(request);
        System.out.print(result);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
