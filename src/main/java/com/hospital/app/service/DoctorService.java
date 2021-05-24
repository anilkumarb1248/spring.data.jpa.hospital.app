package com.hospital.app.service;

import java.util.List;

import com.hospital.app.model.DoctorDTO;

public interface DoctorService {

	public DoctorDTO addDoctor(DoctorDTO doctorDTO);
	
	public List<DoctorDTO> saveAllDoctors(List<DoctorDTO> doctorsList);
	
	public List<DoctorDTO> getAllDoctors();

	public boolean deleteDcotor(int id);

}
