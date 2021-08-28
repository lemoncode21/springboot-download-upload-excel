package com.lemoncode21.springbootdownloadupload.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "brand_cars")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandCars {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type;
    private BigDecimal price;


}
