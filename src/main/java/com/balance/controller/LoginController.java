package com.balance.controller;

import javax.mail.MessagingException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.balance.Mail.SmtpMailSender;
import com.balance.model.*;
import com.balance.service.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.SecureRandom;
import java.security.Security;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	private TerminalService terminalService;
	private BandModelService bandModelService;
	private CaloriesHistoryService caloriesHistoryService;
	private PulseHistoryService pulseHistoryService;
	private StepsHistoryService stepsHistoryService;
	private AgeRangeService ageRangeService;

	@Autowired
    public void setStepsHistoryService(StepsHistoryService stepsHistoryService) {
        this.stepsHistoryService = stepsHistoryService;
    }

	@Autowired
	public void setCaloriesHistoryService(CaloriesHistoryService caloriesHistoryService) {
		this.caloriesHistoryService = caloriesHistoryService;
	}
	@Autowired
	public void setPulseHistoryService(PulseHistoryService pulseHistoryService) {
		this.pulseHistoryService = pulseHistoryService;
	}

	@Autowired
	public void setTerminalService(TerminalService terminalService){
		this.terminalService=terminalService;
	}

	@Autowired
	public void setBandModelService(BandModelService bandModelService){
		this.bandModelService=bandModelService;
	}

	@Autowired
	public void setAgeRangeService(AgeRangeService ageRangeService) {
		this.ageRangeService = ageRangeService;
	}

	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public String login(){
		return "login";
	}

	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public String registration(Model model){
		model.addAttribute("user", new User());
		model.addAttribute("bands",bandModelService.listAllBandModels());
		return "registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		User userExists = userService.findUserByEmail(user.getEmail());
		model.addAttribute("bands",bandModelService.listAllBandModels());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "There is already a user registered with the email provided");
		}

		if(!bindingResult.hasErrors()) {
            if(user.getTerminal()==null){
				model.addAttribute("errorTerminal","El serial no existe o el campo serial esta vacio");
			}else if(!user.getTerminal().getBandModel().getName().equals(user.getBand())){
                    model.addAttribute("errorTerminal","El serial no concuerda con el modelo de banda");
				}else if(terminalService.getTerminalById(user.getTerminal().getSerial()).isActive()) {
                model.addAttribute("errorTerminal", "El serial ya fue activado");
            }else{
				User userTerminal=terminalService.getTerminalById(user.getTerminal().getSerial()).getUser();
				if(userTerminal!=null){
					model.addAttribute("errorTerminal", "El serial ya esta en uso");
				}else{
					Date birthday=new Date();
					Integer age=birthday.getYear()-user.getBirthday().getYear();
                    if(birthday.getMonth()-user.getBirthday().getMonth()<0){
                        age--;
                    }
					user.setAge(age);
					userService.saveUser(user);
					model.addAttribute("successMessage", "El usuario se registro correctamente");
					model.addAttribute("user", new User());
				}
            }
		}
		return "registration";
	}

	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public String home(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if (user == null) {
			return "redirect:/";
		}
		model.addAttribute("userName", "Bienvenido " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		model.addAttribute("userList", userService.listAllUsers());
		model.addAttribute("user", user);
		return "admin/home";
	}

    @RequestMapping(value="/user/home", method = RequestMethod.GET)
    public String homeExclusive(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Integer steps = 0;
        Integer calories=0;
        long distance=0;
        Integer bpm=0;

        Iterator<CaloriesHistory> iteratorC = caloriesHistoryService.listAllCaloriesHistorys().iterator();
        Iterator<StepsHistory> iteratorS = stepsHistoryService.listAllStepsHistory().iterator();
        Iterator<PulseHistory> iteratorP = pulseHistoryService.listAllPulseHistory().iterator();
		Iterator<AgeRange> iteratorA = ageRangeService.listAllAgeRanges().iterator();

        StepsHistory auxS = new StepsHistory();
        CaloriesHistory auxC = new CaloriesHistory();
        PulseHistory auxP = new PulseHistory();
		AgeRange auxA = new AgeRange();
		Date fechaactual=new Date();
        while(iteratorS.hasNext()){
            auxS = iteratorS.next();
            if(auxS.getUser().equals(user.getId()) &&
					auxS.getDate().getDay()==fechaactual.getDay() &&
					auxS.getDate().getMonth()==fechaactual.getMonth() &&
					auxS.getDate().getYear()==fechaactual.getYear() ) {
				steps += auxS.getSteps();
				distance += auxS.getDistance();
			}
        };


        while(iteratorC.hasNext()){
            auxC = iteratorC.next();
            if(auxC.getUser().equals(user.getId()) &&
					auxC.getDate().getDay()==fechaactual.getDay() &&
					auxC.getDate().getMonth()==fechaactual.getMonth() &&
					auxC.getDate().getYear()==fechaactual.getYear() ) {
				calories += auxC.getCalories();
			}
        }

        PulseHistory fechaMayor = null;

		while(iteratorP.hasNext()){
			auxP = iteratorP.next();
			if(auxP.getUser().equals(user.getId()) &&
					auxP.getDate().getDay()==fechaactual.getDay() &&
					auxP.getDate().getMonth()==fechaactual.getMonth() &&
					auxP.getDate().getYear()==fechaactual.getYear() ) {

				if(fechaMayor == null) {
					fechaMayor = auxP;
					bpm = auxP.getBpm();
				}
				else {
					if(auxP.getDate().after(fechaMayor.getDate())) {
						fechaMayor = auxP;
						bpm = auxP.getBpm();
					}
				}
			}
		}

		Boolean flag = false;
		AgeRange auxA2 = new AgeRange();


		while(iteratorA.hasNext()){
			auxA = iteratorA.next();
			if(flag == false){
				if(user.getAge() <= auxA.getAge() ){
					flag = true;
					auxA2 = auxA;

				}
			}

		}

		if(flag == false){
			auxA2 = auxA;
		}



		model.addAttribute("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		model.addAttribute("userMessage","Content Available Only for Users with Limited Role");
		model.addAttribute("user", user);
        model.addAttribute("countSteps",steps);
        model.addAttribute("countCalories",calories);
        model.addAttribute("countDistance",distance);
        model.addAttribute("countBpm",bpm);
        model.addAttribute("id",user.getId());
		model.addAttribute("metaStart",auxA2.getMetaStart());
		model.addAttribute("metaFinish",auxA2.getMetaFinish());
		model.addAttribute("max",auxA2.getMaximum());
        return "user/home";
    }

	@RequestMapping(value="/default", method = RequestMethod.GET)
	public String defaultAfterLogin()
	{
		Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		if (roles.contains("ADMIN")) {
			return "redirect:/admin/home";
		}
		return "redirect:/user/home";
	}

	@RequestMapping(value = "/access-denied", method = RequestMethod.GET)
	public String accesoDenegado() {
		return "access-denied"; //Solo devuelve un mensaje
	}

	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public String listUsers(Model model) {
		model.addAttribute("Users",userService.listAllUsers());
		return "users";
	}

}