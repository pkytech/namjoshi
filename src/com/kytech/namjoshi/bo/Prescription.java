package com.kytech.namjoshi.bo;

import java.util.Date;

public class Prescription {
	private long pid;
	private long prescriptionId;
	private String symtoms;
	private String prescription;
	private String advice;
	private Date examinationDate;
	private String feeCode;
	private double paidAmount;
	private double outstandingAmount;
	private double netPayableAmount;
	private double receivedAmount;
	/**
	 * @return the pid
	 */
	public long getPid() {
		return pid;
	}
	/**
	 * @param pid the pid to set
	 */
	public void setPid(long pid) {
		this.pid = pid;
	}
	/**
	 * @return the prescriptionId
	 */
	public long getPrescriptionId() {
		return prescriptionId;
	}
	/**
	 * @param prescriptionId the prescriptionId to set
	 */
	public void setPrescriptionId(long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}
	/**
	 * @return the symtoms
	 */
	public String getSymtoms() {
		return symtoms;
	}
	/**
	 * @param symtoms the symtoms to set
	 */
	public void setSymtoms(String symtoms) {
		this.symtoms = symtoms;
	}
	/**
	 * @return the prescription
	 */
	public String getPrescription() {
		return prescription;
	}
	/**
	 * @param prescription the prescription to set
	 */
	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}
	/**
	 * @return the advice
	 */
	public String getAdvice() {
		return advice;
	}
	/**
	 * @param advice the advice to set
	 */
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	/**
	 * @return the examinationDate
	 */
	public Date getExaminationDate() {
		return examinationDate;
	}
	/**
	 * @param examinationDate the examinationDate to set
	 */
	public void setExaminationDate(Date examinationDate) {
		this.examinationDate = examinationDate;
	}
	/**
	 * @return the feeCode
	 */
	public String getFeeCode() {
		return feeCode;
	}
	/**
	 * @param feeCode the feeCode to set
	 */
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	/**
	 * @return the paidAmount
	 */
	public double getPaidAmount() {
		return paidAmount;
	}
	/**
	 * @param paidAmount the paidAmount to set
	 */
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	/**
	 * @return the outstandingAmount
	 */
	public double getOutstandingAmount() {
		return outstandingAmount;
	}
	/**
	 * @param outstandingAmount the outstandingAmount to set
	 */
	public void setOutstandingAmount(double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	/**
	 * @return the netPayableAmount
	 */
	public double getNetPayableAmount() {
		return netPayableAmount;
	}
	/**
	 * @param netPayableAmount the netPayableAmount to set
	 */
	public void setNetPayableAmount(double netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}
	/**
	 * @return the receivedAmount
	 */
	public double getReceivedAmount() {
		return receivedAmount;
	}
	/**
	 * @param receivedAmount the receivedAmount to set
	 */
	public void setReceivedAmount(double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (pid ^ (pid >>> 32));
		result = prime * result
				+ (int) (prescriptionId ^ (prescriptionId >>> 32));
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prescription other = (Prescription) obj;
		if (pid != other.pid)
			return false;
		if (prescriptionId != other.prescriptionId)
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Prescription [pid=" + pid + ", prescriptionId="
				+ prescriptionId + ", symtoms=" + symtoms + ", prescription="
				+ prescription + ", advice=" + advice + ", examinationDate="
				+ examinationDate + ", feeCode=" + feeCode + ", paidAmount="
				+ paidAmount + ", outstandingAmount=" + outstandingAmount
				+ ", netPayableAmount=" + netPayableAmount
				+ ", receivedAmount=" + receivedAmount + "]";
	}
}
