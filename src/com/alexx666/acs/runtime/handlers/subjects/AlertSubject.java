package com.alexx666.acs.runtime.handlers.subjects;

import com.alexx666.acs.data.dto.alerts.Alert;
import com.alexx666.acs.runtime.handlers.observers.AlertObserver;

public interface AlertSubject {
	public void addObserver(AlertObserver observer);
	public void setAlert(Alert alert);
	public Alert getAlert();
	public void notifyObservers();
}
