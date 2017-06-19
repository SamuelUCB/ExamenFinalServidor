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
            smtpMailSender.send(text1, "Balance Fitness Tracker: Recupera tu cuenta", "¡Hola, "+userExist.getName()+"! Recibimos una solicitud para recuperar su cuenta, ingrese este codigo: "+ stringToken + " en el siguiente <a href='http://localhost:8080/forgotpasswordconfirm' >enlace</a> para recuperar su cuenta. Si no es así ignore este mensaje.<br></br> Gracias por usar Balance Fitness Tracker.");
            return "redirect:/";
        }
        return "redirect:/forgotpassword";
    }

    @RequestMapping(value="/forgotpasswordconfirm", method = RequestMethod.GET)
    public String changepassword() {
        return "changepassword";
    }

    @RequestMapping(value="/changepasswordyes", method = RequestMethod.GET)
    public String changePasswordInForgot(String email,String codigo) {
        User userExists = userService.findUserByEmail(email);
        if (userExists != null) {
            //  Encriptando password


            Token t=tokenService.findTokenByToken(codigo);
            if(t!=null && t.getActive()==true){
                    Date verifyday=new Date();
                    if(t.getExpired_date().after(verifyday)){
                        if(t.getUser_creator_id()==userExists.getId()){
                            userExists.setPassword(bCryptPasswordEncoder.encode(codigo));
                            userService.saveUserEdited(userExists);
                            t.setActive(false);
                            tokenService.saveToken(t);
                            return "redirect:/";
                        }
                        return "changepassword";
                    }

                    return "changepassword";
            }
            return "changepassword";
        }
        return "changepassword";
    }
}