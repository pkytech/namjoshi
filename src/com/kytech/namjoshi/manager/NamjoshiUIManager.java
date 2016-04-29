package com.kytech.namjoshi.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	public static final NamjoshiUIManager uiManager = new NamjoshiUIManager();
	
	private NamjoshiUIManager() {
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
		if (pat.getBirthDate() != null) {
			dailyWork.setAge(String.valueOf(Util.findAgeOfPatient(pat.getBirthDate())));
		}
		Date birthDate = pat.getBirthDate();
		if (birthDate != null) {
			SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
			dailyWork.setBirthDay(sfd.format(birthDate));
		}
		
		//Load Images
		
		//load Photo
		
		
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
		dailyWork.setBirthDay("");
		dailyWork.setSymtom("");
		dailyWork.setPrescription("");
		dailyWork.setAdvice("");
		dailyWork.setSelectedTab(0);
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
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		DailyWork dailyWork = dailyWorkPanel.getDailyWork();
		dailyWork.setSymtom(pre.getSymtoms());
		dailyWork.setPrescription(pre.getPrescription());
		dailyWork.setAdvice(pre.getAdvice());
	}

	public void selectDetailsTab() {
		NamjoshiClinic clinic = null;
		if (parentFrame instanceof NamjoshiClinic) {
			clinic = (NamjoshiClinic)parentFrame;
		}
		DailyWorkPanel dailyWorkPanel = clinic.getDailyPanel();
		DailyWork dailyWork = dailyWorkPanel.getDailyWork();
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
}
