package main.java.acs.entities;

import java.util.ArrayList;
import java.util.List;

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
		
		statList.add(main.java.acs.utils.Calc.stringListDiff(profile.getSrcIps(), snapshot.getSrcIps()));
		statList.add(main.java.acs.utils.Calc.stringListDiff(profile.getDstIps(), snapshot.getDstIps()));
		statList.add(main.java.acs.utils.Calc.intListDiff(profile.getIpProtos(), snapshot.getIpProtos()));
		statList.add(main.java.acs.utils.Calc.stringListDiff(profile.getSrcPorts(), snapshot.getSrcPorts()));
		statList.add(main.java.acs.utils.Calc.stringListDiff(profile.getDstPorts(), snapshot.getDstPorts()));
		statList.add(main.java.acs.utils.Calc.diff(profile.getAverageSrcPkts(), snapshot.getAverageSrcPkts()));
		statList.add(main.java.acs.utils.Calc.diff(profile.getAverageDstPkts(), snapshot.getAverageDstPkts()));
		statList.add(main.java.acs.utils.Calc.diff(profile.getAverageSrcBytes(), snapshot.getAverageSrcBytes()));
		statList.add(main.java.acs.utils.Calc.diff(profile.getAverageDstBytes(), snapshot.getAverageDstBytes()));
		statList.add(main.java.acs.utils.Calc.diff(profile.getAverageDuration(), snapshot.getAverageDuration()));
		
		return 10 * main.java.acs.utils.Calc.sum(statList);
	}
}
