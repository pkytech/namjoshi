package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.kytech.namjoshi.bo.Patient;
import com.kytech.namjoshi.manager.NamjoshiUIManager;
import com.kytech.namjoshi.table.PatientDetailsTableModel;
import com.kytech.namjoshi.util.Util;

public class PatientSearchPanel extends JPanel {
	private JTextField txtPatientCode;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTable searchRecordTable;
	private JButton btnExit = null;
	private JButton btnSelect = null;
	private JTextField txtMiddleName;
	private PatientDetailsTableModel patientSearchResult = new PatientDetailsTableModel();
	public PatientSearchPanel() {
		setForeground(Color.WHITE);
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(10, 10));
		
		JPanel searchTextPanel = new JPanel();
		searchTextPanel.setBackground(Color.BLUE);
		add(searchTextPanel, BorderLayout.NORTH);
		searchTextPanel.setLayout(new GridLayout(0, 2, 5, 5));
		
		JLabel lblCode = new JLabel("Code");
		lblCode.setForeground(Color.WHITE);
		lblCode.setFont(Util.getSystemFont());
		searchTextPanel.add(lblCode);
		
		txtPatientCode = new JTextField();
		searchTextPanel.add(txtPatientCode);
		txtPatientCode.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setForeground(Color.WHITE);
		lblFirstName.setFont(Util.getSystemFont());
		searchTextPanel.add(lblFirstName);
		
		txtFirstName = new JTextField();
		searchTextPanel.add(txtFirstName);
		txtFirstName.setColumns(10);
		txtFirstName.setFont(Util.getSystemFont());
		
		JLabel lblMiddleName = new JLabel("Middle Name");
		lblMiddleName.setForeground(Color.WHITE);
		lblMiddleName.setFont(Util.getSystemFont());
		searchTextPanel.add(lblMiddleName);
		
		txtMiddleName = new JTextField();
		searchTextPanel.add(txtMiddleName);
		txtMiddleName.setColumns(10);
		txtMiddleName.setFont(Util.getSystemFont());
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setForeground(Color.WHITE);
		lblLastName.setFont(Util.getSystemFont());
		searchTextPanel.add(lblLastName);
		
		txtLastName = new JTextField();
		searchTextPanel.add(txtLastName);
		txtLastName.setColumns(10);
		txtLastName.setFont(Util.getSystemFont());
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(Util.getSystemFont());
		searchTextPanel.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().searchPatient(txtFirstName.getText(), txtMiddleName.getText(), txtLastName.getText());
			}
		});
		
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(Util.getSystemFont());
		searchTextPanel.add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().resetSearchPanel();
			}
		});
		
		JPanel tablePanel = new JPanel();
		add(tablePanel, BorderLayout.CENTER);
		tablePanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		tablePanel.add(scrollPane);
		
		searchRecordTable = new JTable(patientSearchResult );
		searchRecordTable.setFont(Util.getSystemFont());
		searchRecordTable.setRowHeight(25);
		scrollPane.setViewportView(searchRecordTable);
		JTableHeader header = searchRecordTable.getTableHeader();
		header.setFont(Util.getSystemFont());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLUE);
		tablePanel.add(buttonPanel, BorderLayout.SOUTH);
		
		btnSelect = new JButton("Select");
		btnSelect.setFont(Util.getSystemFont());
		buttonPanel.add(btnSelect);
		
		btnExit = new JButton("Exit");
		btnExit.setFont(Util.getSystemFont());
		buttonPanel.add(btnExit);
	}

	public void addExitListner(ActionListener listner) {
		btnExit.addActionListener(listner);
	}
	
	public void addSelectListner(ActionListener listner) {
		btnSelect.addActionListener(listner);
	}

	public PatientDetailsTableModel getPatientSearchResultModel() {
		return this.patientSearchResult;
	}

	public void repaintTable() {
		this.searchRecordTable.repaint();
	}
	
	public void setPatientCode(String text) {
		this.txtPatientCode.setText(text);
	}
	
	public String getPatientCode() {
		return this.txtPatientCode.getText();
	}
	
	public void setFirstName(String text) {
		this.txtFirstName.setText(text);
	}
	
	public String getFirstName() {
		return this.txtFirstName.getText();
	}
	
	public void setMiddleName(String text) {
		this.txtMiddleName.setText(text);
	}
	
	public String getMiddleName() {
		return this.txtMiddleName.getText();
	}
	
	public void setLastName(String text) {
		this.txtLastName.setText(text);
	}
	
	public String getLastName() {
		return this.txtLastName.getText();
	}
	
	public long getSelectedRecord() {
		int rowIndex = this.searchRecordTable.getSelectedRow();
		if (rowIndex <= -1) {
			return -1;
		}
		long patientId = patientSearchResult.getPatients().get(rowIndex).getPid();
		return patientId;
	}
}
