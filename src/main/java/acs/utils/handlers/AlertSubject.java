package main.java.acs.utils.handlers;

import java.util.ArrayList;

import main.java.acs.data.dto.Alert;

/**
 * 
 * @author alexx666
 *
 */
public class AlertSubject {
	
	private ArrayList<AlertObserver> observers;
	private Alert alert; //TODO create previous alert object
	
	public AlertSubject() {	this.observers = new ArrayList<AlertObserver>(); }
	
	public Alert getAlert() { return alert;	}
	
	public void setAlert(Alert alert) {	
		this.alert = alert; 
		notifyObservers(); //TODO notify only if new alert time is different from previous alert time
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
