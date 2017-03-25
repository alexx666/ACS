package main.java.acs.data.dao;

import main.java.acs.data.dto.Statistics;

public interface ProfileDAO {
	public Statistics getProfile();
	public Statistics getFullProfile();
	public boolean isProfileDataEnough();
}
