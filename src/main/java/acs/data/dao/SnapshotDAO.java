package main.java.acs.data.dao;

import java.util.Date;

import main.java.acs.data.dto.Statistics;

public interface SnapshotDAO {
	public Statistics getSnapshot(Date time);
	public boolean isSnapshotReady(Date time);
}
