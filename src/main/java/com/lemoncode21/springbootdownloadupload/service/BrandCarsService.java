package com.lemoncode21.springbootdownloadupload.service;

import com.lemoncode21.springbootdownloadupload.model.BrandCars;
import java.util.List;

public interface BrandCarsService {

    List<BrandCars> getAll();

    void save(BrandCars brandCars);
}
