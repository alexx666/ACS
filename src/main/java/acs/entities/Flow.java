package main.java.acs.entities;

public class Flow {
	
	private String src_ip;
	private String dst_ip;
	private String src_port;
	private String dst_port;
	private int src_pkts;
	private int dst_pkts;
	private int src_bytes;
	private int dst_bytes;
	private int ip_proto;
	private int duration;

	public Flow(String src_ip, String dst_ip, String src_port, String dst_port, int src_pkts, int dst_pkts, int src_bytes, int dst_bytes, int ip_proto, int duration) {
		this.src_ip = src_ip;
		this.dst_ip = dst_ip;
		this.src_port = src_port;
		this.dst_port = dst_port;
		this.src_pkts = src_pkts;
		this.dst_pkts = dst_pkts;
		this.src_bytes = src_bytes;
		this.dst_bytes = dst_bytes;
		this.ip_proto = ip_proto;
		this.duration = duration;
	}	
	
	public String getSrc_ip() {	return src_ip; }
	public String getDst_ip() { return dst_ip; }
	public String getSrc_port() { return src_port; }
	public String getDst_port() { return dst_port;	}
	public int getSrc_pkts() { return src_pkts;	}
	public int getDst_pkts() { return dst_pkts; }
	public int getSrc_bytes() {	return src_bytes; }
	public int getDst_bytes() {	return dst_bytes;}
	public int getIp_proto() { return ip_proto; }
	public int getDuration() { return duration;	}

	public void setSrc_ip(String src_ip) { this.src_ip = src_ip; }
	public void setDst_ip(String dst_ip) { this.dst_ip = dst_ip; }
	public void setSrc_port(String src_port) {	this.src_port = src_port; }
	public void setDst_port(String dst_port) {	this.dst_port = dst_port; }
	public void setSrc_pkts(int src_pkts) { this.src_pkts = src_pkts; }
	public void setDst_pkts(int dst_pkts) { this.dst_pkts = dst_pkts; }
	public void setSrc_bytes(int src_bytes) { this.src_bytes = src_bytes; }
	public void setDst_bytes(int dst_bytes) { this.dst_bytes = dst_bytes; }
	public void setIp_proto(int ip_proto) {	this.ip_proto = ip_proto; }
	public void setDuration(int duration) {	this.duration = duration; }
	
	public String flowToString() { 
		return "Flow -> ip origen: " + src_ip + ", ip destino: " + dst_ip + ", puerto origen: " + src_port + ", puerto destino: ";
	}
}