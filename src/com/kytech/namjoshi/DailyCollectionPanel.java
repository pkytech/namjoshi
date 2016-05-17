/**
 * 
 */
package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

import com.kytech.namjoshi.bo.DailyCollection;
import com.kytech.namjoshi.manager.NamjoshiUIManager;
import com.kytech.namjoshi.table.DailyCollectionTableModel;
import com.kytech.namjoshi.util.DBUtil;
import com.kytech.namjoshi.util.Util;
import com.toedter.calendar.JDateChooser;

/**
 * @author tphadke
 *
 */
public class DailyCollectionPanel extends JPanel {
	private JTable reportTable;
	JComboBox<String> timeFrame;
	JDateChooser dateChooser;
	private DailyCollectionTableModel tableModel;
	private JButton btnExit;
	public DailyCollectionPanel(int reportType) {
		tableModel = new DailyCollectionTableModel(reportType);
		setForeground(Color.WHITE);
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(10, 10));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setForeground(Color.WHITE);
		buttonPanel.setBackground(Color.BLUE);
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(0, 5, 8, 8));
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(Util.getSystemFont());
		buttonPanel.add(lblDate);

		dateChooser = new JDateChooser(new Date(), "dd/MM/yyyy");
		dateChooser.setFont(Util.getSystemFont());
		dateChooser.setSize(100, 25);
		buttonPanel.add(dateChooser);
		
		timeFrame = new JComboBox<String>();
		timeFrame.setFont(Util.getSystemFont());
		timeFrame.setModel(new DefaultComboBoxModel<String>(new String[] {"---- Select ----", "Morning", "Evening", "Both"}));
		timeFrame.setSelectedIndex(1);
		buttonPanel.add(timeFrame);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(Util.getSystemFont());
		buttonPanel.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Date date = dateChooser.getDate();
				int time = timeFrame.getSelectedIndex();
				if (time == 0) {
					NamjoshiUIManager.getUIManager().showErrorMessage("Select proper time frame.");
					return;
				}
				List<DailyCollection> rows = DBUtil.selectCollection(Util.formatDate(date), time);
				tableModel.setTableData(rows);
				tableModel.fireTableDataChanged();
			}
		});
		
		btnExit = new JButton("Exit");
		btnExit.setFont(Util.getSystemFont());
		buttonPanel.add(btnExit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);
		
		//Set Current Data
		if (reportType == DailyCollectionTableModel.COLLECTION_TYPE) {
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.set(Calendar.HOUR, 3);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.AM_PM, Calendar.PM);
			int type = -1;
			Date mid = cal.getTime();
			if (mid.compareTo(now) < 0) {
				type = DBUtil.EVENING;
			} else {
				type = DBUtil.MORNING;
			}
			timeFrame.setSelectedIndex(type);
			List<DailyCollection> rows = DBUtil.selectCollection(Util.formatDate(now), type);
			if (rows != null) {
				tableModel.setTableData(rows);
			}
		} else if (reportType == DailyCollectionTableModel.DUES_TYPES) {
			List<DailyCollection> rows = DBUtil.selectPatientDues();
			if (rows != null) {
				tableModel.setTableData(rows);
			}
		}

		reportTable = new JTable(tableModel);
		JTextField numberTextField = new JTextField();
		numberTextField.setHorizontalAlignment(JTextField.RIGHT);
		numberTextField.setFont(Util.getSystemFont());
		TableCellEditor editor = new DefaultCellEditor(numberTextField);
		reportTable.setDefaultEditor(Number.class, editor);
		reportTable.setBackground(Color.WHITE);
		reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		reportTable.setFillsViewportHeight(true);
		reportTable.setFont(Util.getSystemFont());
		reportTable.setRowHeight(25);
		
		JTableHeader header = reportTable.getTableHeader();
		header.setFont(Util.getSystemFont());
		scrollPane.setViewportView(reportTable);
		
		if (reportType == DailyCollectionTableModel.DUES_TYPES) {
			btnSearch.setEnabled(false);
			timeFrame.setEnabled(false);
			dateChooser.setEnabled(false);
		}
	}

	public void addExitListener(ActionListener ae) {
		btnExit.addActionListener(ae);
	}
}
