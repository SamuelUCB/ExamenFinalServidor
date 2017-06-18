package com.balance.repository;

import com.balance.model.AgeRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by KEVIN on 17/06/2017.
 */
@Repository("ageRangeRepository")
public interface AgeRangeRepository extends JpaRepository<AgeRange, Integer> {
}
