package com.olshkol.rentcompany.documents;

import com.olshkol.rentcompany.dto.ModelDTO;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Configuration;
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
@EqualsAndHashCode(of = "regNumber")

@Document("car")
public class Car {
    @Id
    private String regNumber;
    private String color;
    private Model model;
    @Field
    private State state = State.EXCELLENT;
    @Field
    private boolean inUse = false;
    @Field
    private boolean removed = false;

    private List<ObjectId> rentRecordIds = new ArrayList<>();

    public Car(String regNumber, String color, Model model) {
        this(regNumber, color, model, State.EXCELLENT, false, false, new ArrayList<>());
    }
}

