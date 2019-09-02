package com.olshkol.rentcompany.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverDTO {
    private long licenseId;
    private String name;
    private int birthYear;
    private String phone;
}
