package main.java.com.alexx666.acs.db.dao.snapshot;

import java.util.Date;

import main.java.com.alexx666.acs.db.dto.traffic.Statistics;

public interface SnapshotDAO {
	public Statistics getSnapshot(Date time);
	public boolean isSnapshotReady(Date time);
}
