package com.alexx666.acs.runtime.handlers.subjects.impl;

import java.util.ArrayList;

import com.alexx666.acs.data.dto.alerts.Alert;
import com.alexx666.acs.runtime.handlers.observers.AlertObserver;
import com.alexx666.acs.runtime.handlers.subjects.AlertSubject;

/**
 * 
 * @author alexx666
 *
 */
public class SuricataAlertSubject implements AlertSubject {
	
	private ArrayList<AlertObserver> observers;
	private Alert alert; 
	
	public SuricataAlertSubject() {	this.observers = new ArrayList<AlertObserver>(); }
	
	@Override
	public Alert getAlert() { return alert;	}
	
	@Override
	public void setAlert(Alert alert) {	
		this.alert = alert; 
		notifyObservers();
	}

	@Override
	public void addObserver(AlertObserver newObserver) {
		observers.add(newObserver);
	}

	@Override
	public void notifyObservers() {
		for (AlertObserver observer : observers) {
			observer.update();
		}
	}
}
