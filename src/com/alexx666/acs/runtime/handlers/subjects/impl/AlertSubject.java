package com.alexx666.acs.runtime.handlers.subjects.impl;

import java.util.ArrayList;

import com.alexx666.acs.data.dto.alerts.Alert;
import com.alexx666.acs.runtime.handlers.observers.Observer;
import com.alexx666.acs.runtime.handlers.subjects.Subject;

/**
 * 
 * @author alexx666
 *
 */
public class AlertSubject implements Subject {
	
	private ArrayList<Observer> observers;
	private Alert alert; 
	
	public AlertSubject() {	this.observers = new ArrayList<Observer>(); }
	
	@Override
	public Alert getAlert() { return alert;	}
	
	@Override
	public void setAlert(Alert alert) {	
		this.alert = alert; 
		notifyObservers();
	}

	@Override
	public void addObserver(Observer newObserver) {
		observers.add(newObserver);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update();
		}
	}
}
