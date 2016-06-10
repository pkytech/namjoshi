package com.kytech.namjoshi.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.kytech.namjoshi.bo.Patient;

@SuppressWarnings("serial")
public class PatientDetailsTableModel extends AbstractTableModel {
	private static final String[] columnNames = new String[]{"Code", "First Name", "Middle Name", "Last Name"};
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
}
