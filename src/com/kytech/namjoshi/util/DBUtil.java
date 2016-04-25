package com.kytech.namjoshi.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kytech.namjoshi.bo.Patient;

public final class DBUtil {

	private DBUtil(){
	}

	public static Patient loadPatient(ResultSet rs) throws SQLException {
		Patient patient = new Patient();
		patient.setPid(rs.getLong("Pid"));
		patient.setFirstName(rs.getString("PFName"));
		patient.setMiddleName(rs.getString("PMName"));
		patient.setLastName(rs.getString("PLName"));
		patient.setAddress(rs.getString("Address"));
		patient.setCity(rs.getString("City"));
		patient.setPin(rs.getString("Pin"));
		patient.setTelephoneNumber(rs.getString("TPhone"));
		patient.setMobileNumber(rs.getString("MPhone"));
		patient.setDrReference(rs.getString("DrReference"));
		patient.setEmail(rs.getString("email_id"));
		patient.setBirthday(rs.getString("BirthDate"));
		patient.setActive(rs.getString("active"));
		patient.setAge(rs.getInt("Age"));
		patient.setPreviousBalance(rs.getDouble("Pre_Bal"));
		return patient;
	}
}
