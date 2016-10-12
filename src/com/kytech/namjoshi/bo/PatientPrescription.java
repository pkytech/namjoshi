/**
 * 
 */
package com.kytech.namjoshi.bo;

import java.io.Serializable;

/**
 * @author tphadke
 *
 */
public class PatientPrescription implements Serializable {
	/**
	 * Serial version UID. 
	 */
	private static final long serialVersionUID = -8710796794791486880L;
	
	private long pid;
	private String firstName;
	private String lastName;
	private Prescription prescription;
	
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the prescription
	 */
	public Prescription getPrescription() {
		return prescription;
	}
	/**
	 * @param prescription the prescription to set
	 */
	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatientPrescription [pid=" + pid + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", prescription=" + prescription
				+ "]";
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
				+ ((prescription == null) ? 0 : prescription.hashCode());
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
		PatientPrescription other = (PatientPrescription) obj;
		if (pid != other.pid)
			return false;
		if (prescription == null) {
			if (other.prescription != null)
				return false;
		} else if (!prescription.equals(other.prescription))
			return false;
		return true;
	}
}
