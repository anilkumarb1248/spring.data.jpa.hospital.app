package com.hospital.app.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.app.entity.Appointment;
import com.hospital.app.entity.Patient;
import com.hospital.app.entity.PatientAddress;
import com.hospital.app.model.AppointmentDTO;
import com.hospital.app.model.PatientAddressDTO;
import com.hospital.app.model.PatientDTO;
import com.hospital.app.repository.AddressRepository;
import com.hospital.app.repository.AppointmentRepository;
import com.hospital.app.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService {

	Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

	@Autowired
	PatientRepository patientRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	AppointmentRepository appointmentRepository;

@Transactional(rollbackFor = {Exception.class})
@Override
public PatientDTO addPatient(PatientDTO patientDTO) throws Exception{

		PatientDTO addedPatientDTO = new PatientDTO();

		try {
			Patient patientEntity = new Patient();
			PatientAddress patientAddressEntity = new PatientAddress();
			Appointment appointmentEntity = new Appointment();

			BeanUtils.copyProperties(patientDTO, patientEntity);
			BeanUtils.copyProperties(patientDTO.getAddressDTO(), patientAddressEntity);
			BeanUtils.copyProperties(patientDTO.getAppointmentDTO(), appointmentEntity);

			// Adding Patient details
			Patient patient = patientRepository.save(patientEntity);
			LOGGER.info("Patient details are saved succesfully");

			// Adding Patient address details
			patientAddressEntity.setPatientId(patient.getId());
			PatientAddress patientAddress = addressRepository.save(patientAddressEntity);
			LOGGER.info("Address details are saved succesfully");

			// Adding Appointment details
			appointmentEntity.setPatientId(patient.getId());
//			int i = 10 / 0; // Creating an exception manually
			Appointment appointment = appointmentRepository.save(appointmentEntity);
			LOGGER.info("Appointment details are saved succesfully");

			// Converting the Entity objects to DTO objects
			PatientAddressDTO patientAddressDTO = new PatientAddressDTO();
			AppointmentDTO appointmentDTO = new AppointmentDTO();

			BeanUtils.copyProperties(patient, addedPatientDTO);
			BeanUtils.copyProperties(patientAddress, patientAddressDTO);
			BeanUtils.copyProperties(appointment, appointmentDTO);

			addedPatientDTO.setAddressDTO(patientAddressDTO);
			addedPatientDTO.setAppointmentDTO(appointmentDTO);

		} catch (Exception e) {
			LOGGER.info("Exception occurred while adding the Patient {}", e.getMessage());
			throw new Exception("Error Occurred");
		}

		return addedPatientDTO;
	}

	@Override
	public PatientDTO getPatient(int id) {
		PatientDTO patientDTO = new PatientDTO();

		Optional<Patient> patientOtional = patientRepository.findById(id);

		if (patientOtional.isPresent()) {

			Patient patient = patientOtional.get();
			BeanUtils.copyProperties(patient, patientDTO);

			Optional<PatientAddress> patientAddressOptional = addressRepository.findByPatientId(id);
			Optional<Appointment> appointmentOptional = appointmentRepository.findByPatientId(id);

			if (patientAddressOptional.isPresent()) {
				PatientAddress patientAddress = patientAddressOptional.get();
				PatientAddressDTO patientAddressDTO = new PatientAddressDTO();
				BeanUtils.copyProperties(patientAddress, patientAddressDTO);
				patientDTO.setAddressDTO(patientAddressDTO);
			}

			if (appointmentOptional.isPresent()) {
				Appointment appointment = appointmentOptional.get();
				AppointmentDTO appointmentDTO = new AppointmentDTO();
				BeanUtils.copyProperties(appointment, appointmentDTO);
				patientDTO.setAppointmentDTO(appointmentDTO);
			}
		} else {
			throw new RuntimeException("Patient not found with id: " + id);
		}
		return patientDTO;
	}

	@Override
	public boolean deletePatient(int id) {
		try {
			addressRepository.deleteByPatientId(id);
			appointmentRepository.deleteByPatientId(id);
			patientRepository.deleteById(id);

			LOGGER.info("Patient details are deleted successfully");
		} catch (Exception e) {
			LOGGER.info("Failed delete patient details with id: {} error: {}", id, e.getMessage());
			return false;
		}

		return true;

	}

}
