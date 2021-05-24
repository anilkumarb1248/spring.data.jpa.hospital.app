package com.hospital.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.app.entity.PatientAddress;

@Repository
public interface AddressRepository extends JpaRepository<PatientAddress, Integer> {

	Optional<PatientAddress> findByPatientId(int patientId);

	void deleteByPatientId(int patientId);

}
