package com.kytech.namjoshi.table;

import javax.swing.table.AbstractTableModel;

public class AdviceHistoryTableModel extends AbstractTableModel {

	private static final String[] columnNames = new String[]{"Date", "Symtoms", "Prescription", "Advice", "Fee"};
	@Override
	public int getRowCount() {
		//TODO Change row count
		return 1;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return "1234";
	}

	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
	
	
}
