package main.java.com.alexx666.acs.subject.impl;

import java.util.ArrayList;

import main.java.com.alexx666.acs.db.dto.alerts.Alert;
import main.java.com.alexx666.acs.observer.Observer;
import main.java.com.alexx666.acs.subject.Subject;

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
