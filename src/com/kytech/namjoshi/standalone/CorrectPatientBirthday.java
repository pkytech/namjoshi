package com.kytech.namjoshi.standalone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kytech.namjoshi.util.DBUtil;

public class CorrectPatientBirthday {

	private static final String SELECT_PATIENT = "select Pid, BirthDate from Patient_Master";
	private static final String UPDATE_PATIENT = "update Patient_Master set birth_date=? where Pid=?";
	public static void main(String[] args) {
		PatientData data = null;
		List<PatientData> dataSet = new ArrayList<PatientData>();
		try (Connection con = DBUtil.getConnection(DBUtil.OLTP_POOL_NAME)) {
			System.out.println("Connection DONE");
			try (Statement stmt = con.createStatement()) {
				try (ResultSet rs = stmt.executeQuery(SELECT_PATIENT)) {
					while (rs.next()) {
						data = new PatientData();
						data.patientId = rs.getLong("Pid");
						data.birthDate = rs.getString("BirthDate");
						dataSet.add(data);
					}
				}
			}
			System.out.println("Data fetch and updating patient master");
			convertAndPersistBirthDate(con, dataSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void convertAndPersistBirthDate(Connection con,
			List<PatientData> dataSet) throws SQLException {
		SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sf2 = new SimpleDateFormat("MMM dd yyyy");
		int count = 0;
		try (PreparedStatement stmt = con.prepareStatement(UPDATE_PATIENT)) {
			for (PatientData data : dataSet) {
				String date = data.birthDate;
				if (date == null || date.trim().length() == 0) {
					continue;
				}
				Date dob = null;
				
				//date = date.substring(0, date.indexOf(" "));
				try {
					dob = sf.parse(date);
				} catch (ParseException e) {
					try {
						dob = sf2.parse(date);
					} catch (ParseException e1) {
						System.out.println("Cannot convert to date["+data.patientId+"]: "+data.birthDate);
						continue;
					}
				}

				stmt.setDate(1, dob != null ? new java.sql.Date(dob.getTime()) : null);
				stmt.setLong(2, data.patientId);
				try {
					stmt.executeUpdate();
				} catch(SQLException e) {
					System.out.println("Failed to execute statement["+data.patientId+"]: " + dob);
					//throw e;
				}
				stmt.clearParameters();
				count++;
			}
		}
		System.out.println("Updated patient master: " + count);
	}

	private static class PatientData {
		long patientId = -1;
		String birthDate = null;
	}
}
