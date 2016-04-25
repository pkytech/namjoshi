package com.kytech.namjoshi;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

public class DailyWorkPanel extends JPanel {
	private RecordSelector recordSelector = null;
	private DailyWork dailyWork = null;
	
	public DailyWorkPanel() {
		setBackground(Color.BLUE);
		setLayout(new BorderLayout(0, 0));
		
		dailyWork = new DailyWork();
		add(dailyWork);
		
		recordSelector = new RecordSelector();
		dailyWork.add(recordSelector, BorderLayout.NORTH);
		
	}

	public void addExitListner(ActionListener exitListner) {
		recordSelector.addExitListner(exitListner);
	}
}