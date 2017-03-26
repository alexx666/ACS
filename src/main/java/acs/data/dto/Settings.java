package main.java.acs.data.dto;

public class Settings {
	private Suricata suricata;
	private Trackers trackers;
	private Outputs outputs;
	
	public Suricata getSuricata() {
		return suricata;
	}
	public void setSuricata(Suricata suricata) {
		this.suricata = suricata;
	}
	public Trackers getTrackers() {
		return trackers;
	}
	public void setTrackers(Trackers trackers) {
		this.trackers = trackers;
	}
	public Outputs getOutputs() {
		return outputs;
	}
	public void setOutputs(Outputs outputs) {
		this.outputs = outputs;
	}
}