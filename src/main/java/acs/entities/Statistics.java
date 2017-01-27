package main.java.acs.entities;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
	
	private List<Flow> connections;

	public Statistics(List<Flow> connections) {	this.connections = connections; }
		
	public int getNumberOfConnections() { return connections.size(); }

	public List<String> getSrcIps() {
		List<String> ips = new ArrayList<String>();
		for(Flow connection : connections) {
			if (!ips.contains(connection.getSrc_ip())) {
				ips.add(connection.getSrc_ip());
			}
		}	
		return ips;
	}
	
	public List<String> getDstIps() {
		List<String> ips = new ArrayList<String>();
		for(Flow connection : connections) {
			if (!ips.contains(connection.getDst_ip())) {
				ips.add(connection.getDst_ip());
			}
		}	
		return ips;
	}
	
	public List<String> getSrcPorts() {
		List<String> ports = new ArrayList<String>();
		for(Flow connection : connections) {
			if (!ports.contains(connection.getSrc_port())) {
				ports.add(connection.getSrc_port());
			}
		}	
		return ports;
	}
	
	public List<String> getDstPorts() {
		List<String> ports = new ArrayList<String>();
		for(Flow connection : connections) {
			if (!ports.contains(connection.getDst_port())) {
				ports.add(connection.getDst_port());
			}
		}	
		return ports;
	}
	
	public List<Integer> getIpProtos() {
		List<Integer> protos = new ArrayList<Integer>();
		for(Flow connection : connections) {
			if (!protos.contains(connection.getIp_proto())) {
				protos.add(connection.getIp_proto());
			}
		}	
		return protos;
	}

	public int getAverageSrcBytes() {
		int bytes = 0;
		for (Flow connection : connections) {
			bytes += connection.getSrc_bytes();
		}
		return bytes/connections.size();
	}
	
	public int getAverageDstBytes() {
		int bytes = 0;
		for (Flow connection : connections) {
			bytes += connection.getDst_bytes();
		}
		return bytes/connections.size();
	}
	
	public int getAverageSrcPkts() {
		int pkts = 0;
		for (Flow connection : connections) {
			pkts += connection.getSrc_pkts();
		}
		return pkts/connections.size();
	}
	
	public int getAverageDstPkts() {
		int pkts = 0;
		for (Flow connection : connections) {
			pkts += connection.getDst_pkts();
		}
		return pkts/connections.size();
	}
	
	public int getAverageDuration() {
		int dur = 0;
		for (Flow connection : connections) {
			dur += connection.getDuration();
		}
		return dur/connections.size();
	}
	
	public String flowsToString() {
		String result = "";
		for(Flow f : connections) {	result += f.flowToString() + "\n"; }
		return result;
	}
}