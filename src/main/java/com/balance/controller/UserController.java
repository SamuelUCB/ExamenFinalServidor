package com.balance.controller;

import com.balance.model.*;
import com.balance.repository.TerminalRepository;
import com.balance.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Controller
public class UserController {

    private UserService userService;
    private TerminalService terminalService;
    private BandModelService bandModelService;
    private CaloriesHistoryService caloriesHistoryService;
    private PulseHistoryService pulseHistoryService;
    private StepsHistoryService stepsHistoryService;
    private LocationHistoryService locationHistoryService;

    @Autowired
    public void setLocationHistoryService(LocationHistoryService locationHistoryService) {
        this.locationHistoryService = locationHistoryService;
    }

    @Autowired
    public void setStepsHistoryService(StepsHistoryService stepsHistoryService) {
        this.stepsHistoryService = stepsHistoryService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
    public void setCaloriesHistoryService(CaloriesHistoryService caloriesHistoryService){
        this.caloriesHistoryService=caloriesHistoryService;
    }

    @Autowired
    public void setPulseHistoryService(PulseHistoryService pulseHistoryService) {
        this.pulseHistoryService = pulseHistoryService;
    }

    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
    public String showUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/user";
    }

    @RequestMapping(value = "/admin/user/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin/home";
    }

    @RequestMapping(value = "/admin/user/edit/{id}",method = RequestMethod.GET)
    public String editUser(@PathVariable Integer id,Model model) {
        model.addAttribute("user",userService.getUserById(id));
        return "admin/userForm";
    }


    @RequestMapping(value = "/admin/changepassword",method = RequestMethod.GET)
    public String changepassworduser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user",userService.getUserById(user.getId()));
        return "admin/Password";
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.POST)
    public String saveUser(@Valid User user) {
        userService.saveUserEdited(user);
        return "redirect:/admin/home";
    }

    @RequestMapping(value = "/admin/userHistory/pulse/{id}", method = RequestMethod.GET)
    public String viewPulseHistory(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id);
        Iterator<PulseHistory> iterator = pulseHistoryService.listAllPulseHistory().iterator();
        ArrayList<PulseHistory> resp = new ArrayList<PulseHistory>();
        while(iterator.hasNext()){
            PulseHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
                resp.add(aux);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("pulses", resp);

        return "admin/userHistory/pulse";
    }

    @RequestMapping(value = "/admin/userHistory/calories/{id}", method = RequestMethod.GET)
    public String viewCaloriesHistory(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id);
        Iterator<CaloriesHistory> iterator2 = caloriesHistoryService.listAllCaloriesHistorys().iterator();
        ArrayList<CaloriesHistory> resp2=new ArrayList<CaloriesHistory>();
        while(iterator2.hasNext()){
            CaloriesHistory aux = iterator2.next();
            if(aux.getUser().equals(user.getId())) {
                resp2.add(aux);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("calories",resp2);

        return "admin/userHistory/calories";
    }

    @RequestMapping(value = "admin/userHistory/steps/{id}", method = RequestMethod.GET)
    public String viewStepsHistory(@PathVariable Integer id,Model model) {
        User user = userService.getUserById(id);
        Iterator<StepsHistory> iterator2 = stepsHistoryService.listAllStepsHistory().iterator();
        ArrayList<StepsHistory> resp2=new ArrayList<StepsHistory>();
        while(iterator2.hasNext()){
            StepsHistory aux = iterator2.next();
            if(aux.getUser().equals(user.getId())) {
                resp2.add(aux);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("steps",resp2);

        return "admin/userHistory/steps";
    }


    @RequestMapping(value = "/admin/terminals",method = RequestMethod.GET)
    public String listTerminals(Model model){
        model.addAttribute("terminals",terminalService.listAllTerminals());
        return "admin/terminals";
    }

    @RequestMapping(value="/admin/terminal/edit/{id}",method =RequestMethod.GET)
    public String editTerminal(@PathVariable Integer id,Model model){
        model.addAttribute("terminal",terminalService.getTerminalById(id));
        model.addAttribute("bands",bandModelService.listAllBandModels());
        terminalService.saveTerminal(terminalService.getTerminalById(id));
        return "admin/terminalForm";
    }

    @RequestMapping(value = "/admin/terminal/{id}", method = RequestMethod.POST)
    public String saveTerminal(@Valid Terminal terminal,BindingResult bindingResult,@PathVariable Integer id,Model model) {
        //obteniendo antigua terminal y su usuario
       Terminal terminalOld = terminalService.getTerminalById(id);
       User user = terminalOld.getUser();
       //quitando la terminal al usuario
       if (user != null) {
           user.setTerminal(null);
           userService.saveUserEdited(user);
       }
        model.addAttribute("bands", bandModelService.listAllBandModels());

       if(!bindingResult.hasErrors()) {
           if (terminal == null) {
               model.addAttribute("errorTerminal", "The serial field was empty");
           } else {
               //borrando la anterior terminal
               terminalService.deleteTerminal(id);
               if (terminalService.getTerminalById(terminal.getSerial()) != null) {
                   model.addAttribute("errorTerminal", "That serial already exists");
               } else {
                   //darle al usuario de la antigua terminal la nueva
                   if (user != null) {
                       user.setTerminal(terminal);
                       user.setBand(terminal.getBandModel().getName());
                       userService.saveUserEdited(user);
                   }
                   terminalService.saveTerminal(terminal);
                   return "redirect:/admin/terminals";
               }
           }
       }else{
           model.addAttribute("errorTerminal", "The serial field was empty");
       }
       //si fallo devolverle la terminal anterior a su respectivo usuario
       if (user != null) {
           user.setTerminal(terminalOld);
           user.setBand(terminalOld.getBandModel().getName());
           userService.saveUserEdited(user);
       }
       //guardando la antigua terminal por fallo
       terminalService.saveTerminal(terminalOld);
       model.addAttribute("terminal", terminal);
       model.addAttribute("id", id);

        return "admin/terminalForm";
    }


    //LimitedUser Controller--------------------------------------------------------

    @RequestMapping(value = "/user/profile",method = RequestMethod.GET)
    public String viewProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Terminal ant=terminalService.getTerminalById(userService.getUserById(user.getId()).getTerminal().getSerial());
        ant.setActive(true);
        terminalService.saveTerminal(ant);
        model.addAttribute("user", user);
        return "limited/profile";
    }

    @RequestMapping(value = "/user/edit",method = RequestMethod.GET)
    public String editProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user",userService.getUserById(user.getId()));
        model.addAttribute("terminalUser",userService.getUserById(user.getId()).getTerminal());
        Terminal ant=terminalService.getTerminalById(userService.getUserById(user.getId()).getTerminal().getSerial());
        ant.setActive(false);
        terminalService.saveTerminal(ant);
        model.addAttribute("bands",bandModelService.listAllBandModels());

        return "limited/editProfile";
    }

    @RequestMapping(value = "/user/changepassword",method = RequestMethod.GET)
    public String changepassword(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user",userService.getUserById(user.getId()));
        return "limited/Password";
    }

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String saveLimitedUser(@Valid User user, BindingResult bindingResult,Model model) {
        model.addAttribute("bands",bandModelService.listAllBandModels());

        if(!bindingResult.hasErrors()){
            if(user.getTerminal()==null){
                model.addAttribute("errorTerminal","The serial doesn't exist or the serial field was empty");
            }else if(!user.getTerminal().getBandModel().getName().equals(user.getBand())){
                model.addAttribute("errorTerminal","That serial does not match the band model");
            }else if(terminalService.getTerminalById(user.getTerminal().getSerial()).isActive()) {
                model.addAttribute("errorTerminal", "That serial is already in use");
            }else{

                user.getTerminal().setActive(true);
                terminalService.saveTerminal(user.getTerminal());
                user.setTerminal(user.getTerminal());
                userService.saveUserEdited(user);
                return "redirect:/user/profile/";
            }
        }
        return "limited/editProfile";

    }


    //Historiales
    @RequestMapping(value = "/user/CaloriesHistory", method = RequestMethod.GET)
    public String getCaloriesHistory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Iterator<CaloriesHistory> iterator = caloriesHistoryService.listAllCaloriesHistorys().iterator();
        ArrayList<CaloriesHistory> resp=new ArrayList<CaloriesHistory>();
        while(iterator.hasNext()){
            CaloriesHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
                resp.add(aux);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("calories",resp);
        return "limited/caloriesHistory";
    }

    @RequestMapping(value = "/user/PulseHistory", method = RequestMethod.GET)
    public String getPulsesHistory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Iterator<PulseHistory> iterator = pulseHistoryService.listAllPulseHistory().iterator();
        ArrayList<PulseHistory> resp = new ArrayList<PulseHistory>();
        while(iterator.hasNext()){
            PulseHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
               resp.add(aux);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("pulses", resp);
        return "limited/pulseHistory";
    }

    @RequestMapping(value = "/user/StepsHistory", method = RequestMethod.GET)
    public String getStepsHistory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Iterator<StepsHistory> iterator = stepsHistoryService.listAllStepsHistory().iterator();
        ArrayList<StepsHistory> resp = new ArrayList<StepsHistory>();
        while(iterator.hasNext()){
            StepsHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
                resp.add(aux);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("steps", resp);
        return "limited/stepsHistory";
    }

    @RequestMapping(value = "/user/LocationHistory", method = RequestMethod.GET)
    public String getLocationHistory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Iterator<LocationHistory> iterator = locationHistoryService.listAllLocationHistory().iterator();
        ArrayList<LocationHistory> resp = new ArrayList<LocationHistory>();
        while(iterator.hasNext()){
            LocationHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
                resp.add(aux);
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("locations", resp);
        return "limited/locationHistory";
    }

    @RequestMapping(value = "/user/Map", method = RequestMethod.GET)
    public String getMap(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Iterator<LocationHistory> iterator = locationHistoryService.listAllLocationHistory().iterator();
        int cantidad = 0;
        ArrayList<Float> listLatitud = new ArrayList<>();
        ArrayList<Float> listLongitud = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        while(iterator.hasNext()){
            LocationHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
                cantidad++;
                listLatitud.add(aux.getLatitude());
                listLongitud.add(aux.getLongitude());
                listName.add("Location " + cantidad);
            }
        }
        model.addAttribute("latitudes", listLatitud);
        model.addAttribute("longitudes", listLongitud);
        model.addAttribute("titulos", listName);
        return "limited/map";
    }

    @RequestMapping(value = "/user/LastLocation", method = RequestMethod.GET)
    public String getLastLocation(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Iterator<LocationHistory> iterator = locationHistoryService.listAllLocationHistory().iterator();
        int cantidad = 0;
        ArrayList<Float> listLatitud = new ArrayList<>();
        ArrayList<Float> listLongitud = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        while(iterator.hasNext()){
            LocationHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
                cantidad++;
                listLatitud.add(aux.getLatitude());
                listLongitud.add(aux.getLongitude());
                listName.add("Location " + cantidad);
            }
        }

        if (!listLatitud.isEmpty()) {
            model.addAttribute("latitud", listLatitud.get(listLatitud.size() - 1));
            model.addAttribute("longitud", listLongitud.get(listLongitud.size() - 1));
            model.addAttribute("titulo", listName.get(listName.size() - 1));
        }

        return "limited/mapLastLocation";
    }


    @RequestMapping(value = "/user/dayLocations", method = RequestMethod.GET)
    public String getDayLocations(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Iterator<LocationHistory> iterator = locationHistoryService.listAllLocationHistory().iterator();
        Date fechaactual=new Date();
        int cantidad = 0;
        ArrayList<Float> listLatitud = new ArrayList<>();
        ArrayList<Float> listLongitud = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        while(iterator.hasNext()){
            LocationHistory aux = iterator.next();
            if(aux.getUser().equals(user.getId())) {
                if(aux.getUser().equals(user.getId()) &&
                        aux.getDate().getDay()==fechaactual.getDay() &&
                        aux.getDate().getMonth()==fechaactual.getMonth() &&
                        aux.getDate().getYear()==fechaactual.getYear() ) {
                            cantidad++;
                            listLatitud.add(aux.getLatitude());
                            listLongitud.add(aux.getLongitude());
                            listName.add("Location " + cantidad);
                }
            }
        }
        System.out.println(listName);


        model.addAttribute("latitudes", listLatitud);
        model.addAttribute("longitudes", listLongitud);
        model.addAttribute("titulos", listName);


        return "limited/mapDayLocations";
    }
}