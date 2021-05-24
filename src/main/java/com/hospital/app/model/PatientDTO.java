package com.hospital.app.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PatientDTO {
	
	private int id;
	private String name;
	private AppointmentDTO appointmentDTO;
	private PatientAddressDTO addressDTO;

}
