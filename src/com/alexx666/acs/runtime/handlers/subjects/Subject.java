package com.alexx666.acs.runtime.handlers.subjects;

import com.alexx666.acs.data.dto.alerts.Alert;
import com.alexx666.acs.runtime.handlers.observers.Observer;

public interface Subject {
	public void addObserver(Observer observer);
	public void setAlert(Alert alert);
	public Alert getAlert();
	public void notifyObservers();
}
