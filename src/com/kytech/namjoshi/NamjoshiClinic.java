/**
 * 
 */
package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
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
import com.kytech.namjoshi.table.DailyCollectionTableModel;
import com.kytech.namjoshi.util.Util;

/**
 * @author tphadke
 *
 */
@SuppressWarnings("serial")
public class NamjoshiClinic extends JFrame {
	private final Action dailyWorkAction = new SwingAction(this);
	private final ExitListner exitListner = new ExitListner();
	private DailyWorkPanel dailyWorkPanel;
	private DailyCollectionPanel dailyCollection;
	private static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private final Action action = new DailyCollectionAction(this);
	private final Action duesAction = new DailyCollectionAction(this, DailyCollectionTableModel.DUES_TYPES);
	public NamjoshiClinic() {
		getContentPane().setBackground(Color.BLUE);
		Util.setFrameIcon(this, "hospital-2-16.png", "hospital-2-32.png");

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
		mnReports.setForeground(Color.WHITE);
		mnReports.setFont(Util.getSystemFont());
		menuBar.add(mnReports);
		
		JMenuItem mntmDailyCollection = new JMenuItem("Daily Collection");
		mntmDailyCollection.setAction(action);
		mntmDailyCollection.setFont(Util.getSystemFont());
		mnReports.add(mntmDailyCollection);
		
		JMenuItem mntmDues = new JMenuItem("Dues");
		mntmDues.setAction(duesAction);
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
	@SuppressWarnings("unused")
	public static void main(final String[] args) {
		NamjoshiClinic clinic = new NamjoshiClinic();
	}
	
	private void removeDailyWork() {
		JPanel blankPanel = Util.getBasePanel();
		getContentPane().removeAll();
		getContentPane().add(blankPanel, BorderLayout.CENTER);
		dailyWorkPanel.setVisible(false);
		dailyWorkPanel = null;
		getContentPane().repaint();
		this.repaint();
		repaint();
		setVisible(true);
	}

	private void removeDailyCollection() {
		JPanel blankPanel = Util.getBasePanel();
		getContentPane().removeAll();
		getContentPane().add(blankPanel, BorderLayout.CENTER);
		dailyCollection.setVisible(false);
		dailyCollection = null;
		getContentPane().repaint();
		this.repaint();
		setVisible(true);
	}
	
	private class ExitListner implements ActionListener {
		
		public ExitListner() {
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (dailyWorkPanel != null) {
				removeDailyWork();
			} else if (dailyCollection != null) {
				removeDailyCollection();
			}
			
		}
	}

	private class SwingAction extends AbstractAction {
		private JFrame parentContainer = null;
		public SwingAction(JFrame container) {
			putValue(NAME, "Daily Work");
			putValue(SHORT_DESCRIPTION, "This action opens new panel for daily work");
			this.parentContainer = container;
		}
		public void actionPerformed(ActionEvent e) {
			if (dailyCollection != null) {
				return;
			}
			if (dailyWorkPanel == null) {
				dailyWorkPanel = new DailyWorkPanel();
			}
			parentContainer.getContentPane().removeAll();
			parentContainer.getContentPane().add(dailyWorkPanel, BorderLayout.CENTER);
			dailyWorkPanel.addExitListner(exitListner);
			dailyWorkPanel.focusPatientCode();
			
			dailyWorkPanel.repaint();
			dailyWorkPanel.setVisible(true);
			parentContainer.getContentPane().repaint();
			parentContainer.repaint();
			//parentContainer.pack();
			setVisible(true);
		}
	}

	public DailyWorkPanel getDailyPanel() {
		return this.dailyWorkPanel;
	}

	private class DailyCollectionAction extends AbstractAction {
		private int type;
		private JFrame parentContainer = null;
		public DailyCollectionAction(JFrame parentContainer) {
			this(parentContainer, DailyCollectionTableModel.COLLECTION_TYPE);
		}
		public DailyCollectionAction(JFrame parentContainer, int type) {
			this.parentContainer = parentContainer;
			this.type = type;
			if (type == DailyCollectionTableModel.COLLECTION_TYPE) {
				putValue(NAME, "Daily Report");
				putValue(SHORT_DESCRIPTION, "Shows daily report of patient examination");
			} else if (type == DailyCollectionTableModel.DUES_TYPES) {
				putValue(NAME, "Dues");
				putValue(SHORT_DESCRIPTION, "Shows dues of patients");
			}
		}
		public void actionPerformed(ActionEvent e) {
			if (dailyWorkPanel != null) {
				return;
			}
			if (dailyCollection == null) {
				dailyCollection = new DailyCollectionPanel(type);
			}
			parentContainer.getContentPane().removeAll();
			parentContainer.getContentPane().add(dailyCollection, BorderLayout.CENTER);
			dailyCollection.addExitListener(exitListner);
			
			dailyCollection.repaint();
			dailyCollection.setVisible(true);
			parentContainer.getContentPane().repaint();
			parentContainer.repaint();
			setVisible(true);
		}
	}
}
