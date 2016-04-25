package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.kytech.namjoshi.table.AdviceHistoryTableModel;
import com.kytech.namjoshi.util.Util;

public class DailyWork extends JPanel {
	private JTextField txtCode;
	private JTextField firstName;
	private JTextField txtMiddle;
	private JTextField txtLast;
	private JTextField txtAddress;
	private JTextField txtTelephone;
	private JTextField txtMobile;
	private JTextField txtDob;
	private JTextField txtAge;
	private JTextField txtReference;
	private JTable historyTable;
	private JTextField txtFeeCode;
	public DailyWork() {
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		tabbedPane.setBackground(Color.BLUE);
		tabbedPane.setBorder(new LineBorder(Color.WHITE));
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel historyPanel = new JPanel();
		historyPanel.setBackground(Color.BLUE);
		tabbedPane.addTab("History", null, historyPanel, null);
		historyPanel.setLayout(new BorderLayout(0, 5));
		
		historyTable = new JTable(new AdviceHistoryTableModel());
		JScrollPane adviceTablescrollPane = new JScrollPane(historyTable);
		historyTable.setFont(Util.getSystemFont());
		historyTable.setRowHeight(25);
		historyPanel.add(adviceTablescrollPane, BorderLayout.CENTER);
		JTableHeader header = historyTable.getTableHeader();
		header.setFont(Util.getSystemFont());
		
		historyTable.setFillsViewportHeight(true);
		historyTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		historyTable.getColumnModel().getColumn(4).setPreferredWidth(30);
		
		JPanel adviceMaster = new JPanel();
		adviceMaster.setBackground(Color.BLUE);
		JPanel adviceButton = new JPanel();
		adviceButton.setLayout(new FlowLayout(FlowLayout.LEADING));
		adviceButton.setForeground(Color.WHITE);
		adviceButton.setBackground(Color.BLUE);
		adviceButton.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		adviceMaster.setLayout(new BorderLayout());
		
		JPanel advice = new JPanel();
		advice.setForeground(Color.WHITE);
		advice.setBackground(Color.BLUE);
		historyPanel.add(adviceMaster, BorderLayout.SOUTH);
		adviceMaster.add(advice, BorderLayout.CENTER);
		adviceMaster.add(adviceButton, BorderLayout.SOUTH);
		GridBagLayout gbl_advice = new GridBagLayout();
		gbl_advice.columnWidths = new int[]{170, 1000, 0};
		gbl_advice.rowHeights = new int[]{100, 100, 100, 0};
		gbl_advice.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_advice.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		advice.setLayout(gbl_advice);
		
		JLabel lblSymtom = new JLabel("Symtoms");
		lblSymtom.setForeground(Color.WHITE);
		lblSymtom.setFont(Util.getSystemFont());
		GridBagConstraints gbc_lblSymtom = new GridBagConstraints();
		gbc_lblSymtom.fill = GridBagConstraints.BOTH;
		gbc_lblSymtom.insets = new Insets(0, 0, 5, 5);
		gbc_lblSymtom.gridx = 0;
		gbc_lblSymtom.gridy = 0;
		advice.add(lblSymtom, gbc_lblSymtom);
		
		JTextArea txtSymtom = new JTextArea();
		JScrollPane symtomScrollPane = new JScrollPane();
		symtomScrollPane.setViewportView(txtSymtom);
		txtSymtom.setLineWrap(true);
		txtSymtom.setFont(Util.getSystemFont());
		GridBagConstraints gbc_txtSymtom = new GridBagConstraints();
		gbc_txtSymtom.fill = GridBagConstraints.BOTH;
		gbc_txtSymtom.insets = new Insets(0, 0, 5, 0);
		gbc_txtSymtom.gridx = 1;
		gbc_txtSymtom.gridy = 0;
		advice.add(symtomScrollPane, gbc_txtSymtom);
		
		JLabel lblMedicine = new JLabel("Presecription");
		lblMedicine.setForeground(Color.WHITE);
		lblMedicine.setFont(Util.getSystemFont());
		GridBagConstraints gbc_lblMedicine = new GridBagConstraints();
		gbc_lblMedicine.fill = GridBagConstraints.BOTH;
		gbc_lblMedicine.insets = new Insets(0, 0, 5, 5);
		gbc_lblMedicine.gridx = 0;
		gbc_lblMedicine.gridy = 1;
		advice.add(lblMedicine, gbc_lblMedicine);
		
		JTextArea txtMedicine = new JTextArea();
		JScrollPane medicineScrollPane = new JScrollPane();
		medicineScrollPane.setViewportView(txtMedicine);
		txtMedicine.setLineWrap(true);
		txtMedicine.setFont(Util.getSystemFont());
		GridBagConstraints gbc_txtMedicine = new GridBagConstraints();
		gbc_txtMedicine.fill = GridBagConstraints.BOTH;
		gbc_txtMedicine.insets = new Insets(0, 0, 5, 0);
		gbc_txtMedicine.gridx = 1;
		gbc_txtMedicine.gridy = 1;
		advice.add(medicineScrollPane, gbc_txtMedicine);
		txtMedicine.setColumns(10);
		
		
		JLabel lblAdvice = new JLabel("Advice");
		lblAdvice.setForeground(Color.WHITE);
		lblAdvice.setFont(Util.getSystemFont());
		GridBagConstraints gbc_lblAdvice = new GridBagConstraints();
		gbc_lblAdvice.fill = GridBagConstraints.BOTH;
		gbc_lblAdvice.insets = new Insets(0, 0, 0, 5);
		gbc_lblAdvice.gridx = 0;
		gbc_lblAdvice.gridy = 2;
		advice.add(lblAdvice, gbc_lblAdvice);
		
		JTextArea txtAdvice = new JTextArea();
		JScrollPane adviceScrollPane = new JScrollPane();
		adviceScrollPane.setViewportView(txtAdvice);
		txtAdvice.setLineWrap(true);
		txtAdvice.setFont(Util.getSystemFont());
		GridBagConstraints gbc_txtAdvice = new GridBagConstraints();
		gbc_txtAdvice.fill = GridBagConstraints.BOTH;
		gbc_txtAdvice.gridx = 1;
		gbc_txtAdvice.gridy = 2;
		advice.add(adviceScrollPane, gbc_txtAdvice);
		txtAdvice.setColumns(10);
		
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(Util.getSystemFont());
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JLabel lblFeeCode = new JLabel("Fee Code");
		lblFeeCode.setFont(Util.getSystemFont());
		lblFeeCode.setForeground(Color.WHITE);
		adviceButton.add(lblFeeCode);
		
		txtFeeCode = new JTextField();
		txtFeeCode.setFont(Util.getSystemFont());
		adviceButton.add(txtFeeCode);
		txtFeeCode.setColumns(10);
		adviceButton.add(btnSave);
		
		JPanel uploadPanel = new JPanel();
		uploadPanel.setBackground(Color.BLUE);
		
		JPanel attachmentsPanel = new JPanel();
		
		attachmentsPanel.setBackground(Color.BLUE);
		tabbedPane.addTab("Attachments", null, attachmentsPanel, null);
		attachmentsPanel.setLayout(new BorderLayout(0, 15));
		
		attachmentsPanel.add(uploadPanel, BorderLayout.WEST);
		
		JButton btnUploadAttachment = new JButton("Upload");
		btnUploadAttachment.setFont(Util.getSystemFont());
		uploadPanel.add(btnUploadAttachment);
		
		
		JScrollPane iconScollPane = new JScrollPane();
		attachmentsPanel.add(iconScollPane, BorderLayout.NORTH);
		
		JPanel iconPanel = new JPanel();
		iconScollPane.setViewportView(iconPanel);
		
		
		JScrollPane imageScrollPane = new JScrollPane();
		attachmentsPanel.add(imageScrollPane, BorderLayout.CENTER);
		
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.BLUE);
		imageScrollPane.setViewportView(imagePanel);
		
		Util.loadAttachmentIcons(iconPanel, imagePanel);
		
		JPanel patientPhotoPanel = new JPanel();
		patientPhotoPanel.setBackground(Color.BLUE);
		
		
		JPanel patientDetailsParent = new JPanel();
		patientDetailsParent.setBackground(Color.BLUE);
		patientDetailsParent.setLayout(new BorderLayout());
		patientDetailsParent.add(patientPhotoPanel, BorderLayout.WEST);
		patientPhotoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblPhoto = new JLabel("");
		lblPhoto.setHorizontalAlignment(SwingConstants.LEFT);
		Util.loadIconImage(lblPhoto);
		patientPhotoPanel.add(lblPhoto);
		
		JButton btnChoseImage = new JButton("Chose Image");
		btnChoseImage.setFont(Util.getSystemFont());
		patientPhotoPanel.add(btnChoseImage); 
		
		JPanel patientDetails = new JPanel();
		patientDetails.setForeground(Color.WHITE);
		patientDetails.setBackground(Color.BLUE);
		//patientDetails.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), null), "Patients Deatils", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(255, 255, 255)));
		patientDetails.setLayout(new GridLayout(0, 6, 0, 0));
		patientDetailsParent.add(patientDetails, BorderLayout.NORTH);
		
		tabbedPane.addTab("Deatils", null, patientDetailsParent, null);
		
		
		JLabel lblCode = new JLabel("Code   ");
		lblCode.setHorizontalAlignment(SwingConstants.LEFT);
		lblCode.setFont(Util.getSystemFont());
		lblCode.setForeground(Color.WHITE);
		patientDetails.add(lblCode);
		
		txtCode = new JTextField();
		txtCode.setFont(Util.getSystemFont());
		txtCode.setHorizontalAlignment(SwingConstants.LEFT);
		patientDetails.add(txtCode);
		txtCode.setColumns(10);
		
		JLabel label = new JLabel("");
		patientDetails.add(label);
		
		JLabel label_1 = new JLabel("");
		patientDetails.add(label_1);
		
		JLabel label_2 = new JLabel("");
		patientDetails.add(label_2);
		
		JLabel label_3 = new JLabel("");
		patientDetails.add(label_3);
		
		JLabel lblFirstName = new JLabel("First Name    ");
		lblFirstName.setHorizontalAlignment(SwingConstants.LEFT);
		lblFirstName.setFont(Util.getSystemFont());
		lblFirstName.setForeground(Color.WHITE);
		patientDetails.add(lblFirstName);
		
		firstName = new JTextField();
		firstName.setFont(Util.getSystemFont());
		patientDetails.add(firstName);
		firstName.setColumns(10);
		
		JLabel lblMiddle = new JLabel("Middle    ");
		lblMiddle.setHorizontalAlignment(SwingConstants.LEFT);
		lblMiddle.setFont(Util.getSystemFont());
		lblMiddle.setForeground(Color.WHITE);
		patientDetails.add(lblMiddle);
		
		txtMiddle = new JTextField();
		txtMiddle.setFont(Util.getSystemFont());
		patientDetails.add(txtMiddle);
		txtMiddle.setColumns(10);
		
		JLabel lblLast = new JLabel("Last    ");
		lblLast.setHorizontalAlignment(SwingConstants.LEFT);
		lblLast.setFont(Util.getSystemFont());
		lblLast.setForeground(Color.WHITE);
		patientDetails.add(lblLast);
		
		txtLast = new JTextField();
		txtLast.setFont(Util.getSystemFont());
		patientDetails.add(txtLast);
		txtLast.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(Util.getSystemFont());
		lblAddress.setForeground(Color.WHITE);
		patientDetails.add(lblAddress);
		
		txtAddress = new JTextField();
		txtAddress.setFont(Util.getSystemFont());
		patientDetails.add(txtAddress);
		txtAddress.setColumns(10);
		
		JLabel lblTelephone = new JLabel("Telephone");
		lblTelephone.setForeground(Color.WHITE);
		lblTelephone.setFont(Util.getSystemFont());
		patientDetails.add(lblTelephone);
		
		txtTelephone = new JTextField();
		txtTelephone.setFont(Util.getSystemFont());
		patientDetails.add(txtTelephone);
		txtTelephone.setColumns(10);
		
		JLabel lblMobile = new JLabel("Mobile");
		lblMobile.setFont(Util.getSystemFont());
		lblMobile.setForeground(Color.WHITE);
		patientDetails.add(lblMobile);
		
		txtMobile = new JTextField();
		txtMobile.setFont(Util.getSystemFont());
		patientDetails.add(txtMobile);
		txtMobile.setColumns(10);
		
		JLabel lblDob = new JLabel("DOB");
		lblDob.setFont(Util.getSystemFont());
		lblDob.setForeground(Color.WHITE);
		patientDetails.add(lblDob);
		
		txtDob = new JTextField();
		txtDob.setFont(Util.getSystemFont());
		patientDetails.add(txtDob);
		txtDob.setColumns(10);
		/*UtilDateModel model = new UtilDateModel();
		Properties props = new Properties();
		props.put("key", "value");
		props.put("text.today", "Today");
		props.put("text.month", "Month");
		props.put("text.year", "Year");
		props.put("text.clear", "Clear");
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, props);
		SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
		AbstractFormatter dateFormatter = new AbstractFormatter() {
			private SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
			@Override
			public String valueToString(Object date) throws ParseException {
				String result = null;
				if (date instanceof Date) {
					result = sfd.format(date);
				}
				
				return result;
			}
			
			@Override
			public Object stringToValue(String source) throws ParseException {
				Date date = null;
				date = sfd.parse(source);
				return date;
			}
		};

		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, dateFormatter);
		patientDetails.add(datePicker);*/
		
		JLabel lblAge = new JLabel("Age");
		lblAge.setForeground(Color.WHITE);
		patientDetails.add(lblAge);
		lblAge.setFont(Util.getSystemFont());
		
		txtAge = new JTextField();
		txtAge.setFont(Util.getSystemFont());
		patientDetails.add(txtAge);
		txtAge.setColumns(10);
		
		JLabel lblReference = new JLabel("Reference");
		lblReference.setForeground(Color.WHITE);
		patientDetails.add(lblReference);
		lblReference.setFont(Util.getSystemFont());
		
		txtReference = new JTextField();
		txtReference.setFont(Util.getSystemFont());
		patientDetails.add(txtReference);
		txtAge.setColumns(10);
	}

}
