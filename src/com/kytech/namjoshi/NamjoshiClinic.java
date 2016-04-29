/**
 * 
 */
package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.kytech.namjoshi.manager.NamjoshiUIManager;
import com.kytech.namjoshi.util.Util;

/**
 * @author tphadke
 *
 */
public class NamjoshiClinic extends JFrame {
	private final Action dailyWorkAction = new SwingAction(getContentPane());
	private final ExitListner exitListner = new ExitListner(getContentPane());
	private DailyWorkPanel dailyWorkPanel = new DailyWorkPanel();
	private static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public NamjoshiClinic() {
		getContentPane().setBackground(Color.BLUE);
		

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.BLUE);
		setJMenuBar(menuBar);
		
		JMenu mnWork = new JMenu("Work");
		mnWork.setForeground(Color.WHITE);
		mnWork.setBackground(Color.BLUE);
		mnWork.setFont(Util.getSystemFont());
		menuBar.add(mnWork);
		
		JMenuItem mntmDailyWork = new JMenuItem("Daily Work");
		mntmDailyWork.setForeground(Color.BLACK);
		mntmDailyWork.setBackground(Color.WHITE);
		mntmDailyWork.setHorizontalAlignment(SwingConstants.LEFT);
		mntmDailyWork.setAction(dailyWorkAction);
		mntmDailyWork.setFont(Util.getSystemFont());
		mnWork.add(mntmDailyWork);
		
		JMenu mnReports = new JMenu("Reports");
		mnReports.setBackground(Color.BLUE);
		mnReports.setFont(Util.getSystemFont());
		menuBar.add(mnReports);
		
		JMenuItem mntmDailyCollection = new JMenuItem("Daily Collection");
		mntmDailyCollection.setFont(Util.getSystemFont());
		mnReports.add(mntmDailyCollection);
		
		JMenuItem mntmDues = new JMenuItem("Dues");
		mntmDues.setFont(Util.getSystemFont());
		mnReports.add(mntmDues);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel blankPanel = Util.getBasePanel();
		getContentPane().add(blankPanel, BorderLayout.CENTER);
		
		
		setFont(new Font("Arial", Font.BOLD, 16));
		setTitle("Namjoshi Clinic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		
		//Register parent window with UI manager
		NamjoshiUIManager.getUIManager().registerFrame(this);
		//setUndecorated(true);
		if (gd.isFullScreenSupported()) {
    		try {
    			gd.setFullScreenWindow(this);
    		}
    		finally {
    			gd.setFullScreenWindow(null);
    		}
    	}
		
		setVisible(true);
		//pack();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NamjoshiClinic clinic = new NamjoshiClinic();
		
	}
	
	private class ExitListner implements ActionListener {
		private Container parentContainer = null;
		public ExitListner(Container container) {
			this.parentContainer = container;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JPanel blankPanel = new JPanel();
			blankPanel.setBackground(Color.BLUE);
			this.parentContainer.add(blankPanel, BorderLayout.CENTER);
			dailyWorkPanel.setVisible(false);
			dailyWorkPanel = null;
			setVisible(true);
		}
		
	}

	private class SwingAction extends AbstractAction {
		private Container parentContainer = null;
		public SwingAction(Container container) {
			putValue(NAME, "Daily Work");
			putValue(SHORT_DESCRIPTION, "This action opens new panel for daily work");
			this.parentContainer = container;
		}
		public void actionPerformed(ActionEvent e) {
			if (dailyWorkPanel == null) {
				dailyWorkPanel = new DailyWorkPanel();
			}
			this.parentContainer.add(dailyWorkPanel, BorderLayout.CENTER);
			dailyWorkPanel.addExitListner(exitListner);
			dailyWorkPanel.setVisible(true);
			dailyWorkPanel.repaint();
			setVisible(true);
		}
	}

	/**
	 * Returns Daily work panel.
	 *  
	 * @return
	 */
	public DailyWorkPanel getDailyPanel() {
		return this.dailyWorkPanel;
	}
}
