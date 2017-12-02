package com.alexx666.acs.db.dao.profile;

import java.util.Date;

import com.alexx666.acs.db.dto.traffic.Statistics;

public interface ProfileDAO {
	public Statistics getFullProfile(Date hour);
	public Statistics getProfile(Date hour);
	public boolean isProfileDataEnough(Date hour);
}
