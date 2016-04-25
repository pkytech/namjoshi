package com.kytech.namjoshi.table;

import javax.swing.table.AbstractTableModel;

public class PatientDetailsTableModel extends AbstractTableModel {
	private static final String[] columnNames = new String[]{"Code", "First Name", "Middle Name", "Last Name"};
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return "";
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
}
