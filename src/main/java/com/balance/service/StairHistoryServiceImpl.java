package com.balance.service;

import com.balance.model.StairHistory;
import com.balance.repository.StairHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StairHistoryServiceImpl implements StairHistoryService{

    @Autowired
    StairHistoryRepository stairHistoryRepository;

    @Override
    public void saveStairHistory(StairHistory stairHistory) {
        stairHistoryRepository.save(stairHistory);
    }

    @Override
    public Iterable<StairHistory> listAllStairHistory() {
        return stairHistoryRepository.findAll();
    }

    @Override
    public StairHistory getStairHistoryById(Integer id) {
        return stairHistoryRepository.findOne(id);
    }

    @Override
    public void deleteStairHistory(Integer id) {
        stairHistoryRepository.delete(id);
    }
}