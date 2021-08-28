package com.lemoncode21.springbootdownloadupload.service.implementation;

import com.lemoncode21.springbootdownloadupload.model.BrandCars;
import com.lemoncode21.springbootdownloadupload.repository.BrandCarsRepository;
import com.lemoncode21.springbootdownloadupload.service.BrandCarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandCarsImplementation implements BrandCarsService {

    @Autowired
    private BrandCarsRepository brandCarsRepository;

    @Override
    public List<BrandCars> getAll() {
        return this.brandCarsRepository.findAll();
    }

    @Override
    public void save(BrandCars brandCars) {
        this.brandCarsRepository.save(brandCars);
    }
}
