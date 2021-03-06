package com.balance.controller;

import com.balance.model.*;
import com.balance.service.*;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by da_20 on 31/5/2017.
 */
@RestController
public class BandController {

    private BandService bandService;
    private UserService userService;
    private CaloriesHistoryService caloriesHistoryService;
    private PulseHistoryService pulseHistoryService;
    private StepsHistoryService stepsHistoryService;
    private LocationHistoryService locationHistoryService;
    private StairHistoryService stairHistoryService;

    @Autowired
    public void setStepsHistoryService(StepsHistoryService stepsHistoryService) {
        this.stepsHistoryService = stepsHistoryService;
    }

    @Autowired
    public void setStairHistoryService(StairHistoryService stairHistoryService) {
        this.stairHistoryService = stairHistoryService;
    }

    @Autowired
    public void setLocationHistoryService(LocationHistoryService locationHistoryService) {
        this.locationHistoryService = locationHistoryService;
    }

    @Autowired
    public void setPulseHistoryService(PulseHistoryService pulseHistoryService) {
        this.pulseHistoryService = pulseHistoryService;
    }

    @Autowired
    public void setCaloriesHistoryService(CaloriesHistoryService caloriesHistoryService) {
        this.caloriesHistoryService = caloriesHistoryService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBandService(BandService bandService) {
        this.bandService = bandService;
    }

    @RequestMapping(value = "/band", method = RequestMethod.POST)
    public Band saveBand(@Valid Band band, BindingResult bindingResult, Model model) {
        band.setFecha_registro(new Date());
        caloriesHistoryService.saveCaloriesHistory(new CaloriesHistory(band.getCalories(), band.getUser(), band.getFecha_registro()));
        pulseHistoryService.savePulseHistory(new PulseHistory(band.getBpm(), band.getFecha_registro(), band.getUser(), band.getIntensidad()));
        stepsHistoryService.saveStepsHistory(new StepsHistory(band.getSteps(), band.getDistance(), band.getUser(), band.getFecha_registro()));
        locationHistoryService.saveLocationHistory(new LocationHistory(band.getLatitude(), band.getLongitude(), band.getUser(), band.getFecha_registro()));
        bandService.saveBand(band);
        if(band.getCantidad() != null && band.getTipo() != null) {
            if(band.getCantidad() > 0 && band.getCantidad() < 1000) {
                if(band.getTipo() == 0 || band.getTipo() == 1) {
                    StairHistory guardar = new StairHistory();
                    guardar.setCantidad(band.getCantidad());
                    guardar.setTipo(band.getTipo());
                    guardar.setDate(band.getFecha_registro());
                    guardar.setUser(band.getUser());
                    stairHistoryService.saveStairHistory(guardar);
                }
                else {
                    System.out.println("NO SE GUARDO PORQUE EL TIPO ES ERRONEO");
                }
            }
            else {
                System.out.println("NO SE GUARDO LOS VALORES NO SE CUMPLEN");
            }
        }
        return band;
    }

    @RequestMapping(value = "/bands", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Band>> getBands() {
        return new ResponseEntity(bandService.listAllBands(), HttpStatus.NOT_FOUND);
    }

}