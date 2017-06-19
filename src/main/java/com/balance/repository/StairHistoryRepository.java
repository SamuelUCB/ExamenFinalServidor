package com.balance.repository;

import com.balance.model.StairHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StairHistoryRepository extends JpaRepository<StairHistory, Integer> {
}