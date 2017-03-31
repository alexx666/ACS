package com.alexx666.acs.runtime.handlers;

import java.util.ArrayList;

import com.alexx666.acs.data.dto.Alert;

/**
 * 
 * @author alexx666
 *
 */
public class AlertSubject {
	
	private ArrayList<AlertObserver> observers;
	private Alert alert; 
	
	public AlertSubject() {	this.observers = new ArrayList<AlertObserver>(); }
	
	public Alert getAlert() { return alert;	}
	
	public void setAlert(Alert alert) {	
		this.alert = alert; 
		notifyObservers();
	}

	public void addObserver(AlertObserver newObserver) {
		observers.add(newObserver);
	}

	public void notifyObservers() {
		for (AlertObserver observer : observers) {
			observer.update();
		}
	}
}
