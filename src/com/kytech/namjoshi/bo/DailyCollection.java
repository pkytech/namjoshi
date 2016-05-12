/**
 * 
 */
package com.kytech.namjoshi.bo;

import java.util.Date;

/**
 * @author tphadke
 *
 */
public class DailyCollection {
	private long patientCode;
	private long prescriptionCode;
	private Date examinationDate;
	private String firstName;
	private String lastName;
	private String feeCode;
	private double previousBal;
	private double amountPayable;
	private double outstanding;
	
	/**
	 * @return the patientCode
	 */
	public long getPatientCode() {
		return patientCode;
	}
	
	/**
	 * @param patientCode the patientCode to set
	 */
	public void setPatientCode(long patientCode) {
		this.patientCode = patientCode;
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @param feeCode the feeCode to set
	 */
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	/**
	 * @param previousBal the previousBal to set
	 */
	public void setPreviousBal(double previousBal) {
		this.previousBal = previousBal;
	}
	/**
	 * @param amountPayable the amountPayable to set
	 */
	public void setAmountPayable(double amountPayable) {
		this.amountPayable = amountPayable;
	}
	/**
	 * @param outstanding the outstanding to set
	 */
	public void setOutstanding(double outstanding) {
		this.outstanding = outstanding;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @return the feeCode
	 */
	public String getFeeCode() {
		return feeCode;
	}
	/**
	 * @return the previousBal
	 */
	public double getPreviousBal() {
		return previousBal;
	}
	/**
	 * @return the amountPayable
	 */
	public double getAmountPayable() {
		return amountPayable;
	}
	/**
	 * @return the outstanding
	 */
	public double getOutstanding() {
		return outstanding;
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
	 * @return the prescriptionCode
	 */
	public long getPrescriptionCode() {
		return prescriptionCode;
	}

	/**
	 * @param prescriptionCode the prescriptionCode to set
	 */
	public void setPrescriptionCode(long prescriptionCode) {
		this.prescriptionCode = prescriptionCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amountPayable);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((examinationDate == null) ? 0 : examinationDate.hashCode());
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
		DailyCollection other = (DailyCollection) obj;
		if (Double.doubleToLongBits(amountPayable) != Double
				.doubleToLongBits(other.amountPayable))
			return false;
		if (examinationDate == null) {
			if (other.examinationDate != null)
				return false;
		} else if (!examinationDate.equals(other.examinationDate))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DailyCollection [patientCode=" + patientCode
				+ ", examinationDate=" + examinationDate + ", firstName="
				+ firstName + ", lastName=" + lastName + ", feeCode=" + feeCode
				+ ", previousBal=" + previousBal + ", amountPayable="
				+ amountPayable + ", outstanding=" + outstanding + "]";
	}
}
