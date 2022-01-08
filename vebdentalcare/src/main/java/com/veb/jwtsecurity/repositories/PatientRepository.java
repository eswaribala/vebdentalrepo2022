package com.veb.jwtsecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veb.jwtsecurity.models.Patient;
import com.veb.jwtsecurity.models.User;

public interface PatientRepository extends JpaRepository<Patient,Long>{

}
