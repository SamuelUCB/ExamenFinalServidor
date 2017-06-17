package com.balance.controller;

import com.balance.Mail.SmtpMailSender;
import com.balance.configuration.WebMvcConfig;
import com.balance.model.Token;
import com.balance.model.User;
import com.balance.service.TokenService;
import com.balance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by da_20 on 4/6/2017.
 */
@Controller
public class TokenController {

    private TokenService tokenService;

    @Autowired
    private SmtpMailSender smtpMailSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(value="/forgotpassword", method = RequestMethod.GET)
    public String forgotpassword(){
        return "forgot";
    }

    @RequestMapping(value="/send-mail", method = RequestMethod.GET)
    public String sendMail(HttpServletRequest request) throws MessagingException,ServletException {
        String text1= request.getParameter("email");
        if(userService.findUserByEmail(request.getParameter("email"))!=null){
            //Crear token
            SecureRandom random = new SecureRandom();
            long longToken = Math.abs( random.nextLong() );
            String stringToken = Long.toString(longToken,16);
            Token t=new Token(stringToken);
            User userExist=userService.findUserByEmail(request.getParameter("email"));
            t.setUser_creator_id(userExist.getId());

            //modificar token
            tokenService.saveToken(t);

            userExist.setToken(t);
            userService.saveUserEdited(userExist);

            //Enviar mail
            smtpMailSender.send(text1, "Balance Fitness Tracker: Recover your password", "<a href='http://localhost:8080/forgotpasswordconfirm/" + stringToken +" ' > Change password </a>");
            return "redirect:/";
        }
        return "redirect:/forgotpassword";
    }

    @RequestMapping(value="/forgotpasswordconfirm/{tokenS}", method = RequestMethod.GET)
    public String changepassword(@PathVariable String tokenS) {
        Token token=tokenService.findTokenByToken(tokenS);

        Date date_verification=new Date();

        if(token!=null && token.getActive()==true && date_verification.before(token.getExpired_date())){
            User user=userService.getUserById(token.getUser_creator_id());
            System.out.println(user.getEmail());
            if(user!=null && user.getToken().getId()==token.getId()){
                return "changepassword";
            }
            return "redirect:/";
        }
        return "redirect:/";
    }

    @RequestMapping(value="/changepasswordyes", method = RequestMethod.GET)
    public String changePasswordInForgot(String email,String password) {
        User userExists = userService.findUserByEmail(email);
        if (userExists != null) {
            //  Encriptando password
            userExists.setPassword(bCryptPasswordEncoder.encode(password));
            userService.saveUserEdited(userExists);
            //deshabilitando token
            Token t=tokenService.getTokenById(userExists.getToken().getId());
            t.setActive(false);
            tokenService.saveToken(t);
            return "redirect:/";
        }
        return "redirect:changepasswordyes";
    }
}