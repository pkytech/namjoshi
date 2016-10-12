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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.kytech.namjoshi.bo.Prescription;
import com.kytech.namjoshi.manager.NamjoshiUIManager;
import com.kytech.namjoshi.table.AdviceHistoryTableModel;
import com.kytech.namjoshi.util.Util;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class DailyWork extends JPanel {
	private JTextField firstName;
	private JTextField txtMiddle;
	private JTextField txtLast;
	private JTextField txtAddress;
	private JTextField txtTelephone;
	private JTextField txtMobile;
	//private JTextField txtDob;
	private JTextField txtAge;
	private JTextField txtReference;
	private JTable historyTable;
	private JTextField txtFeeCode;
	private JTextArea txtSymtom;
	private JTextArea txtMedicine;
	private JTextArea txtAdvice;
	private JTabbedPane tabbedPane;
	private JDateChooser txtDob;
	private JPanel attachmentImagePanel;
	private JPanel attachmentIconPanel;
	private JLabel lblPhoto;
	private JScrollPane imageScrollPane;
	private JButton btnUploadAttachment;
	private JButton btnChoseImage;
	private JButton btnSaveUpdate;
	private JLabel lblAttachmentImageLabel;
	private AdviceHistoryTableModel historyTableModel = new AdviceHistoryTableModel();
	public DailyWork() {
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		tabbedPane.setBackground(Color.BLUE);
		tabbedPane.setBorder(new LineBorder(Color.WHITE));
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel historyPanel = new JPanel();
		historyPanel.setBackground(Color.BLUE);
		tabbedPane.addTab("History", null, historyPanel, null);
		historyPanel.setLayout(new BorderLayout(0, 5));
		historyTable = createJTable();
		historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane adviceTablescrollPane = new JScrollPane(historyTable);
		historyTable.setFont(Util.getSystemFont());
		historyTable.setRowHeight(25);
		historyPanel.add(adviceTablescrollPane, BorderLayout.CENTER);
		JTableHeader header = historyTable.getTableHeader();
		header.setFont(Util.getSystemFont());
		historyTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedRow = historyTable.getSelectedRow(); 
				if (selectedRow > -1) {
					Prescription pre = historyTableModel.getRows().get(selectedRow);
					NamjoshiUIManager.getUIManager().loadPrescription(pre);
				}
			}
		});
		
		historyTable.setFillsViewportHeight(true);
		int widths[] = historyTableModel.getColumnWidths();
		TableColumnModel columnMode = historyTable.getColumnModel();
		int column = 0;
		for (int width : widths) {
			columnMode.getColumn(column++).setPreferredWidth(width);
		}
		
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
		gbl_advice.columnWidths = new int[] {170, 800, 0};
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
		
		txtSymtom = new JTextArea();
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
		
		txtMedicine = new JTextArea();
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
		
		txtAdvice = new JTextArea();
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
				NamjoshiUIManager.getUIManager().insertPrescription();
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
		txtFeeCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().insertPrescription();
			}
		});
		adviceButton.add(btnSave);
		
		JPanel uploadPanel = new JPanel();
		uploadPanel.setBackground(Color.BLUE);
		
		JPanel attachmentsPanel = new JPanel();
		
		attachmentsPanel.setBackground(Color.BLUE);
		tabbedPane.addTab("Attachments", null, attachmentsPanel, null);
		attachmentsPanel.setLayout(new BorderLayout(0, 15));
		
		attachmentsPanel.add(uploadPanel, BorderLayout.WEST);
		
		btnUploadAttachment = new JButton("Upload");
		btnUploadAttachment.setFont(Util.getSystemFont());
		btnUploadAttachment.setEnabled(false);
		uploadPanel.add(btnUploadAttachment);
		btnUploadAttachment.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().uploadAttachments();
			}
		});
		
		
		JScrollPane iconScollPane = new JScrollPane();
		attachmentsPanel.add(iconScollPane, BorderLayout.NORTH);
		
		attachmentIconPanel = new JPanel();
		attachmentIconPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		iconScollPane.setViewportView(attachmentIconPanel);
		
		
		imageScrollPane = new JScrollPane();
		attachmentsPanel.add(imageScrollPane, BorderLayout.CENTER);
		
		attachmentImagePanel = new JPanel();
		attachmentImagePanel.setBackground(Color.BLUE);
		imageScrollPane.setViewportView(attachmentImagePanel);
		attachmentImagePanel.setLayout(new BorderLayout(0, 0));
		lblAttachmentImageLabel = new JLabel("");
		lblAttachmentImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		attachmentImagePanel.add(lblAttachmentImageLabel, BorderLayout.CENTER);
		
		
		//load dummy image
		Util.loadEmptyAttachmentIcon(attachmentIconPanel);
		
		JPanel patientPhotoPanel = new JPanel();
		patientPhotoPanel.setBackground(Color.BLUE);
		
		
		JPanel patientDetailsParent = new JPanel();
		patientDetailsParent.setBackground(Color.BLUE);
		patientDetailsParent.setLayout(new BorderLayout());
		patientDetailsParent.add(patientPhotoPanel, BorderLayout.WEST);
		patientPhotoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		lblPhoto = new JLabel("");
		lblPhoto.setHorizontalAlignment(SwingConstants.LEFT);
		Util.loadIconImage(lblPhoto, 200, 200);
		patientPhotoPanel.add(lblPhoto);
		
		btnChoseImage = new JButton("Chose Image");
		btnChoseImage.setFont(Util.getSystemFont());
		btnChoseImage.setEnabled(false);
		patientPhotoPanel.add(btnChoseImage);
		btnChoseImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().chooseProfilePicture();
				
			}
		});
		
		JPanel patientDetails = new JPanel();
		patientDetails.setForeground(Color.WHITE);
		patientDetails.setBackground(Color.BLUE);
		//patientDetails.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), null), "Patients Deatils", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(255, 255, 255)));
		patientDetails.setLayout(new GridLayout(0, 6, 0, 0));
		patientDetailsParent.add(patientDetails, BorderLayout.NORTH);
		
		tabbedPane.addTab("Deatils", null, patientDetailsParent, null);
		
		JLabel lblFirstName = new JLabel("First Name    ");
		lblFirstName.setHorizontalAlignment(SwingConstants.LEFT);
		lblFirstName.setFont(Util.getSystemFont());
		lblFirstName.setForeground(Color.WHITE);
		patientDetails.add(lblFirstName);
		
		firstName = new JTextField();
		firstName.setFont(Util.getSystemFont());
		patientDetails.add(firstName);
		firstName.setColumns(10);
		Util.addUpperCaseDocumentFilter(firstName);
		
		JLabel lblMiddle = new JLabel("Middle    ");
		lblMiddle.setHorizontalAlignment(SwingConstants.LEFT);
		lblMiddle.setFont(Util.getSystemFont());
		lblMiddle.setForeground(Color.WHITE);
		patientDetails.add(lblMiddle);
		
		txtMiddle = new JTextField();
		txtMiddle.setFont(Util.getSystemFont());
		patientDetails.add(txtMiddle);
		txtMiddle.setColumns(10);
		Util.addUpperCaseDocumentFilter(txtMiddle);
		
		JLabel lblLast = new JLabel("Last    ");
		lblLast.setHorizontalAlignment(SwingConstants.LEFT);
		lblLast.setFont(Util.getSystemFont());
		lblLast.setForeground(Color.WHITE);
		patientDetails.add(lblLast);
		
		txtLast = new JTextField();
		txtLast.setFont(Util.getSystemFont());
		patientDetails.add(txtLast);
		txtLast.setColumns(10);
		Util.addUpperCaseDocumentFilter(txtLast);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(Util.getSystemFont());
		lblAddress.setForeground(Color.WHITE);
		patientDetails.add(lblAddress);
		
		txtAddress = new JTextField();
		txtAddress.setFont(Util.getSystemFont());
		patientDetails.add(txtAddress);
		txtAddress.setColumns(10);
		Util.addUpperCaseDocumentFilter(txtAddress);
		
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
		
		txtDob = new JDateChooser(null, "dd/MM/yyyy");
		txtDob.setFont(Util.getSystemFont());
		txtDob.setFont(Util.getSystemFont());
		patientDetails.add(txtDob);
		
		JLabel lblAge = new JLabel("Age");
		lblAge.setForeground(Color.WHITE);
		patientDetails.add(lblAge);
		lblAge.setFont(Util.getSystemFont());
		
		txtAge = new JTextField();
		txtAge.setEnabled(false);
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
		
		btnSaveUpdate = new JButton("Save/Update");
		patientDetails.add(btnSaveUpdate);
		btnSaveUpdate.setEnabled(false);
		btnSaveUpdate.setFont(Util.getSystemFont());
		btnSaveUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NamjoshiUIManager.getUIManager().saveOrUpdatePatientDetails();
			}
		});
	}

	private JTable createJTable() {
		JTable prescriptionTable = new JTable(historyTableModel) {
			@Override
			public String getToolTipText(MouseEvent event) {
				java.awt.Point p = event.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);
				if (colIndex == 1 || colIndex == 2 || colIndex == 3) {
					if (rowIndex > -1) {
						String cellText = String.valueOf(getValueAt(rowIndex, colIndex));
						cellText = cellText.replaceAll("(\r\n|\n)", "<br/>");
						String toolTip = "<html><body><p><font size=\"5\">" + cellText + "</font></p></body></html>";
						return  toolTip;
					}
				}
				return super.getToolTipText(event);
			}
		};
		JPopupMenu popup = new JPopupMenu();
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setFont(Util.getSystemFont());
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.setVisible(false);
				popup.removeAll();
			}
		});
		prescriptionTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				int row = prescriptionTable.rowAtPoint(me.getPoint());
				int column = prescriptionTable.columnAtPoint(me.getPoint());
				if (me.getButton() == MouseEvent.BUTTON3 && column == 2 && row > -1) {
					String value = String.valueOf(prescriptionTable.getValueAt(row, column));

					if (value != null && value.trim().length() > 0) {
						String values[] = value.split("(\r\n|\n)");
						popup.removeAll();
						for (String val : values) {
							JMenuItem item = new JMenuItem("Append.. "+ val);
							item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									NamjoshiUIManager.getUIManager().appendPrescription(val);
								}
							});
							item.setFont(Util.getSystemFont());
							popup.add(item);
						}
						popup.add(exitItem);
						popup.setVisible(true);
						popup.show(prescriptionTable, me.getX(), me.getY());
					}
				}
			}
		});

		return prescriptionTable;
	}

	public void setFirstName(String name) {
		firstName.setText(name);
	}
	
	public String getFirstName() {
		return firstName.getText();
	}
	
	public void setMiddleName(String name) {
		txtMiddle.setText(name);
	}
	
	public String getMiddleName() {
		return txtMiddle.getText();
	}
	
	public void setLastName(String name) {
		txtLast.setText(name);
	}
	
	public String getLastName() {
		return txtLast.getText();
	}
	
	public void setAddress(String address) {
		txtAddress.setText(address);
	}
	
	public String getAddress() {
		return txtAddress.getText();
	}
	
	public void setTelephone(String tele) {
		txtTelephone.setText(tele);
	}
	
	public String getTelephone() {
		return this.txtTelephone.getText();
	}
	
	public void setMobile(String mobile) {
		txtMobile.setText(mobile);
	}
	
	public String getMobile() {
		return this.txtMobile.getText();
	}
	
	public void setBirthDate(Date date) {
		txtDob.setDate(date);
	}

	public Date getBirthDate() {
		return this.txtDob.getDate();
	}
	
	public void setAge(String age) {
		txtAge.setText(age);
	}
	
	public void setReference(String ref) {
		txtReference.setText(ref);
	}
	
	public String getReference() {
		return this.txtReference.getText();
	}
	
	public void setHistoryTableModelData(List<Prescription> rows) {
		if (rows == null) {
			return;
		}
		historyTableModel.setRows(rows);
		historyTableModel.fireTableDataChanged();
		historyTable.repaint();
	}
	
	public void addHistory(Prescription pre) {
		historyTableModel.getHistoryData().add(0, pre);
		historyTableModel.fireTableDataChanged();
		historyTable.repaint();
	}
	
	public String getSymtom() {
		return txtSymtom.getText();
	}
	
	public void setSymtom(String symtom) {
		txtSymtom.setText(symtom);
	}
	
	public String getPrescription() {
		return txtMedicine.getText();
	}
	
	public void setPrescription(String prescription) {
		txtMedicine.setText(prescription);
	}
	
	public String getAdvice() {
		return txtAdvice.getText();
	}
	
	public void setAdvice(String advice) {
		txtAdvice.setText(advice);
	}
	
	public void setSelectedTab(int index) {
		int tabCount = tabbedPane.getTabCount();
		if (tabCount < index) {
			return;
		}
		tabbedPane.setSelectedIndex(index);
	}

	public String getFeeCode() {
		return txtFeeCode.getText();
	}
	
	public void setFeeCode(String feeCode) {
		txtFeeCode.setText(feeCode);
	}
	
	public JLabel getProfilePictureLabel() {
		return this.lblPhoto;
	}
	
	public JPanel getAttachmentIconPanel() {
		return this.attachmentIconPanel;
	}
	
	public JPanel getAttachmentImagePanel() {
		return this.attachmentImagePanel;
	}

	public JScrollPane getAttachmentScrollPanel() {
		return this.imageScrollPane;
	}
	
	public JButton getAttachmentUploadButton() {
		return this.btnUploadAttachment;
	}
	
	public JButton getUploadProfilePictureButton() {
		return this.btnChoseImage;
	}
	
	public JButton getSaveProfileButton() {
		return this.btnSaveUpdate;
	}

	public JLabel getAttachmentImageLabel() {
		return this.lblAttachmentImageLabel;
	}
	
	public void focusFirstName() {
		firstName.requestFocusInWindow();
	}
}
