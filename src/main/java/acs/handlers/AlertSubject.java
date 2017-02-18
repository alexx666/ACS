package main.java.acs.handlers;

import java.util.ArrayList;

import main.java.acs.entities.Alert;

public class AlertSubject {
	
	private ArrayList<Observer> observers;
	private Alert alert;
	
	public AlertSubject() {	this.observers = new ArrayList<Observer>();	}
	
	public Alert getAlert() { return alert;	}
	
	public void setAlert(Alert alert) {	
		this.alert = alert; 
		notifyObservers();
	}

	public void addObserver(Observer newObserver) {
		observers.add(newObserver);
	}

	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update();
		}
	}
	
}
