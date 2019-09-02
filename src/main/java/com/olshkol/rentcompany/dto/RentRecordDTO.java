package com.olshkol.rentcompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentRecordDTO {
    private ObjectId contractNum;
    private CarDTO car;
    private DriverDTO driver;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rentDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    private int rentDays;
    private int damages;
    private int tankPercent;
    private double cost;
}
