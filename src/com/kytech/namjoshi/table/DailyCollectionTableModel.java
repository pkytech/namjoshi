/**
 * 
 */
package com.kytech.namjoshi.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.kytech.namjoshi.bo.DailyCollection;
import com.kytech.namjoshi.util.DBUtil;

/**
 * @author tphadke
 *
 */
public class DailyCollectionTableModel extends AbstractTableModel {

	private List<DailyCollection> rows = new ArrayList<DailyCollection>();
	private static final String[] columnNames = new String[]{"Patient Code", "First Name", "Last Name", "Fee Code", "Previous Balance", "Amount Payable", "Outstanding"};
	private double data = 10;
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return rows.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = "";
		DailyCollection coll = rows.get(rowIndex);
		switch(columnIndex) {
			case 0:
				value = String.valueOf(coll.getPatientCode());
				break;
			case 1:
				value = coll.getFirstName();
				break;
			case 2:
				value = coll.getLastName();
				break;
			case 3:
				value = coll.getFeeCode();
				break;
			case 4:
				value = String.valueOf(coll.getPreviousBal());
				break;
			case 5:
				value = String.valueOf(coll.getAmountPayable());
				break;
			case 6:
				value = String.valueOf(coll.getOutstanding());
				break;
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == columnNames.length-1) {
			return true;
		} else {
			return super.isCellEditable(rowIndex, columnIndex);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == columnNames.length -1) {
			try {
				data = Double.valueOf(String.valueOf(aValue));
				DailyCollection coll = rows.get(rowIndex);
				if (coll != null) {
					coll.setOutstanding(data);
					DBUtil.updatePatientOutstandingAmount(coll.getPatientCode(), data);
				}
				fireTableCellUpdated(rowIndex, columnIndex);
				fireTableRowsUpdated(rowIndex, columnIndex);
				return;
			} catch(NumberFormatException ne) {
				//Do nothing
			}
		}
		super.setValueAt(aValue, rowIndex, columnIndex);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == columnNames.length - 1) {
			return Double.class;
		}
		return super.getColumnClass(columnIndex);
	}
	
	public void setTableData(List<DailyCollection> rows) {
		this.rows = rows;
	}
}
