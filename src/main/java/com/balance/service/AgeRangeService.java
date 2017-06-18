package com.balance.service;

import com.balance.model.AgeRange;

/**
 * Created by KEVIN on 17/06/2017.
 */
public interface AgeRangeService {
    void saveAgeRange(AgeRange ageRange);
    Iterable<AgeRange> listAllAgeRanges();
    AgeRange getAgeRangeById(Integer id);
    void deleteAgeRange(Integer id);
}
