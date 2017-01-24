package main;

/**
 * This enum defines a runnable command needed for ACS and its components to work
 * 
 * Format of the commands:
 * 
 * 		COMMAND (name - String, command - String, process - boolean, print - boolean, output - boolean)
 * 		
 * NOTE: process - indicates that a process is created after execution
 * 		 print - true if you want the output of the command to be printed on console
 * 		 output - true if you want the output to be saved for later use or for other processes
 * */
public enum Commands {
	/* RUNNABLE PROCESSES */
	//BARNYARD2 ("barnyard2", "sudo -S barnyard2 -c /etc/suricata/barnyard2.conf -d /var/log/suricata -f unified2.alert -w /var/log/suricata/suricata.waldo -D", true, false, false),
	PROFILE2DB ("profile2db.pl", "sudo -S profile2db.pl eth2 --dir /var/log/cxtracker/eth2/sessions/ --daemon", true),
	CXTRACKER ("cxtracker", "sudo -S cxtracker -i eth2 -d /var/log/cxtracker/eth2/sessions/ -D", true),
	OINKMASTER ("oinkmaster", "sudo -S oinkmaster -o /etc/suricata/rules -s", true),
	SNAPSHOT2DB ("snapshot2db.pl", "sudo -S snapshot2db.pl eth2 --dir /var/log/prads/eth2/sessions/ --daemon", true),
	PRADS ("prads", "sudo -S prads -i eth2 -L /var/log/prads/eth2/sessions/ -x -O -D", true),
	SURICATA ("Suricata-Main", "sudo -S suricata -c /etc/suricata/suricata.yaml --pcap=eth1 -D", true),
	/* CONFIG FILE LOCATIONS */
	//FILE_BARNUARD2 ("barnyard2", "locate 'barnyard2.conf'", false),
	FILE_SURICATA ("suricata", "locate 'suricata.yaml'", false),
	FILE_OINKMASTER ("oinkmaster", "locate 'oinkmaster.conf'", false),
	/* TOOL VERSIONS */
	//VERSION_BARNUARD2 ("barnyard2", "barnyard2 -V", false),
	VERSION_SURICATA ("suricata", "suricata -V", false),
	VERSION_OINKMASTER ("oinkmaster", "oinkmaster -V", false);
	
    private final String name;   
    private final String command;
    private final boolean process;
    
    Commands(String name, String command, boolean process) {
        this.name = name;
        this.command = command;
        this.process = process;
    }
    
    public String getName() { return name; }
    public String getCommand() { return command; }
    public boolean isProcess() { return process; }
}
