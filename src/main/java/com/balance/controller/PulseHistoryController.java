package com.balance.controller;

import com.balance.model.Band;

import com.balance.model.PulseHistory;
import com.balance.service.BandService;
import com.balance.service.PulseHistoryService;
import com.balance.service.UserService;
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
import java.util.Date;
import java.util.Iterator;

/**
 * Created by da_20 on 31/5/2017.
 */
@RestController
public class PulseHistoryController {
    private BandService bandService;
    private UserService userService;
    private PulseHistoryService pulseHistoryService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBandService(BandService bandService) {
        this.bandService = bandService;
    }

    @Autowired
    public void setPulseHistory(PulseHistoryService pulseHistoryService){
        this.pulseHistoryService = pulseHistoryService;
    }

    @RequestMapping(value = "/Pulse", method = RequestMethod.GET)
    public ResponseEntity<Iterable<PulseHistory>> getPulseHistories() {
        return new ResponseEntity(pulseHistoryService.listAllPulseHistory(), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/getPulse/{id}", method = RequestMethod.GET)
    public PulseHistory getBandPulse(@PathVariable Integer id) {

        Iterator<PulseHistory> iteratorP = pulseHistoryService.listAllPulseHistory().iterator();
        PulseHistory auxP = new PulseHistory();

        PulseHistory fechaMayor = null;
        Date fechaactual=new Date();
        Integer bpm=0;
        while(iteratorP.hasNext()){
            auxP = iteratorP.next();
            if(auxP.getUser().equals(id) &&
                    auxP.getDate().getDay()==fechaactual.getDay() &&
                    auxP.getDate().getMonth()==fechaactual.getMonth() &&
                    auxP.getDate().getYear()==fechaactual.getYear() ) {
                        bpm = auxP.getBpm();

            }
        }
        PulseHistory pulse = new PulseHistory();
        pulse.setBpm(bpm);
        pulse.setId(67620L);
        return pulse;
    }



}

