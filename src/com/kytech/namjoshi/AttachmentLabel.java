/**
 * 
 */
package com.kytech.namjoshi;

import javax.swing.JLabel;

/**
 * @author tphadke
 *
 */
@SuppressWarnings("serial")
public class AttachmentLabel extends JLabel {

	private String fullFileName = null;

	/**
	 * @return the fullFileName
	 */
	public String getFullFileName() {
		return fullFileName;
	}

	/**
	 * @param fullFileName the fullFileName to set
	 */
	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}
}
