package com.olshkol.rentcompany.repository;

import com.olshkol.rentcompany.documents.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarRepository extends MongoRepository<Car, String> {
}
