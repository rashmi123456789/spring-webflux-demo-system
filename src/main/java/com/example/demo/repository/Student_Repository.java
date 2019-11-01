package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Student_Repository extends ReactiveMongoRepository<Student,Integer> {

}
