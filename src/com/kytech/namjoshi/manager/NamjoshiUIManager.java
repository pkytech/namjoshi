package com.kytech.namjoshi.manager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
}
