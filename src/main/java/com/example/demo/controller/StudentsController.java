package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.repository.Student_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/students")

public class StudentsController {

    @Autowired
    private Student_Repository student_repository;


    @PostMapping
    public Mono<ResponseEntity<Student>> create(@RequestBody Student student) {
        return student_repository.save(student)
              .map(savedStudent -> ResponseEntity.ok(savedStudent))
              .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Student> getStudents(){
        return student_repository.findAll();
    }

    @GetMapping("/{studentId}")
    public Mono<ResponseEntity<Student>> getStudentById(@PathVariable int studentId){
        return student_repository.findById(studentId)
              .map(student -> ResponseEntity.ok(student))
              .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{studentId}")
    public Mono updateStudent(@PathVariable int studentId, @RequestBody Student student){
        return student_repository.findById(studentId)
              .flatMap(selectedStudentFromDB ->{
                  selectedStudentFromDB.setName(student.getName());
                  selectedStudentFromDB.setAge(student.getAge());
                  selectedStudentFromDB.setUniversity(student.getUniversity());
                  selectedStudentFromDB.setGpa(student.getGpa());

                  return student_repository.save(selectedStudentFromDB);
              })
              .map(updatedStudent -> ResponseEntity.ok(updatedStudent))
              .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/student/{id}")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable(value = "id") int studentId) {

        return student_repository.findById(studentId)
              .flatMap(selectedStudentFromDB ->
                    student_repository.delete(selectedStudentFromDB)
                          .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
              )
              .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
