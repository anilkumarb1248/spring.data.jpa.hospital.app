package com.hospital.app;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hospital.app.model.AppointmentDTO;
import com.hospital.app.model.DoctorDTO;
import com.hospital.app.model.PatientAddressDTO;
import com.hospital.app.model.PatientDTO;
import com.hospital.app.service.DoctorService;
import com.hospital.app.service.PatientService;

@Component
public class ApplicationTest {

	Logger LOGGER = LoggerFactory.getLogger(ApplicationTest.class);

	@Autowired
	DoctorService doctorService;

	@Autowired
	PatientService patientService;

	private static List<DoctorDTO> availableDoctors;

	private Random radom = new Random();

	public void testApp() throws Exception {
		insertDoctorsData();
		getAvailableDoctors();
		testAddPatient();
	}

	public void insertDoctorsData() {
		List<DoctorDTO> doctorsDTOList = Arrays.asList(new DoctorDTO(100, "Anvi"), new DoctorDTO(200, "Sahanvika"),
				new DoctorDTO(300, "Pinkulu"), new DoctorDTO(400, "Jasvanth"), new DoctorDTO(500, "Jashmitha"));
		List<DoctorDTO> doctorsList = doctorService.saveAllDoctors(doctorsDTOList);

		LOGGER.info("Static doctors data inserted {}", doctorsList.size());
	}

	public void getAvailableDoctors() {
		availableDoctors = doctorService.getAllDoctors();
		LOGGER.info("Available doctors count {}", availableDoctors.size());
	}

	private void testAddPatient() throws Exception {
		PatientDTO patientDTO = new PatientDTO();
		PatientAddressDTO patientAddressDTO = new PatientAddressDTO();
		AppointmentDTO appointmentDTO = new AppointmentDTO();

		patientDTO.setName("Anil");

		patientAddressDTO.setCity("Hyderabad");
		patientAddressDTO.setState("Telangana");
		patientDTO.setAddressDTO(patientAddressDTO);

		int randomDoctorIndex = radom.nextInt(availableDoctors.size());
		int doctorId = availableDoctors.get(randomDoctorIndex).getId();

		appointmentDTO.setDoctorId(doctorId);
		appointmentDTO.setAppointmentDate(LocalDateTime.now().plusDays(2));
		patientDTO.setAppointmentDTO(appointmentDTO);

		PatientDTO addedPatientDTO = patientService.addPatient(patientDTO);

		LOGGER.info("The patient is inserted with id {}", addedPatientDTO.getId());

		getPatientDetails(addedPatientDTO.getId());

	}

	public void getPatientDetails(int patientId) {
		PatientDTO addedPatientDTO = patientService.getPatient(patientId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(addedPatientDTO.toString());
		}
		LOGGER.info("Appointment got successfully on {}", addedPatientDTO.getAppointmentDTO().getAppointmentDate());
	}

}
