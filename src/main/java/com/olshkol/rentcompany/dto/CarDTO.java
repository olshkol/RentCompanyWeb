package com.olshkol.rentcompany.dto;

import com.olshkol.rentcompany.documents.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarDTO {
    private String regNumber;
    private String color;
    private ModelDTO model;
    private State state;
    private boolean inUse;
}
