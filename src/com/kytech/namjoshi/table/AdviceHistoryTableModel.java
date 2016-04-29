package com.kytech.namjoshi.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.kytech.namjoshi.bo.Prescription;
import com.kytech.namjoshi.util.Util;

public class AdviceHistoryTableModel extends AbstractTableModel {

	private List<Prescription> rows = new ArrayList<Prescription>();
	/**
	 * @return the rows
	 */
	public List<Prescription> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<Prescription> rows) {
		this.rows = rows;
	}

	private static final String[] columnNames = new String[]{"Date", "Symtoms", "Prescription", "Advice", "Fee"};
	@Override
	public int getRowCount() {
		return rows != null ? rows.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String cellData = "";
		if (rowIndex < rows.size()) {
			Prescription pre = rows.get(rowIndex);
			switch (columnIndex) {
				case 0:
					Date exDate = pre.getExaminationDate();
					cellData = Util.formatDate(exDate);
					break;
				case 1:
					cellData = pre.getSymtoms();
					break;
				case 2:
					cellData = pre.getPrescription();
					break;
				case 3:
					cellData = pre.getAdvice();
					break;
				case 4:
					cellData = pre.getFeeCode();
					break;
				default:
					break;
			}
		}
		return cellData;
	}

	

	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
	
	
}
