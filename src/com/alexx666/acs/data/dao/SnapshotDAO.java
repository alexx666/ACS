package com.alexx666.acs.data.dao;

import java.util.Date;

import com.alexx666.acs.data.dto.Statistics;

public interface SnapshotDAO {
	public Statistics getSnapshot(Date time);
	public boolean isSnapshotReady(Date time);
}
