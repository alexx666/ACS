package com.alexx666.acs.data.dto.traffic;

import java.util.ArrayList;
import java.util.List;

import com.alexx666.acs.utils.calc.StatDiff;

/**
 * 
 * @author alexx666
 *
 */
public class Anomaly {
	
	private Statistics profile;
	private Statistics snapshot;
	
	public Anomaly(Statistics profile, Statistics snapshot) {
		this.profile = profile;
		this.snapshot = snapshot;
	}

	public void setProfile(Statistics profile) { this.profile = profile; }
	public void setSnapshot(Statistics snapshot) { this.snapshot = snapshot; }

	public double getAnomaly() {
		
		List<Double> statList = new ArrayList<Double>();
		
		statList.add(StatDiff.stringListDiff(profile.getSrcIps(), snapshot.getSrcIps()));
		statList.add(StatDiff.stringListDiff(profile.getDstIps(), snapshot.getDstIps()));
		statList.add(StatDiff.intListDiff(profile.getIpProtos(), snapshot.getIpProtos()));
		statList.add(StatDiff.stringListDiff(profile.getSrcPorts(), snapshot.getSrcPorts()));
		statList.add(StatDiff.stringListDiff(profile.getDstPorts(), snapshot.getDstPorts()));
		statList.add(StatDiff.diff(profile.getAverageSrcPkts(), snapshot.getAverageSrcPkts()));
		statList.add(StatDiff.diff(profile.getAverageDstPkts(), snapshot.getAverageDstPkts()));
		statList.add(StatDiff.diff(profile.getAverageSrcBytes(), snapshot.getAverageSrcBytes()));
		statList.add(StatDiff.diff(profile.getAverageDstBytes(), snapshot.getAverageDstBytes()));
		statList.add(StatDiff.diff(profile.getAverageDuration(), snapshot.getAverageDuration()));
		
		return 10 * StatDiff.sum(statList);
	}
}
