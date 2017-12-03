package com.alexx666.acs.subject;

import com.alexx666.acs.db.dto.alerts.Alert;
import com.alexx666.acs.observer.Observer;

public interface Subject {
	public void addObserver(Observer observer);
	public void setAlert(Alert alert);
	public Alert getAlert();
	public void notifyObservers();
}
