package com.olshkol.rentcompany.repository;

import com.olshkol.rentcompany.documents.Model;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelRepository extends MongoRepository<Model, String> {
}
