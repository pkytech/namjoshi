package com.kytech.namjoshi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

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

	public RecordSelector getRecordSelector() {
		return this.recordSelector;
	}
	
	public DailyWork getDailyWork() {
		return this.dailyWork;
	}
}
