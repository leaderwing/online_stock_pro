package com.online.stock.controller;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IAccountService;
import com.online.stock.services.IThirdPartyService;
import com.online.stock.utils.FileUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private AfmastRepository afmastRepository;
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
        String errDetail = "";
        if (afmastRepository.findOneByUsername(request.getAcctno()) != null) {
            return new ResponseEntity<>("Đã tồn tại tên tài khoản!",HttpStatus.CONFLICT);
        }
        if (afmastRepository.findOneByEmail(request.getEmail()) != null) {
            return new ResponseEntity<>("Đã tồn tại tài khoản email!", HttpStatus.CONFLICT);
        }
        String genPassword = FileUtils.genRandomPassword(6);
        String errCode = accountService.register(request,genPassword);
        switch (errCode) {
            case "030001" :
                errDetail = "Số tài khoản đã tồn tại";
                break;
            case "030002":
                errDetail = "Số tài khoản không hợp lệ";
                break;
            case "030005" :
                errDetail = "Không có thông tin loại hình hợp đồng";
                break;
            case "000000" :
                errDetail = "Đăng ký thành công! Vui lòng đợi phê duyệt";
                break;
            case "999999" :
                errDetail = "Không thể tạo tài khoản! Vui lòng đăng ký lại";
                break;
                default:
                    errDetail = "Không thể tạo tài khoản! Vui lòng đăng ký lại";
                    break;
        }
        return new ResponseEntity<>(errDetail,HttpStatus.CREATED);
    }

    /**
     * This method will return the logged user.
     *
     * @param principal
     * @return Principal java security principal object
     */
    @RequestMapping("/user")
    public Afmast user(Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        return afmastRepository.findOneByUsername(loggedUsername);
    }

    /**
     * @param response
     * @return JSON contains token and user after success authentication.
     * @throws IOException
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody String toDo,
                                                     HttpServletResponse response) throws IOException, JSONException {
        String token = null;
        JSONObject jsonObject = new JSONObject(toDo);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        Afmast appUser = afmastRepository.findOneByUsername(username);
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        if (appUser != null && appUser.getPassword().equals(password)) {
            // login success, call vndirect api
            thirdPartyService.getAdminAuthen();
            //gen token jwt
            String role = null;
            switch (appUser.getIsStaft()) {
                case 0 :
                    role = "ROLE_USER";
                    break;
                case 1:
                    role = "ROLE_ADMIN_1";
                    break;
                case 2:
                    role = "ROLE_ADMIN_2";
                    break;
                case 3:
                    role = "ROLE_SADMIN";
                    break;
            }
            List<String> roles = Collections.singletonList(role);
            token = Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(new Date())
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
        if (StringUtils.isBlank(username) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Afmast appUser = afmastRepository.findOneByUsername(username);
        if (appUser != null && appUser.getPassword().equals(oldPassword)) {
            // update new password
            accountService.changePassword(username, newPassword);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<String> resetPassword (@RequestParam String email) {
        Afmast afmast = afmastRepository.findOneByEmail(email);
        if (afmast == null) {
            return new ResponseEntity<>("Không tìm thấy tài khoản email!",HttpStatus.NOT_FOUND);
        }
        //gen new password
        String newPass = FileUtils.genRandomPassword(6);
        return new ResponseEntity<>("Gửi email thay đổi mật khẩu thành công!", HttpStatus.OK);
    }
}
