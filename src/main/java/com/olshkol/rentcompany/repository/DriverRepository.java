package com.olshkol.rentcompany.repository;

import com.olshkol.rentcompany.documents.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverRepository extends MongoRepository<Driver, Long> {
}
