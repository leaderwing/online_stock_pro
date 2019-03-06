package com.online.stock.controller;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.model.AppUser;
import com.online.stock.repository.AppUserRepository;
import com.online.stock.services.IAccountService;
import com.online.stock.services.IThirdPartyService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * All web services in this controller will be available for all the users
 *
 * @author Hendi Santika
 */
@RestController
public class UserAuthenticationController {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private IThirdPartyService thirdPartyService;
    @Autowired
    private IAccountService accountService;

    /**
     * This method is used for user registration. Note: user registration is not
     * require any authentication.
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody RegisterRequest request) {
        if (appUserRepository.findOneByUsername(request.getAcctno()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        String errCode = accountService.register(request);
        return new ResponseEntity<>(errCode,HttpStatus.CREATED);
    }

    /**
     * This method will return the logged user.
     *
     * @param principal
     * @return Principal java security principal object
     */
    @RequestMapping("/user")
    public AppUser user(Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        return appUserRepository.findOneByUsername(loggedUsername);
    }

    /**
     * @param username
     * @param password
     * @param response
     * @return JSON contains token and user after success authentication.
     * @throws IOException
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password,
                                                     HttpServletResponse response) throws IOException {
        String token = null;
        AppUser appUser = appUserRepository.findOneByUsername(username);
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        if (appUser != null && appUser.getPassword().equals(password)) {
            // login success, call vndirect api
            thirdPartyService.getAdminAuthen();
            //gen token jwt
            token = Jwts.builder().setSubject(username).claim("roles", appUser.getRoles()).setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
            tokenMap.put("token", token);
            tokenMap.put("user", appUser);
            return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
        } else {
            tokenMap.put("token", null);
            return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
        }
    }
    @RequestMapping(value = "/doimk", method = RequestMethod.POST)
    public ResponseEntity<Void> changePassword(@RequestParam String username, @RequestParam String oldPassword,
                                    @RequestParam String newPassword) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AppUser appUser = appUserRepository.findOneByUsername(username);
        if(appUser != null && appUser.getPassword().equals(oldPassword)) {
            // update new password
            accountService.changePassword(username,newPassword);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
