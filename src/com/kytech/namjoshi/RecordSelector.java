package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.kytech.namjoshi.manager.NamjoshiUIManager;
import com.kytech.namjoshi.util.Util;

public class RecordSelector extends JPanel {
	private JTextField txtPatientcodeselector;
	private JTextField txtPatientName;
	private JTextField txtDues;
	private JButton btnExit = null;
	private final PatientSearchPanel searchPanel;
	public RecordSelector() {
		setForeground(Color.WHITE);
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(0, 0));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.BLUE);
		add(buttonsPanel, BorderLayout.SOUTH);
		
		searchPanel = new PatientSearchPanel();
		final JButton btnSearch = new JButton("Search");
		btnSearch.setFont(Util.getSystemFont());
		buttonsPanel.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Window parentWindow = SwingUtilities.windowForComponent(btnSearch);
				final JDialog dialog = new JDialog(parentWindow);
				dialog.setLocationRelativeTo(btnSearch);
				dialog.setSize(800, 550);
				dialog.getContentPane().add(searchPanel);
				searchPanel.addExitListner(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.dispose();
					}
				});
				searchPanel.addSelectListner(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.setVisible(false);
						NamjoshiUIManager.getUIManager().loadSearchedPatient();
						NamjoshiUIManager.getUIManager().resetSearchPanel();
						dialog.dispose();
					}
				});
                dialog.setModal(true);
                dialog.setVisible(true);
			}
		});
		
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setFont(Util.getSystemFont());
		buttonsPanel.add(btnLoad);
		
		JButton btnNew = new JButton("New");
		btnNew.setFont(Util.getSystemFont());
		buttonsPanel.add(btnNew);
		btnNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().newPatient();
			}
		});
		
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(Util.getSystemFont());
		buttonsPanel.add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().clearPatientData();
				
			}
		});
		
		btnExit = new JButton("Exit");
		btnExit.setFont(Util.getSystemFont());
		buttonsPanel.add(btnExit);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLUE);
		add(panel, BorderLayout.CENTER);
		
		JLabel lblCode = new JLabel("Code");
		lblCode.setFont(Util.getSystemFont());
		lblCode.setForeground(Color.WHITE);
		panel.add(lblCode);
		
		txtPatientcodeselector = new JTextField();
		txtPatientcodeselector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = txtPatientcodeselector.getText();
				//NamjoshiUIManager.getUIManager().showInformationMessage("Enter clicked");
				try {
					Integer.parseInt(text);
				} catch (NumberFormatException nfe) {
					NamjoshiUIManager.getUIManager().showErrorMessage("Invalid Patient code");
					txtPatientcodeselector.setText("");
					return;
				}
				NamjoshiUIManager.getUIManager().loadPatient(text);
			}
		});
		
		txtPatientcodeselector.setFont(Util.getSystemFont());
		panel.add(txtPatientcodeselector);
		txtPatientcodeselector.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(Util.getSystemFont());
		lblName.setForeground(Color.WHITE);
		panel.add(lblName);
		
		txtPatientName = new JTextField();
		txtPatientName.setEditable(false);
		txtPatientName.setFont(Util.getSystemFont());
		panel.add(txtPatientName);
		txtPatientName.setColumns(20);
		
		JLabel lblDues = new JLabel("Dues");
		lblDues.setFont(Util.getSystemFont());
		lblDues.setForeground(Color.WHITE);
		panel.add(lblDues);
		
		txtDues = new JTextField();
		txtDues.setToolTipText("");
		txtDues.setForeground(Color.RED);
		txtDues.setEditable(false);
		txtDues.setFont(Util.getSystemFont());
		panel.add(txtDues);
		txtDues.setColumns(10);
	}
	
	public void addExitListner(ActionListener exitListner) {
		btnExit.addActionListener(exitListner);
	}

	public void setPatientName(String name) {
		txtPatientName.setText(name);
	}
	
	public void setPatientCode(String code) {
		txtPatientcodeselector.setText(code);
	}

	public String getPatientCode() {
		return txtPatientcodeselector.getText();
	}
	
	public void enablePatientCode() {
		txtPatientcodeselector.setEditable(true);
	}
	
	public void disablePatientCode() {
		txtPatientcodeselector.setEditable(false);
	}
	
	public void setOutstandingAmount(double amount) {
		txtDues.setText(amount == -1 ? "" : String.valueOf(amount));
	}

	public PatientSearchPanel getSearchPanel() {
		return this.searchPanel;
	}
}
