package com.hospital.app.service;

import com.hospital.app.model.PatientDTO;

public interface PatientService {
	
	public PatientDTO addPatient(PatientDTO patientDTO) throws Exception;
	
	public PatientDTO getPatient(int id);
	
	public boolean deletePatient(int id);

}
