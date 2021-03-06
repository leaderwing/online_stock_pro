package com.online.stock.controller;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IAccountService;
import com.online.stock.services.IThirdPartyService;
import com.online.stock.utils.Constant;
import com.online.stock.utils.FileUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
    @Autowired
    public  JavaMailSender emailSender;

    /**
     * This method is used for user registration. Note: user registration is not
     * require any authentication.
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody RegisterRequest request) throws JSONException {
        String errDetail = "";
        JSONObject jsonObjectNotErrDetail = new JSONObject();
        if (afmastRepository.findOneByUsername(request.getAcctno()) != null) {
            jsonObjectNotErrDetail.put("result", "Đã tồn tại tên tài khoản!");
            return new ResponseEntity<>(jsonObjectNotErrDetail.toString(),HttpStatus.CONFLICT);
        }
        if (afmastRepository.findFirstByEmail(request.getEmail()) != null) {
            jsonObjectNotErrDetail.put("result", "Đã tồn tại tài khoản email!");
            return new ResponseEntity<>(jsonObjectNotErrDetail.toString(), HttpStatus.CONFLICT);
        }
        String genPassword = FileUtils.genRandomPassword(6);
        String hashPassword = FileUtils.hashString(genPassword);
        String errCode = accountService.register(request,hashPassword);
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
        //send mail
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, multipart, "utf-8");
            StringBuilder htmlMsg = new StringBuilder("<html> <head><meta charset='UTF-8'></head>");
            htmlMsg.append("<body>");
            htmlMsg.append("<h3>Thông tin đăng nhập tài khoản của bạn là </h3>"
                    +"<p><b> Username: </b>" + "<i>"+request.getAcctno()+"</i> </p>"
                    + "<p><b> Password: </b>" + "<i>"+genPassword+"</i> </p>");
            htmlMsg.append("<p> Try cập địa chỉ sau để đăng nhập vào hệ thống: http://stock88.com.vn</p>");
            htmlMsg.append("</body> </html>");
            message.setContent(htmlMsg.toString(), "text/html; charset=\"UTF-8\"");
            helper.setTo(request.getEmail());
            helper.setSubject("Thông tin đăng nhập tài khoản Stock88.com.vn");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(message);
        JSONObject jsonObject = new JSONObject();
        jsonObjectNotErrDetail.put("result", errDetail);
        return new ResponseEntity<>(jsonObject.toString(),HttpStatus.CREATED);
    }

    /**
     * This method will return the logged user.
     *
     * @return Principal java security principal object
     */
    @RequestMapping("/random/custid")
    public ResponseEntity<String> getRandomCustId() throws JSONException {
       JSONObject jsonObject = new JSONObject();
       String custId = accountService.getCustId("0001");
       jsonObject.put("result", custId);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
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
        if (appUser != null && appUser.getPassword().equals(FileUtils.hashString(password))
            && "A".equals(appUser.getStatus())) {
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

    @RequestMapping(value = "/doimk", method = RequestMethod.PUT)
    public ResponseEntity<Void> changePassword(@RequestBody String changeReq) throws JSONException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        JSONObject jsonObject = new JSONObject(changeReq);
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        if (StringUtils.isBlank(username) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Afmast appUser = afmastRepository.findOneByUsername(username);
        if (appUser != null && appUser.getPassword().equals(FileUtils.hashString(oldPassword))) {
            // update new password
            accountService.changePassword(username, FileUtils.hashString(newPassword));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/resetmk", method = RequestMethod.GET)
    public ResponseEntity<String> resetPassword (@RequestParam String email) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Afmast afmast = afmastRepository.findFirstByEmail(email);
        if (afmast == null) {
            jsonObject.put("result","Không tìm thấy tài khoản email!");
            return new ResponseEntity<>(jsonObject.toString(),HttpStatus.NOT_FOUND);
        }
        //gen new password
        String newPass = FileUtils.genRandomPassword(6);
        afmast.setPassword(FileUtils.hashString(newPass));
        afmastRepository.save(afmast);
        // send mail
        MimeMessage message = emailSender.createMimeMessage();
        boolean multipart = true;

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, multipart, "utf-8");
            StringBuilder htmlMsg = new StringBuilder("<html> <head><meta charset='UTF-8'></head>");
            htmlMsg.append("<body>");
            htmlMsg.append("<h3>Thông tin đăng nhập của bạn đã được reset lại như sau: </h3>"
                    +"<p><b> Username: </b>" + "<i>"+afmast.getUsername()+"</i> </p>"
                    + "<p><b> Password: </b>" + "<i>"+newPass+"</i> </p>");
            htmlMsg.append("<p> Try cập địa chỉ sau để đăng nhập vào hệ thống: http://stock88.com.vn</p>");
            htmlMsg.append("</body> </html>");
            message.setContent(htmlMsg.toString(), "text/html; charset=\"UTF-8\"");
            helper.setTo(email);
            helper.setSubject("Thông tin đăng nhập tài khoản Stock88.com.vn");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(message);
        jsonObject.put("result","Gửi email thay đổi mật khẩu thành công!");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    public static void main(String[] args) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(Constant.MAIL_ADDRESS);
        mailSender.setPassword(Constant.MAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        MimeMessage message = mailSender.createMimeMessage();
        boolean multipart = true;

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, multipart, "utf-8");
            StringBuilder htmlMsg = new StringBuilder("<html> <head><meta charset='UTF-8'></head>");
            htmlMsg.append("<body>");
            htmlMsg.append("<h3>Thông tin đăng nhập tài khoản của bạn là </h3>"
                    +"<p><b> Username: </b>" + "<i>abcd</i> </p>"
                    + "<p><b> Password: </b>" + "<i>123456</i> </p>");
            htmlMsg.append("<p> Try cập địa chỉ sau để đăng nhập vào hệ thống: http://stock88.com.vn</p>");
            htmlMsg.append("</body> </html>");
            message.setContent(htmlMsg.toString(), "text/html; charset=\"UTF-8\"");
            helper.setTo("quyict.hut@gmail.com");
            helper.setSubject("Thông tin đăng nhập tài khoản Stock88.com.vn");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}
