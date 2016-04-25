package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import com.kytech.namjoshi.table.PatientDetailsTableModel;
import com.kytech.namjoshi.util.Util;

public class PatientSearchPanel extends JPanel {
	private JTextField textField;
	private JTextField txtFirstName;
	private JTextField txtLastname;
	private JTable searchRecordTable;
	private JButton btnExit = null;
	private JButton btnSelect = null;
	public PatientSearchPanel() {
		setForeground(Color.WHITE);
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(5, 5));
		
		JPanel searchTextPanel = new JPanel();
		searchTextPanel.setBackground(Color.BLUE);
		add(searchTextPanel, BorderLayout.NORTH);
		searchTextPanel.setLayout(new GridLayout(0, 2, 5, 5));
		
		JLabel lblCode = new JLabel("Code");
		lblCode.setForeground(Color.WHITE);
		lblCode.setFont(Util.getSystemFont());
		searchTextPanel.add(lblCode);
		
		textField = new JTextField();
		searchTextPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setForeground(Color.WHITE);
		lblFirstName.setFont(Util.getSystemFont());
		searchTextPanel.add(lblFirstName);
		
		txtFirstName = new JTextField();
		searchTextPanel.add(txtFirstName);
		txtFirstName.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setForeground(Color.WHITE);
		lblLastName.setFont(Util.getSystemFont());
		searchTextPanel.add(lblLastName);
		
		txtLastname = new JTextField();
		searchTextPanel.add(txtLastname);
		txtLastname.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(Util.getSystemFont());
		searchTextPanel.add(btnSearch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(Util.getSystemFont());
		searchTextPanel.add(btnClear);
		
		JPanel tablePanel = new JPanel();
		add(tablePanel, BorderLayout.CENTER);
		tablePanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		tablePanel.add(scrollPane);
		
		searchRecordTable = new JTable(new PatientDetailsTableModel());
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
}
