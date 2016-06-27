package com.kytech.migrate.dbf;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.knaw.dans.common.dbflib.DbfLibException;
import nl.knaw.dans.common.dbflib.Field;
import nl.knaw.dans.common.dbflib.IfNonExistent;
import nl.knaw.dans.common.dbflib.Record;
import nl.knaw.dans.common.dbflib.Table;

import com.kytech.namjoshi.util.DBUtil;

public class MigrateDbf {
	private static final String INSERT_PATIENT = "insert into Patient_Master (Pid, PFName, PMName, PLName, Address, TPhone, DrReference, active, birth_date) values (?, ?, ?, ?, ?, ?, ?, 'Y', ?)";
	private static final String INSERT_PRESCRIPTION = "insert into Prescription_Transaction (Pre_id, P_id, sym_nm, Med_nm, Ex_date, Fee_Code) values (?, ?, ?, ?, ?, ?)";
	
	
	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.out.println("Please provide root folder where DBF fiels are located as parameter\nMigrateDbf <Directory>");
			return;
		}
		String rootFolder = args[0];
		String patientFile = rootFolder + "PATIENT.DBF";
		String prescriptionFile = rootFolder + "HISTCUM.DBF";
        final Table patientMasterTable = new Table(new File(patientFile));
        final Table prescriptionTable = new Table(new File(prescriptionFile));
        try {
        	try (Connection con = DBUtil.getConnection(DBUtil.OLTP_POOL_NAME)) {
        		System.out.println("Connection DONE");
        		con.setAutoCommit(false);
        		long time = System.currentTimeMillis();
        		long overall = time;
				migratePatientMaster(con, patientMasterTable);
				System.out.println("Time required for master: " + (System.currentTimeMillis()-time));
	        	time = System.currentTimeMillis();
	        	migratePrescription(con, prescriptionTable);
	        	System.out.println("Time required for prescription: " + (System.currentTimeMillis()-time));
	        	System.out.println("Time required for overall migration: " + (System.currentTimeMillis()-overall));
				con.commit();
        	} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void migratePrescription(Connection con, Table prescriptionTable) throws IOException, DbfLibException, SQLException, ParseException {
		prescriptionTable.open(IfNonExistent.ERROR);
		try {
			final List<Field> fields = prescriptionTable.getFields();
			final Iterator<Record> recordIterator = prescriptionTable.recordIterator();
			Preceription preceription = null;
			long count = 0, batch = 0;
			try (PreparedStatement stmt = con.prepareStatement(INSERT_PRESCRIPTION)) {
				while (recordIterator.hasNext()) {
					final Record record = recordIterator.next();
					preceription = new Preceription();
		            for (final Field field: fields) {
		            	String columnName = field.getName();
						
						switch (columnName) {
						case "CODE":
							preceription.code = getStringValue(field, record);
							break;
						case "SYMP1":
							preceription.symp1 = getStringValue(field, record);
							break;
						case "SYMP2":
							preceription.symp2 = getStringValue(field, record);
							break;
						case "SYMP3":
							preceription.symp3 = getStringValue(field, record);
							break;
						case "PRESC1":
							preceription.prescription1 = getStringValue(field, record);
							break;
						case "PRESC2":
							preceription.prescription2 = getStringValue(field, record);
							break;
						case "PRESC3":
							preceription.prescription3 = getStringValue(field, record);
							break;
						case "EX_DATE":
							preceription.examDate = record.getDateValue(columnName);
							break;
						case "FEE_COD":
							preceription.feeCode = getStringValue(field, record);
							break;
						}
		            }
		            if (!(preceription.symp1 == null && preceription.prescription1 == null)) {
		            	pushPrescriptionToDatabase(con, stmt, preceription, count);
		            	count++;
		            	batch++;
		            	if (batch == 100) {
		            		stmt.executeBatch();
		            		stmt.clearBatch();
		            		batch = 0;
		            	}
		            }
				}
				if (batch > 0) {
            		stmt.executeBatch();
            		stmt.clearBatch();
            		batch = 0;
            	}
				System.out.println("Migrated prescription data: " + count);
			}
		} finally {
			try {
				prescriptionTable.close();
			} catch (IOException ex) {
				System.out.println("Unable to close the table");
			}
		}
		
	}

	private static void pushPrescriptionToDatabase(Connection con, PreparedStatement stmt, Preceription preceription, long count) throws SQLException, ParseException {
		//System.out.println(preceription);
		stmt.setLong(1, count+1);
		stmt.setLong(2, Long.parseLong(preceription.code));
		stmt.setString(3, concat(preceription.symp1, preceription.symp2, preceription.symp3));
		stmt.setString(4, concat(preceription.prescription1, preceription.prescription2, preceription.prescription3));
		if (preceription.examDate != null) {
			Date exDate = getCorrectedDate(preceription.examDate);
			stmt.setDate(5, new java.sql.Date(exDate.getTime()));
		} else {
			stmt.setDate(5, null);
		}
		stmt.setString(6, preceription.feeCode);
		stmt.addBatch();
		stmt.clearParameters();
	}

	private static String concat(String... values) {
		StringBuffer sbOut = new StringBuffer();
		for (String value : values) {
			sbOut.append(value != null ? value.trim() : "").append("\n");
		}
		String finalValue = sbOut.toString().trim(); 
		return finalValue.equals("") ? null : finalValue;
	}
	
	private static void migratePatientMaster(Connection con, Table patientMasterTable) throws IOException, DbfLibException, SQLException, ParseException {
		patientMasterTable.open(IfNonExistent.ERROR);
		try {
			final List<Field> fields = patientMasterTable.getFields();
			final List<Patient> patients = new ArrayList<Patient>();
			final Iterator<Record> recordIterator = patientMasterTable.recordIterator();
			Patient patient = null;
			while (recordIterator.hasNext()) {
				final Record record = recordIterator.next();
				patient = new Patient(); 
				for (Field field: fields) {
					String columnName = field.getName();
					
					switch (columnName) {
					case "CODE":
						patient.code = getStringValue(field, record);
						break;
					case "FIRST_NAME":
						patient.firstName = getStringValue(field, record);
						break;
					case "SECOD_NAME":
						patient.middleName = getStringValue(field, record);
						break;
					case "SURNAME":
						patient.lastName = getStringValue(field, record);
						break;
					case "ADDRESS1":
						patient.address1 = getStringValue(field, record);
						break;
					case "ADDRESS2":
						patient.address2 = getStringValue(field, record);
						break;
					case "TELEPHONE":
						patient.telephone = getStringValue(field, record);
						break;
					case "REFERENCE":
						patient.reference = getStringValue(field, record);
						break;
					case "BIRTH_DATE":
						patient.birthDate = record.getDateValue(columnName);
						break;
					}
				}
				if (CORRECT_RECORD.contains(patient.code)) {
					patient.birthDate = getCorrectedDate(patient.birthDate);
				}
				patients.add(patient);
			}
			//System.out.println(patients);
			int batch = 0, count = 0;
			try (PreparedStatement stmt = con.prepareStatement(INSERT_PATIENT)) {
				for (Patient pat : patients) {
					
					if ((pat.firstName == null || pat.firstName.trim().length()==0) && (pat.lastName == null || pat.lastName.trim().length()==0)) {
						continue;
					}
					if (INSERTED_RECORDS.contains(Long.parseLong(pat.code))) {
						continue;
					}
					//System.out.println(pat);
					stmt.setLong(1, Long.parseLong(pat.code));
					stmt.setString(2, pat.firstName);
					stmt.setString(3, pat.middleName);
					stmt.setString(4, pat.lastName);
					String address = pat.address1 != null ? pat.address1 : "";
					address+= " " + pat.address2 != null ? pat.address2 : "";
					stmt.setString(5, address.trim().length() == 0 ? null : address);
					stmt.setString(6, pat.telephone);
					stmt.setString(7, pat.reference);
					stmt.setDate(8, pat.birthDate != null ? new java.sql.Date(pat.birthDate.getTime()) : null);
					//stmt.executeUpdate();
					INSERTED_RECORDS.add(Long.parseLong(pat.code));
					stmt.addBatch();
					stmt.clearParameters();
					batch++;
					count++;
					if (batch == 100) {
						stmt.executeBatch();
						batch = 0;
					}
				}
				if (batch > 0) {
					stmt.executeBatch();
				}
			}
			System.out.println("Migrated Patients: " + count);
		} finally {
			try {
				patientMasterTable.close();
			} catch (IOException ex) {
				System.out.println("Unable to close the table");
			}
		}
	}

	private static String getStringValue(Field field, Record record) throws DbfLibException {
		byte data[] = record.getRawValue(field);
		String value = null;
		if (data != null && data.length > 0) {
			value = new String(data).trim();
		}
		return value;
	}
	private static final String correctRecord[] = new String[]{"00352", "00726", "01331", "02082", "02692", "02744", "04241",
		"04423", "04752", "04882", "05019", "05532", "06268", "06914", "07004", "07214", "07229", "07572", "08544"};
	private static final List<String> CORRECT_RECORD = Arrays.asList(correctRecord);
	private static final List<Long> INSERTED_RECORDS = new ArrayList<Long>();
	private static Date getCorrectedDate(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = sdf.format(date);
		dateStr = dateStr.replaceAll("1850", "1950");
		dateStr = dateStr.replaceAll("0945", "1945");
		dateStr = dateStr.replaceAll("1338", "1948");
		dateStr = dateStr.replaceAll("1665", "1965");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("0936", "1936");
		dateStr = dateStr.replaceAll("1204", "2004");
		dateStr = dateStr.replaceAll("1656", "1956");
		dateStr = dateStr.replaceAll("1170", "1970");
		dateStr = dateStr.replaceAll("1757", "1957");
		dateStr = dateStr.replaceAll("1337", "1937");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2090", "1990");
		dateStr = dateStr.replaceAll("2091", "1991");
		dateStr = dateStr.replaceAll("1059", "1959");
		dateStr = dateStr.replaceAll("0194", "1994");
		dateStr = dateStr.replaceAll("1892", "1982");
		dateStr = dateStr.replaceAll("0982", "1982");
		dateStr = dateStr.replaceAll("2975", "1975");
		/*dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");
		dateStr = dateStr.replaceAll("2977", "1977");*/
		//System.out.println(dateStr);
		return sdf.parse(dateStr);
	}
}
class Preceription {
	String code;
	String symp1;
	String symp2;
	String symp3;
	String prescription1;
	String prescription2;
	String prescription3;
	String feeCode;
	Date examDate;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Preceription [code=" + code + ", symp1=" + symp1 + ", symp2="
				+ symp2 + ", symp3=" + symp3 + ", prescription1="
				+ prescription1 + ", prescription2=" + prescription2
				+ ", prescription3=" + prescription3 + ", feeCode=" + feeCode
				+ ", examDate=" + examDate + "]";
	}
}
class Patient {
	String code;
	String firstName;
	String middleName;
	String lastName;
	String address1;
	String address2;
	String telephone;
	String reference;
	Date birthDate;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Patient [code=" + code + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName
				+ ", address1=" + address1 + ", address2=" + address2
				+ ", telephone=" + telephone + ", reference=" + reference
				+ ", birthDate=" + birthDate + "]";
	}
	
}
