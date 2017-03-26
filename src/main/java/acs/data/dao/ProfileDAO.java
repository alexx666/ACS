package main.java.acs.data.dao;

import main.java.acs.data.dto.Statistics;

public interface ProfileDAO {
	public Statistics getFullProfile();
	public Statistics getProfile(String hour);
	public boolean isProfileDataEnough(String hour);
}
