package com.balance.controller;

import com.balance.model.StairHistory;
import com.balance.service.StairHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@RestController
public class StairHistoryController {

    private StairHistoryService stairHistoryService;

    @Autowired
    public void setStairHistoryService(StairHistoryService stairHistoryService) {
        this.stairHistoryService = stairHistoryService;
    }

    @RequestMapping(value = "/getStairCount/{id}", method = RequestMethod.GET)
    public StairHistory getStairCount(@PathVariable Integer id) {
        int total = 0;
        Iterator<StairHistory> iterator = stairHistoryService.listAllStairHistory().iterator();
        List<StairHistory> myList = new ArrayList<>();
        Date fechaactual = new Date();
        while(iterator.hasNext()){
            myList.add(iterator.next());
        }
        for(StairHistory sh : myList){
            if(sh.getUser().equals(id) &&
                    fechaactual.getDay()==sh.getDate().getDay() &&
                    fechaactual.getMonth()==sh.getDate().getMonth() &&
                    fechaactual.getYear()==sh.getDate().getYear()){
                total += sh.getCantidad();
            }
        }
        StairHistory resp = new StairHistory();
        resp.setCantidad(total);
        resp.setId(67620);
        return resp;
    }

}