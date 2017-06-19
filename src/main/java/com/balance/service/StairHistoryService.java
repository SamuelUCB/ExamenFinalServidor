package com.balance.service;

import com.balance.model.StairHistory;

public interface StairHistoryService {
    void saveStairHistory(StairHistory stairHistory);
    Iterable<StairHistory> listAllStairHistory();
    StairHistory getStairHistoryById(Integer id);
    void deleteStairHistory(Integer id);
}