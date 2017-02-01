package main.java.acs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public class ACSConfiguration {
	
	private enum ExternalProcess {
		
		PROFILE2DB ("profile2DB", "profile2db.pl"),
		SNAPSHOT2DB ("snapshot2DB", "snapshot2db.pl"),
		CXTRACKER ("cxtracker", "cxtracker"),
		PRADS ("prads", "prads"),
		OINKMASTER ("oinkmaster", "oinkmaster"),
		SURICATA ("suricata", "Suricata-Main");
		
		private final String alias;
	    private final String name;   
	    
	    ExternalProcess(String alias, String name) {
	    	this.alias = alias;
	        this.name = name;
	    }
	    
	    @SuppressWarnings("unused")
		public String getAlias() { return alias; }
		public String getName() { return name; }
	}
	
	private String yaml;
	private EnumMap<ExternalProcess, String> emp;
	
	public ACSConfiguration(String yaml) {
		this.yaml = yaml;
		this.emp = new EnumMap<ExternalProcess, String>(ExternalProcess.class);
	}
	
	public EnumMap<ExternalProcess, String> getEmp() { return emp; }

	@SuppressWarnings("rawtypes")
	public void init() {
		try {
			YamlReader reader = new YamlReader(new FileReader(yaml));
			Object object = reader.read();
			Map map = (Map)object;
			
			String sniffinet = (String) map.get("sniffers-interface"); //eth2
			String snifflogfolder = (String) map.get("sniffers-logfolder"); // /var/log/
			String surinet = (String) map.get("suricata-interface"); //eth1
			String surirules = (String) map.get("suricata-rules"); // /etc/suricata/rules
			String suriyaml = (String) map.get("suricata-yaml"); // /etc/suricata/suricata.yaml
			
			emp.put(ExternalProcess.PROFILE2DB, ExternalProcess.PROFILE2DB.getName() + " " + sniffinet + " --dir " + snifflogfolder + "/cxtracker/"+ sniffinet +"/sessions/ --daemon");
			emp.put(ExternalProcess.SNAPSHOT2DB, ExternalProcess.SNAPSHOT2DB.getName() + " " + sniffinet + " --dir " + snifflogfolder + "/prads/"+ sniffinet +"/sessions/ --daemon");
			emp.put(ExternalProcess.CXTRACKER, ExternalProcess.CXTRACKER.getName() + " -i " + sniffinet + " -d " + snifflogfolder + "/cxtracker/"+ sniffinet +"/sessions/ -D");
			emp.put(ExternalProcess.PRADS, ExternalProcess.PRADS.getName() + " -i " + sniffinet + " -L " + snifflogfolder + "/prads/"+ sniffinet +"/sessions/ -x -O -D");
			emp.put(ExternalProcess.OINKMASTER, ExternalProcess.OINKMASTER.getName() + " -o " + surirules + " -s");
			emp.put(ExternalProcess.SURICATA, ExternalProcess.SURICATA.getName() + " -c " + suriyaml + " --pcap=" + surinet + " -D");
			
			System.out.println(emp.get(ExternalProcess.PROFILE2DB));
			System.out.println(emp.get(ExternalProcess.SNAPSHOT2DB));
			System.out.println(emp.get(ExternalProcess.CXTRACKER));
			System.out.println(emp.get(ExternalProcess.PRADS));
			System.out.println(emp.get(ExternalProcess.OINKMASTER));
			System.out.println(emp.get(ExternalProcess.SURICATA));
			
		} catch (FileNotFoundException | YamlException e) {
			e.printStackTrace();
		}
	}
}
