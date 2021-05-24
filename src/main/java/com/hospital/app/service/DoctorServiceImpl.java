package com.hospital.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.app.entity.Doctor;
import com.hospital.app.model.DoctorDTO;
import com.hospital.app.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	DoctorRepository doctorRepository;

	@Override
	public DoctorDTO addDoctor(DoctorDTO doctorDTO) {

		boolean flag = doctorRepository.existsByName(doctorDTO.getName());
		if (flag) {
			throw new RuntimeException("The name is already exist: " + doctorDTO.getName());
		}

		Doctor doctorEntity = new Doctor();
		BeanUtils.copyProperties(doctorDTO, doctorEntity);
		Doctor doctor = doctorRepository.save(doctorEntity);

		BeanUtils.copyProperties(doctor, doctorDTO);

		return doctorDTO;
	}

	@Override
	public List<DoctorDTO> saveAllDoctors(List<DoctorDTO> doctorsList) {

		List<Doctor> doctorsEntityList = new ArrayList<>();

		doctorsList.forEach(doctorDTO -> {
			if (!doctorRepository.existsByName(doctorDTO.getName())) {
				Doctor doctorEntity = new Doctor();
				BeanUtils.copyProperties(doctorDTO, doctorEntity);
				doctorsEntityList.add(doctorEntity);
			}
		});

		List<Doctor> doctorsEntities = doctorRepository.saveAll(doctorsEntityList);

		List<DoctorDTO> doctorsDTOList = new ArrayList<>();

		doctorsEntities.forEach(entity -> {
			DoctorDTO dto = new DoctorDTO();
			BeanUtils.copyProperties(entity, dto);
			doctorsDTOList.add(dto);
		});

		return doctorsDTOList;
	}

	@Override
	public List<DoctorDTO> getAllDoctors() {
		List<Doctor> doctorsEntities = doctorRepository.findAll();

		List<DoctorDTO> doctorsDTOList = new ArrayList<>();

		doctorsEntities.forEach(entity -> {
			DoctorDTO dto = new DoctorDTO();
			BeanUtils.copyProperties(entity, dto);
			doctorsDTOList.add(dto);
		});
		return doctorsDTOList;
	}

	@Override
	public boolean deleteDcotor(int id) {
		boolean flag = doctorRepository.existsById(id);
		if (!flag) {
			throw new RuntimeException("Docotor is not available with id : " + id);
		}
		doctorRepository.deleteById(id);
		return true;
	}

}
