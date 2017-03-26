package main.java.acs.data.dao;

import main.java.acs.data.dto.Statistics;

public interface SnapshotDAO {
	public Statistics getSnapshot(String time);
	public boolean isSnapshotReady(String time);
}
