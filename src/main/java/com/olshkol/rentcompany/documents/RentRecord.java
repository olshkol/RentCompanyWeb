package com.olshkol.rentcompany.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olshkol.rentcompany.dto.CarDTO;
import com.olshkol.rentcompany.dto.DriverDTO;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(exclude = {"car", "driver"})

@Document("rentrecord")
public class RentRecord {
    @Id
    private ObjectId contractNum;
    private Car car;
    private Driver driver;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rentDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    private int rentDays;
    private int damages;
    private int tankPercent;
    private double cost;

    public RentRecord(Car car, Driver driver, LocalDate rentDate, int rentDays) {
        this.car = car;
        this.driver = driver;
        this.rentDate = rentDate;
        this.rentDays = rentDays;
    }
}
