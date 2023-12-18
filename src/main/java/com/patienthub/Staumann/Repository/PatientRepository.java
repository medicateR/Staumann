package com.patienthub.Staumann.Repository;

import com.patienthub.Staumann.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Integer> {
}
