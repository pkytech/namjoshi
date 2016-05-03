package com.kytech.namjoshi.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.kytech.namjoshi.DailyWork;
import com.kytech.namjoshi.DailyWorkPanel;
import com.kytech.namjoshi.NamjoshiClinic;
import com.kytech.namjoshi.PatientSearchPanel;
import com.kytech.namjoshi.RecordSelector;
import com.kytech.namjoshi.bo.Patient;
import com.kytech.namjoshi.bo.Prescription;
import com.kytech.namjoshi.util.DBUtil;
import com.kytech.namjoshi.util.Util;

public final class NamjoshiUIManager {

	private JFrame parentFrame;
	private JFileChooser fc;
	private boolean isAttachmentImagesFound = false;
	public static final NamjoshiUIManager uiManager = new NamjoshiUIManager();
	
	private NamjoshiUIManager() {
		fc = new JFileChooser();
	}
	
	public static final NamjoshiUIManager getUIManager() {
		return uiManager;
	}

	public void registerFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	public void showInformationMessage(String message) {
		JOptionPane.showMessageDialog(parentFrame,
			    message);
	}
	
	public int showOptionMessage(String message) {
		return JOptionPane.showConfirmDialog(parentFrame, message, "Namjoshi Clinic", JOptionPane.YES_NO_OPTION);
	}
	
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(parentFrame, message, "Namjoshi Clinic", JOptionPane.ERROR_MESSAGE);
	}
	
	public void loadPatient(String patientNumber) {
		long patientNo = -1;
		try {
			patientNo = Long.parseLong(patientNumber);
		} catch(NumberFormatException nfe) {
			showErrorMessage("No patient found. Please check number");
			return;
		}
		Patient pat = DBUtil.loadPatient(patientNo);
		if (pat == null) {
			showErrorMessage("No patient found with code: " + patientNumber);
			return;
		}
		List<Prescription> pre = DBUtil.loadPrescription(patientNo);
		
		//set Patient details
		String patientFullName = pat.getFirstName() + " " + pat.getMiddleName() + " " + pat.getLastName();
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		RecordSelector recSel = dailyWorkPanel.getRecordSelector();
		DailyWork dailyWork = dailyWorkPanel.getDailyWork();
		dailyWork.setHistoryTableModelData(pre);
		recSel.setPatientName(patientFullName);
		recSel.disablePatientCode();
		dailyWork.setFirstName(pat.getFirstName());
		dailyWork.setMiddleName(pat.getMiddleName());
		dailyWork.setLastName(pat.getLastName());
		dailyWork.setAddress(pat.getAddress());
		dailyWork.setTelephone(pat.getTelephoneNumber());
		dailyWork.setMobile(pat.getMobileNumber());
		dailyWork.setReference(pat.getDrReference());
		if (pat.getBirthDate() != null) {
			dailyWork.setAge(String.valueOf(Util.findAgeOfPatient(pat.getBirthDate())));
		}
		Date birthDate = pat.getBirthDate();
		if (birthDate != null) {
			//SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
			dailyWork.setBirthDate(birthDate);
		}
		
		//Load Images
		JPanel attachIconPanel = dailyWork.getAttachmentIconPanel();
		attachIconPanel.removeAll();
		attachIconPanel.repaint();
		JPanel attachImagePanel = dailyWork.getAttachmentImagePanel();
		Util.emptyAttachmentImage(dailyWork.getAttachmentImageLabel());
		attachImagePanel.repaint();
		Util.loadAttachmentIcons(attachIconPanel, attachImagePanel, patientNumber);

		//load Photo
		Util.loadIconImage(patientNumber, dailyWork.getProfilePictureLabel(), 200, 200);
		
		//Enable profile buttons
		enableUploadAttachmentButton();
		enableUploadProfilePicture();
		enableSaveProfileButton();
	}
	
	public void clearPatientData() {
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		RecordSelector recSel = dailyWorkPanel.getRecordSelector();
		DailyWork dailyWork = dailyWorkPanel.getDailyWork();
		List<Prescription> rows = new ArrayList<Prescription>();
		recSel.setPatientCode("");
		dailyWork.setHistoryTableModelData(rows);
		recSel.enablePatientCode();
		recSel.setPatientName("");
		dailyWork.setFirstName("");
		dailyWork.setMiddleName("");
		dailyWork.setLastName("");
		dailyWork.setAddress("");
		dailyWork.setAge("");
		dailyWork.setTelephone("");
		dailyWork.setMobile("");
		dailyWork.setReference("");
		dailyWork.setBirthDate(null);
		dailyWork.setSymtom("");
		dailyWork.setPrescription("");
		dailyWork.setAdvice("");
		dailyWork.setFeeCode("");
		dailyWork.setSelectedTab(0);
		
		//Reset profile picture
		Util.loadIconImage(dailyWork.getProfilePictureLabel(), 200, 200);
		
		//Reset attachments
		JPanel attachmentIconPanel = dailyWork.getAttachmentIconPanel();
		attachmentIconPanel.removeAll();
		attachmentIconPanel.repaint();
		JPanel attachmentImagePanel = dailyWork.getAttachmentImagePanel();
		attachmentImagePanel.repaint();
		Util.emptyAttachmentImage(dailyWork.getAttachmentImageLabel());
		Util.loadEmptyAttachmentIcon(attachmentIconPanel);
		
		disableUploadAttachmentButton();
		disableUploadProfilePicture();
		disableSaveProfileButton();
		resetAttachmentImageFound();
	}

	public void disablePatientCode() {
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		RecordSelector recSel = dailyWorkPanel.getRecordSelector();
		recSel.disablePatientCode();
	}
	
	public void loadPrescription(Prescription pre) {
		DailyWork dailyWork = getDailyWork();
		dailyWork.setSymtom(pre.getSymtoms());
		dailyWork.setPrescription(pre.getPrescription());
		dailyWork.setAdvice(pre.getAdvice());
	}

	public void selectDetailsTab() {
		DailyWork dailyWork = getDailyWork();
		dailyWork.setSelectedTab(2);
	}

	public void searchPatient(String firstName, String middleName, String lastName) {
		if ((firstName == null || firstName.trim().length() <= 0) && 
				(middleName == null || middleName.trim().length() <= 0) && 
				(lastName == null || lastName.trim().length() <= 0)) {
			
			showErrorMessage("Choose atleat one field to search");
			return;
		}
		List<Patient> patients = DBUtil.searchPatients(firstName, middleName, lastName);
		if (patients == null || patients.size() == 0) {
			showInformationMessage("No patient records found");
			return;
		}
		
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		RecordSelector recSel = dailyWorkPanel.getRecordSelector();
		
		PatientSearchPanel  searchPanel = recSel.getSearchPanel();
		if (patients != null && patients.size() > 0) {
			searchPanel.getPatientSearchResultModel().setPatients(patients);
			searchPanel.getPatientSearchResultModel().fireTableDataChanged();
			searchPanel.repaintTable();
		}
	}
	
	public void resetSearchPanel() {
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		RecordSelector recSel = dailyWorkPanel.getRecordSelector();
		
		PatientSearchPanel  searchPanel = recSel.getSearchPanel();
		List<Patient> patients = new ArrayList<Patient>();
		
		searchPanel.getPatientSearchResultModel().setPatients(patients);
		searchPanel.getPatientSearchResultModel().fireTableDataChanged();
		searchPanel.repaintTable();
		searchPanel.setPatientCode("");
		searchPanel.setFirstName("");
		searchPanel.setMiddleName("");
		searchPanel.setLastName("");
	}

	public void loadSearchedPatient() {
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		RecordSelector recSel = dailyWorkPanel.getRecordSelector();
		
		PatientSearchPanel  searchPanel = recSel.getSearchPanel();
		long patientId = searchPanel.getSelectedRecord();
		recSel.setPatientCode(String.valueOf(patientId));
		recSel.disablePatientCode();
		loadPatient(String.valueOf(patientId));
	}

	public void saveOrUpdatePatientDetails() {
		DailyWork dailyWork = getDailyWork();
		RecordSelector recSel = getRecordSelector();
		
		String firstName = dailyWork.getFirstName();
		if (firstName == null || firstName.trim().length() <= 0) {
			showErrorMessage("Please enter patient first name");
			return;
		}
		String middleName = dailyWork.getMiddleName();
		middleName = middleName == null || middleName.trim().length() == 0 ? null : middleName;
		String lastName = dailyWork.getLastName();
		if (lastName == null || lastName.trim().length() <= 0) {
			showErrorMessage("Please enter patient last name");
			return;
		}
		String address = dailyWork.getAddress();
		address = address == null || address.trim().length() == 0 ? null : address;
		String telephone = dailyWork.getTelephone();
		telephone = telephone == null || telephone.trim().length() == 0 ? null : telephone;
		String mobile = dailyWork.getMobile();
		mobile = mobile == null || mobile.trim().length() == 0 ? null : mobile;
		String reference = dailyWork.getReference();
		reference = reference == null || reference.trim().length() == 0 ? null : reference;
		Date dob = dailyWork.getBirthDate();
		if (dob == null) {
			showErrorMessage("Please enter patient birthday");
			return;
		}
		String patientCode = recSel.getPatientCode();
		if (patientCode == null || patientCode.trim().length() <= 0) {
			long patientCodeLong = DBUtil.createPatient(firstName, middleName, lastName, address, telephone, mobile, reference, dob);
			patientCode = String.valueOf(patientCodeLong);
			recSel.setPatientCode(patientCode);
			recSel.setPatientName(firstName + " " + lastName);
			dailyWork.setAge(String.valueOf(Util.findAgeOfPatient(dob)));
			showInformationMessage("Patient record created. New patient number is: " + patientCode);
		} else {
			DBUtil.updatePatient(patientCode, firstName, middleName, lastName, address, telephone, mobile, reference, dob);
			showInformationMessage("Patient record updated.");
		}
	}

	public void insertPrescription() {
		DailyWork dailyWork = getDailyWork();
		RecordSelector recSel = getRecordSelector();
		
		String patientCode = recSel.getPatientCode();
		String firstName = dailyWork.getFirstName();
		String lastName = dailyWork.getLastName();
		String  name = firstName + " " + lastName;
		String symtom = dailyWork.getSymtom();
		String prescription = dailyWork.getPrescription();
		String advice = dailyWork.getAdvice();
		String feeCode = dailyWork.getFeeCode();
		if (symtom == null || symtom.trim().length() == 0) {
			showErrorMessage("Please enter symtoms");
		}
		if (prescription == null || prescription.trim().length() == 0) {
			showErrorMessage("Please enter Precription");
		}
		if (feeCode == null || feeCode.trim().length() == 0) {
			showErrorMessage("Please enter fee code");
		}

		boolean success = DBUtil.insertPrescription(Long.parseLong(patientCode), symtom, prescription, advice, feeCode);
		
		if (!success) {
			showErrorMessage("Failed to save record. Please see log for error");
			return;
		} else {
			int option = showOptionMessage("Patient updated successfully, Patient code: " + patientCode + "\nPrint Prescription..?");
			if (option == JOptionPane.OK_OPTION) {
				Util.printPrescription(patientCode, name, prescription, feeCode);
			}
			
		}
		Prescription pre = new Prescription();
		pre.setSymtoms(symtom);
		pre.setPrescription(prescription);
		pre.setAdvice(advice);
		pre.setFeeCode(feeCode);
		pre.setExaminationDate(new Date());

		dailyWork.addHistory(pre);
		dailyWork.setSymtom("");
		dailyWork.setPrescription("");
		dailyWork.setAdvice("");
		dailyWork.setFeeCode("");
	}
	
	public void chooseProfilePicture() {
		DailyWork dailyWork = getDailyWork();
		RecordSelector recSel = getRecordSelector();
		String patientCode = recSel.getPatientCode();
		
		if (patientCode == null || patientCode.trim().length() == 0) {
			showErrorMessage("Please select patient before uploading profile picture");
			return;
		}
		
		int returnVal = fc.showSaveDialog(parentFrame);
		
		File selectedFile = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFile = fc.getSelectedFile();
			String name = selectedFile.getName().toLowerCase();
			if (!(name.endsWith(".jpg") || name.endsWith(".jpeg"))) {
				showErrorMessage("Application accept only JPG files. Choose different file");
				return;
			}
			double fileSizeInMb = Util.findFileSizeInMb(selectedFile);
			if (fileSizeInMb > 1.2) {
				showErrorMessage("File size should not be greater than 1.2 MB. Please resize image or upload new image.");
				return;
			}
			Util.uploadProfilePicture(patientCode, selectedFile);
			Util.loadIconImage(patientCode, dailyWork.getProfilePictureLabel(), 200, 200);
			showInformationMessage("Profile pciture updated successfully");
		}
	}
	
	public void uploadAttachments() {
		DailyWork dailyWork = getDailyWork();
		RecordSelector recSel = getRecordSelector();
		String patientCode = recSel.getPatientCode();
	
		if (patientCode == null || patientCode.trim().length() == 0) {
			showErrorMessage("Please select patient before uploading profile picture");
			return;
		}
		
		int returnVal = fc.showSaveDialog(parentFrame);
		
		File selectedFile = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFile = fc.getSelectedFile();
			String name = selectedFile.getName().toLowerCase();
			if (!(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif"))) {
				showErrorMessage("Application accept only JPG/GIF files. Choose different file");
				return;
			}
			double fileSizeInMb = Util.findFileSizeInMb(selectedFile);
			if (fileSizeInMb > 1.2) {
				showErrorMessage("File size should not be greater than 1.2 MB. Please resize image or upload new image.");
				return;
			}
			JPanel iconPanel = dailyWork.getAttachmentIconPanel();
			JPanel attachmentPanel = dailyWork.getAttachmentImagePanel();
			Util.uploadAttachment(iconPanel, attachmentPanel, patientCode, selectedFile);
			showInformationMessage("Profile pciture updated successfully");
		}
	}
	
	public void repaintAttachmentImagePanel() {
		DailyWork dailyWork = getDailyWork();
		dailyWork.getAttachmentImagePanel().repaint();
		dailyWork.getAttachmentScrollPanel().repaint();
	}

	private DailyWork getDailyWork() {
		DailyWork dailyWork = getDailyWorkPanel().getDailyWork();
		return dailyWork;
	}
	
	public DailyWorkPanel getDailyWorkPanel() {
		return getNamjoshiClinic().getDailyPanel();
	}

	public RecordSelector getRecordSelector() {
		return getDailyWorkPanel().getRecordSelector();
	}
	
	public NamjoshiClinic getNamjoshiClinic() {
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		return clinic;
	}
	
	public void enableUploadAttachmentButton() {
		DailyWork dailyWork = getDailyWork();
		dailyWork.getAttachmentUploadButton().setEnabled(true);
	}
	
	public void disableUploadAttachmentButton() {
		DailyWork dailyWork = getDailyWork();
		dailyWork.getAttachmentUploadButton().setEnabled(false);
	}
	
	public void enableUploadProfilePicture() {
		getDailyWork().getUploadProfilePictureButton().setEnabled(true);
	}
	
	public void disableUploadProfilePicture() {
		getDailyWork().getUploadProfilePictureButton().setEnabled(false);
	}

	public void enableSaveProfileButton() {
		getDailyWork().getSaveProfileButton().setEnabled(true);
	}
	
	public void disableSaveProfileButton() {
		getDailyWork().getSaveProfileButton().setEnabled(false);
	}
	
	public void attachmentImgesFound() {
		isAttachmentImagesFound = true;
	}
	
	public void resetAttachmentImageFound() {
		isAttachmentImagesFound = false;
	}

	public boolean isAttachmentImagesFound() {
		return this.isAttachmentImagesFound;
	}

	public JLabel getAttachmentImageLabel() {
		return getDailyWork().getAttachmentImageLabel();
	}
}
