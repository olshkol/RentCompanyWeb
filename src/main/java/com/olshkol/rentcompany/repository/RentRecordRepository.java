package com.olshkol.rentcompany.repository;

import com.olshkol.rentcompany.documents.RentRecord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentRecordRepository extends MongoRepository<RentRecord, ObjectId> {
    List<RentRecord> findAllByRentDateBetween(LocalDate from, LocalDate to);
}
