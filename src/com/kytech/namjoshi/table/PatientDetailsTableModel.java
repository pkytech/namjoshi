package com.kytech.namjoshi.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.kytech.namjoshi.bo.Patient;
import com.kytech.namjoshi.util.Util;

@SuppressWarnings("serial")
public class PatientDetailsTableModel extends AbstractTableModel {
	private static final String[] columnNames = new String[]{"Code", "First Name", "M. Name", "Last Name", "DOB"};
	private static final int[] columnWidths = new int[]{70, 250, 80, 250, 110};
	private List<Patient> patients = new ArrayList<Patient>(); 
	@Override
	public int getRowCount() {
		return patients !=  null ? patients.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (patients == null || patients.size() < rowIndex) {
			return "";
		}
		Patient patient = patients.get(rowIndex);
		String cellValue = "";
		switch (columnIndex) {
			case 0:
				cellValue = String.valueOf(patient.getPid());
				break;
			case 1:
				cellValue = patient.getFirstName();
				break;
			case 2:
				cellValue = patient.getMiddleName();
				break;
			case 3:
				cellValue = patient.getLastName();
				break;
			case 4:
				Date dob = patient.getBirthDate();
				if (dob != null) {
					cellValue = Util.formatDate(dob);
				} else {
					cellValue = "";
				}
				break;
			default:
				cellValue = "";
		}
		return cellValue;
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }

	/**
	 * @return the patients
	 */
	public List<Patient> getPatients() {
		return patients;
	}

	/**
	 * @param patients the patients to set
	 */
	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}
	
	public int[] getColumnWidths() {
		return columnWidths;
	}
}
