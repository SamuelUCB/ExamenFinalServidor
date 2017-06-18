package com.balance.service;

import com.balance.model.AgeRange;
import com.balance.repository.AgeRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by KEVIN on 17/06/2017.
 */
@Service
public class AgeRangeServiceImpl implements AgeRangeService {
    @Autowired
    private AgeRangeRepository ageRangeRepository;


    @Override
    public void saveAgeRange(AgeRange ageRange) {
        ageRangeRepository.save(ageRange);
    }

    @Override
    public Iterable<AgeRange> listAllAgeRanges() {
        return ageRangeRepository.findAll();
    }

    @Override
    public AgeRange getAgeRangeById(Integer id) {
        return ageRangeRepository.getOne(id);
    }

    @Override
    public void deleteAgeRange(Integer id) {
        ageRangeRepository.delete(id);
    }



}
