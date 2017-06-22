package com.alexx666.acs.runtime.handlers.observers.impl;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.alexx666.acs.data.dao.ProfileDAO;
import com.alexx666.acs.data.dao.SnapshotDAO;
import com.alexx666.acs.data.dao.factory.DAOFactory;
import com.alexx666.acs.data.dto.alerts.Alert;
import com.alexx666.acs.data.dto.config.ExternalProcess;
import com.alexx666.acs.data.dto.traffic.Anomaly;
import com.alexx666.acs.data.dto.traffic.Statistics;
import com.alexx666.acs.runtime.handlers.observers.Observer;
import com.alexx666.acs.runtime.handlers.subjects.Subject;
import com.alexx666.acs.runtime.managers.impl.ProcessManager;

/**
 * 
 * @author alexx666
 *
 */
public class AlertObserver implements Observer {
	
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private static final SimpleFormatter FORMATTER = new SimpleFormatter();
	
	private Subject subject;
	private Alert oldAlert;
	private ExternalProcess process;
	private Anomaly oldAnomaly;
	private ProfileDAO profileDao;
	private SnapshotDAO snapshotDao; 
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertObserver(Subject subject, ExternalProcess process, String dumpFile, boolean append, String dataSource) {
		this.subject = subject;
		this.subject.addObserver(this);
		this.profileDao = DAOFactory.getDAOFactory(dataSource).getProfileDAO();
		this.snapshotDao= DAOFactory.getDAOFactory(dataSource).getSnapshotDAO();
		this.profile = profileDao.getFullProfile(new Date());
		this.snapshot = profile;
		this.oldAnomaly = new Anomaly(profile, snapshot);
		this.process = process;
		
		try {
			FileHandler fh = new FileHandler(dumpFile, append);
			fh.setFormatter(FORMATTER);
			LOGGER.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (oldAlert == null || subject.getAlert().getHour().toString() != oldAlert.getHour().toString()) {
			ProcessManager.destroy(process, false);
			
			if (profileDao.isProfileDataEnough(subject.getAlert().getHour())) {	
				profile = profileDao.getProfile(subject.getAlert().getHour()); 
			}
			if (snapshotDao.isSnapshotReady(subject.getAlert().getHour())) { 
				snapshot = snapshotDao.getSnapshot(subject.getAlert().getHour()); 
			}
				
			oldAnomaly = new Anomaly(profile, snapshot);
								
			ProcessManager.create(process, false);
			
		}
		
		LOGGER.warning(subject.getAlert().getMessage() 
					+ " ---> Network ANOMALY of: " 
					+ Math.round(oldAnomaly.getAnomaly()) 
					+ "/100");
		
		oldAlert = subject.getAlert();
	}
}
