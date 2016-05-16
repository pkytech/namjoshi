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
	private static final String[] COLLECTION_COLUMN_NAMES = new String[]{"Patient Code", "First Name", "Last Name", "Fee Code", "Previous Balance", "Amount Payable", "Outstanding"};
	private static final String[] DUES_COLUMN_NAME = new String[]{"Patient Code", "First Name", "Last Name", "Outstanding"};
	private String columnNames[];
	public static final int COLLECTION_TYPE = 0;
	public static final int DUES_TYPES = 1;
	private int type = COLLECTION_TYPE;
	private double data = 10;
	
	public DailyCollectionTableModel() {
		columnNames = COLLECTION_COLUMN_NAMES;
	}

	public DailyCollectionTableModel(int type) {
		if (type == DUES_TYPES) {
			columnNames = DUES_COLUMN_NAME;
			this.type = DUES_TYPES; 
		} else {
			columnNames = COLLECTION_COLUMN_NAMES;
		}
	}
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
		Object value = null;
		DailyCollection coll = rows.get(rowIndex);
		if (type == COLLECTION_TYPE) {
			value = getCollectionValueAt(columnIndex, coll); 
		} else if (type == DUES_TYPES) {
			value = getDuesValueAt(columnIndex, coll);
		}
		return value;
	}

	private Object getDuesValueAt(int columnIndex, DailyCollection coll) {
		Object value = null;
		switch (columnIndex) {
		case 0:
			value = coll.getFirstName();
			break;
		case 1:
			value = coll.getLastName();
			break;
		case 2:
			value = coll.getOutstanding();
			break;
		}
		return value;
	}

	private Object getCollectionValueAt(int columnIndex,
			DailyCollection coll) {
		Object value = null;
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
				value = coll.getPreviousBal();
				break;
			case 5:
				value = coll.getAmountPayable();
				break;
			case 6:
				value = coll.getOutstanding();
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
