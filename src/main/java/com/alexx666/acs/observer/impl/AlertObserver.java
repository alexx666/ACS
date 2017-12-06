package com.alexx666.acs.observer.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.alexx666.acs.configuration.ConfigurationFactory;
import com.alexx666.acs.db.dao.profile.ProfileDAO;
import com.alexx666.acs.db.dao.snapshot.SnapshotDAO;
import com.alexx666.acs.db.dto.alerts.Alert;
import com.alexx666.acs.db.dto.traffic.Anomaly;
import com.alexx666.acs.db.dto.traffic.Statistics;
import com.alexx666.acs.db.factory.DAOFactory;
import com.alexx666.acs.observer.Observer;
import com.alexx666.acs.subject.Subject;

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
	private Anomaly oldAnomaly;
	private ProfileDAO profileDao;
	private SnapshotDAO snapshotDao; 
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertObserver(Subject subject) {
		this.subject = subject;
		this.subject.addObserver(this);
		this.profileDao = DAOFactory.getDAOFactory(ConfigurationFactory.getInstance().getSettings().getTrackers().getType()).getProfileDAO();
		this.snapshotDao= DAOFactory.getDAOFactory(ConfigurationFactory.getInstance().getSettings().getTrackers().getType()).getSnapshotDAO();
		this.profile = profileDao.getFullProfile(new Date());
		this.snapshot = profile;
		this.oldAnomaly = new Anomaly(profile, snapshot);
		
		try {
			String dumpFile = ConfigurationFactory.getInstance().getSettings().getOutputs().getFile() + 
					"/acs_" + Calendar.getInstance().getTimeInMillis() + ".log";
			FileHandler fh = new FileHandler(dumpFile, ConfigurationFactory.getInstance().getSettings().getOutputs().shouldAppend());
			fh.setFormatter(FORMATTER);
			LOGGER.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (oldAlert == null || subject.getAlert().getHour().toString() != oldAlert.getHour().toString()) {
		
			if (profileDao.isProfileDataEnough(subject.getAlert().getHour())) {	
				profile = profileDao.getProfile(subject.getAlert().getHour()); 
			}
			if (snapshotDao.isSnapshotReady(subject.getAlert().getHour())) { 
				snapshot = snapshotDao.getSnapshot(subject.getAlert().getHour()); 
			}
				
			oldAnomaly = new Anomaly(profile, snapshot);		
		}
		
		LOGGER.warning(subject.getAlert().getMessage() 
					+ " ---> Network ANOMALY of: " 
					+ Math.round(oldAnomaly.getAnomaly()) 
					+ "/100");
		
		oldAlert = subject.getAlert();
	}
}
