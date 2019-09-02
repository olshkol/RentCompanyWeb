package com.olshkol.rentcompany.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "modelName")

@Document("model")
public class Model {
    @Id
    private String modelName;
    private int gasTank; // in liters
    private String company;
    private String country;
    private int priceDay; // price per rent day no delay
    @Field
    private boolean removed = false;

    List<String> carsId = new ArrayList<>();
}
