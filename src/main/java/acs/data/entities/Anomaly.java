package main.java.acs.data.entities;

import java.util.ArrayList;
import java.util.List;

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
		
		statList.add(main.java.acs.utils.calc.Calc.stringListDiff(profile.getSrcIps(), snapshot.getSrcIps()));
		statList.add(main.java.acs.utils.calc.Calc.stringListDiff(profile.getDstIps(), snapshot.getDstIps()));
		statList.add(main.java.acs.utils.calc.Calc.intListDiff(profile.getIpProtos(), snapshot.getIpProtos()));
		statList.add(main.java.acs.utils.calc.Calc.stringListDiff(profile.getSrcPorts(), snapshot.getSrcPorts()));
		statList.add(main.java.acs.utils.calc.Calc.stringListDiff(profile.getDstPorts(), snapshot.getDstPorts()));
		statList.add(main.java.acs.utils.calc.Calc.diff(profile.getAverageSrcPkts(), snapshot.getAverageSrcPkts()));
		statList.add(main.java.acs.utils.calc.Calc.diff(profile.getAverageDstPkts(), snapshot.getAverageDstPkts()));
		statList.add(main.java.acs.utils.calc.Calc.diff(profile.getAverageSrcBytes(), snapshot.getAverageSrcBytes()));
		statList.add(main.java.acs.utils.calc.Calc.diff(profile.getAverageDstBytes(), snapshot.getAverageDstBytes()));
		statList.add(main.java.acs.utils.calc.Calc.diff(profile.getAverageDuration(), snapshot.getAverageDuration()));
		
		return 10 * main.java.acs.utils.calc.Calc.sum(statList);
	}
}
