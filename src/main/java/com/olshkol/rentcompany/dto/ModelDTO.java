package com.olshkol.rentcompany.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModelDTO {
    private String modelName;
    private int gasTank; // in liters
    private String company;
    private String country;
    private int priceDay; // price per rent day no delay
}
