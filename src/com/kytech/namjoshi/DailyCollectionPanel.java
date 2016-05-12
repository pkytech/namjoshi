/**
 * 
 */
package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private DailyCollectionTableModel tableModel = new DailyCollectionTableModel();
	private JButton btnExit;
	public DailyCollectionPanel() {
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
				String date = dateChooser.getDateFormatString();
				int time = timeFrame.getSelectedIndex();
				if (time == 0) {
					NamjoshiUIManager.getUIManager().showErrorMessage("Select proper time frame.");
					return;
				}
				List<DailyCollection> rows = DBUtil.selectCollection(date, time);
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
	}

	public void addExitListener(ActionListener ae) {
		btnExit.addActionListener(ae);
	}
}
