package com.olshkol.rentcompany.documents;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "licenseId")

@Document("driver")
public class Driver {
    @Id
    private long licenseId;
    private String name;
    private int birthYear;
    private String phone;

    private List<ObjectId> rentRecordIds = new ArrayList<>();
}
