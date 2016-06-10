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

@SuppressWarnings("serial")
public class RecordSelector extends JPanel {
	private JTextField txtPatientcodeselector;
	private JTextField txtPatientName;
	private JTextField txtDues;
	private JButton btnExit = null;
	private final PatientSearchPanel searchPanel;
	private JTextField txtAge;
	public RecordSelector() {
		setForeground(Color.WHITE);
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(0, 0));
		
		populateSearchPanel();
		
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
				Window parentWindow = SwingUtilities.windowForComponent(txtPatientcodeselector);
				final JDialog dialog = new JDialog(parentWindow);
				dialog.setLocationRelativeTo(btnSearch);
				dialog.setLocation(100, 100);
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
		btnLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectPatient();
			}
		});
		
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
		

	}

	private void populateSearchPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLUE);
		add(panel, BorderLayout.CENTER);
		
		JLabel lblCode = new JLabel("Code");
		lblCode.setFont(Util.getSystemFont());
		lblCode.setForeground(Color.WHITE);
		panel.add(lblCode);
		
		txtPatientcodeselector = new JTextField();
		txtPatientcodeselector.requestFocus();
		txtPatientcodeselector.requestFocusInWindow();
		txtPatientcodeselector.requestFocus(true);
		txtPatientcodeselector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectPatient();
			}
		});
		
		txtPatientcodeselector.setFont(Util.getSystemFont());
		panel.add(txtPatientcodeselector);
		txtPatientcodeselector.setColumns(7);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(Util.getSystemFont());
		lblName.setForeground(Color.WHITE);
		panel.add(lblName);
		
		txtPatientName = new JTextField();
		txtPatientName.setEditable(false);
		txtPatientName.setFont(Util.getSystemFont());
		panel.add(txtPatientName);
		txtPatientName.setColumns(15);
		
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
		txtDues.setColumns(5);
		
		JLabel lblAge = new JLabel("Age");
		lblAge.setForeground(Color.WHITE);
		lblAge.setFont(Util.getSystemFont());
		panel.add(lblAge);
		
		txtAge = new JTextField();
		txtAge.setEditable(false);
		txtAge.setFont(Util.getSystemFont());
		panel.add(txtAge);
		txtAge.setColumns(4);
	}
	
	public void selectPatient() {
		String text = txtPatientcodeselector.getText();
		//NamjoshiUIManager.getUIManager().showInformationMessage("Enter clicked");
		try {
			Integer.parseInt(text);
		} catch (NumberFormatException nfe) {
			NamjoshiUIManager.getUIManager().showErrorMessage("Invalid Patient code");
			txtPatientcodeselector.setText("");
			txtPatientcodeselector.requestFocusInWindow();
			return;
		}
		NamjoshiUIManager.getUIManager().loadPatient(text);
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
	
	public void setPatientAge(String age) {
		txtAge.setText(age);
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
	
	public void focusPatientCode() {
		txtPatientcodeselector.requestFocusInWindow();
		txtPatientcodeselector.requestFocus();
	}
}
