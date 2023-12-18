package com.patienthub.Staumann.Controller;

import com.patienthub.Staumann.Exceptions.ResourceNotFoundException;
import com.patienthub.Staumann.Model.Patient;
import com.patienthub.Staumann.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;


    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @GetMapping
    @Cacheable(cacheNames = "patients",key = "pid")
    public ResponseEntity<Patient> getPatientById(@PathVariable  int id){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id:" + id));
        return ResponseEntity.ok(patient);
    }

    @PutMapping("{id}")
    @CachePut(cacheNames = "patients",key = "pid")
    public ResponseEntity<Patient> updatePatient(@PathVariable int id,@RequestBody Patient patient) {
        Patient updatedPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("patient not exist with id: " + id));
        updatedPatient.setName(patient.getName());


       patientRepository.save(updatedPatient);

        return ResponseEntity.ok(updatedPatient);
    }


    @DeleteMapping("{id}")
    @CacheEvict(cacheNames = "patients",key = "pid")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable int id){

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not exist with id: " + id));

       patientRepository.delete(patient);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}


