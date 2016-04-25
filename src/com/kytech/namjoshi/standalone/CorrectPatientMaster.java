/**
 * 
 */
package com.kytech.namjoshi.standalone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kytech.namjoshi.bo.Patient;
import com.kytech.namjoshi.util.DBUtil;
import com.kytech.namjoshi.util.NamjoshiConfigurator;

/**
 * @author tphadke
 *
 */
public class CorrectPatientMaster {
	private static Map<Long, List<Patient>> patients = new HashMap<Long, List<Patient>>();
	private static final String SELECT_DUPLICATE = "select * from Patient_Master where Pid in (select Pid from Patient_Master group by pid having count(pid)>1) order by PID";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NamjoshiConfigurator config = NamjoshiConfigurator.getInstance(); 
			String driverName = config.getKeyValue(NamjoshiConfigurator.JDBC_CLASSNAME);
			String jdbcUrl = config.getKeyValue(NamjoshiConfigurator.JDBC_DB_URL);
			String userName = config.getKeyValue(NamjoshiConfigurator.DB_USER_NAME);
			String password = config.getKeyValue(NamjoshiConfigurator.DB_PASSWORD);
			
			Class.forName(driverName);
			try (Connection con = DriverManager.getConnection(jdbcUrl, userName, password)) {
				System.out.println("Connection DONE");
				try (Statement stmt = con.createStatement()) {
					try (ResultSet rs = stmt.executeQuery(SELECT_DUPLICATE)) {
						while (rs.next()) {
							Patient patient = DBUtil.loadPatient(rs);
							long pid = patient.getPid();
							if (patients.containsKey(pid)) {
								patients.get(pid).add(patient);
							} else {
								List<Patient> list = new ArrayList<Patient>();
								list.add(patient);
								patients.put(pid, list);
							}
						}
					}
				}
			
				//Iterate through Duplicate set
				int updated = 0;
				for (Map.Entry<Long, List<Patient>> entry : patients.entrySet()) {
					List<Patient> patients = entry.getValue();
					Patient nonEmpty = findNonEmptyPatient(patients);
					if (nonEmpty == null) {
						nonEmpty = patients.get(0);
					}
					deletePatientEntry(con, nonEmpty.getPid());
					enterFreshRecord(con, nonEmpty);
					updated++;
				}
				System.out.println("Corrected patients records: " + updated);
			}
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}

	}

	private static final String INSERT_PATIENT_RECORD = "insert into Patient_Master (Pid, PFName, PMName, PLName, Address, City, Pin, TPhone, MPhone, DrReference, email_id, BirthDate, Age, active, Pre_Bal) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static void enterFreshRecord(Connection con, Patient nonEmpty) throws SQLException {
		try (PreparedStatement pstmt = con.prepareStatement(INSERT_PATIENT_RECORD)) {
			pstmt.setLong(1, nonEmpty.getPid());
			pstmt.setString(2, nonEmpty.getFirstName());
			pstmt.setString(3, nonEmpty.getMiddleName());
			pstmt.setString(4, nonEmpty.getLastName());
			pstmt.setString(5, nonEmpty.getAddress());
			pstmt.setString(6, nonEmpty.getCity());
			pstmt.setString(7, nonEmpty.getPin());
			pstmt.setString(8, nonEmpty.getTelephoneNumber());
			pstmt.setString(9, nonEmpty.getMobileNumber());
			pstmt.setString(10, nonEmpty.getDrReference());
			pstmt.setString(11, nonEmpty.getEmail());
			pstmt.setString(12, nonEmpty.getBirthday());
			pstmt.setInt(13, nonEmpty.getAge());
			pstmt.setString(14, nonEmpty.getActive());
			pstmt.setDouble(15, nonEmpty.getPreviousBalance());
			pstmt.execute();
		}
	}

	private static void deletePatientEntry(Connection con, long pid) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			int records = stmt.executeUpdate("delete from Patient_Master where Pid="+pid);
			System.out.println("Records deleted: ["+pid+"]: " +records);
		}
	}

	private static Patient findNonEmptyPatient(List<Patient> patients) {
		for (Patient patient : patients) {
			if (patient.getFirstName() != null || patient.getMiddleName() != null
					||patient.getLastName() != null ||
					patient.getAddress() != null ||
					patient.getBirthday() != null ||
					patient.getMobileNumber() != null ||
					patient.getTelephoneNumber() != null) {
				return patient;
			}
		}
		return null;
	}

}
