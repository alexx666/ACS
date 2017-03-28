package main.java.acs.data.dao;

import java.util.Date;

import main.java.acs.data.dto.Statistics;

public interface ProfileDAO {
	public Statistics getFullProfile(Date hour);
	public Statistics getProfile(Date hour);
	public boolean isProfileDataEnough(Date hour);
}
