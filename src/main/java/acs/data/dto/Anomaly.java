package main.java.acs.data.dto;

import java.util.ArrayList;
import java.util.List;

import main.java.acs.utils.Calc;

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
		
		statList.add(Calc.stringListDiff(profile.getSrcIps(), snapshot.getSrcIps()));
		statList.add(Calc.stringListDiff(profile.getDstIps(), snapshot.getDstIps()));
		statList.add(Calc.intListDiff(profile.getIpProtos(), snapshot.getIpProtos()));
		statList.add(Calc.stringListDiff(profile.getSrcPorts(), snapshot.getSrcPorts()));
		statList.add(Calc.stringListDiff(profile.getDstPorts(), snapshot.getDstPorts()));
		statList.add(Calc.diff(profile.getAverageSrcPkts(), snapshot.getAverageSrcPkts()));
		statList.add(Calc.diff(profile.getAverageDstPkts(), snapshot.getAverageDstPkts()));
		statList.add(Calc.diff(profile.getAverageSrcBytes(), snapshot.getAverageSrcBytes()));
		statList.add(Calc.diff(profile.getAverageDstBytes(), snapshot.getAverageDstBytes()));
		statList.add(Calc.diff(profile.getAverageDuration(), snapshot.getAverageDuration()));
		
		return 10 * Calc.sum(statList);
	}
}
