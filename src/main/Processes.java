package main;

public enum Processes {
	
	CXTRACKER2DB ("cxtracker2db.pl", "sudo -S cxtracker2db.pl eth2 --dir /var/log/cxtracker/eth2/sessions/ --daemon", false, true, true),
	CXTRACKER ("cxtracker", "sudo -S cxtracker -i eth2 -d /var/log/cxtracker/eth2/sessions/ -D", false, true, true),
	OINKMASTER ("oinkmaster", "sudo -S oinkmaster -o /etc/suricata/rules -s", false, true, true),
	SNAPSHOT2DB ("snapshot2db.pl", "sudo -S snapshot2db.pl eth2 --dir /var/log/prads/eth2/sessions/ --daemon", false, true, true),
	PRADS ("prads", "sudo -S prads -i eth2 -L /var/log/prads/eth2/sessions/ -q -D", false, true, true),
	SURICATA ("Suricata-Main", "sudo -S suricata -c /etc/suricata/suricata.yaml --pcap=eth1 -D", false, true, true),
	BARNYARD2 ("barnyard2", "sudo -S barnyard2 -c /etc/suricata/barnyard2.conf -d /var/log/suricata -f unified2.alert -w /var/log/suricata/suricata.waldo -D", true, false, false),
	FILES ("files", "locate 'barnyard2.conf' && locate 'suricata.yaml' && locate 'oinkmaster.conf'", true, true, true),
	VERSIONS ("versions", "suricata -V && oinkmaster -V && barnyard2 -V", true, true, true);
	
    private final String name;   
    private final String command;
    private final boolean solo;
    private final boolean print;
    private final boolean output;
    
    Processes(String name, String command, boolean solo, boolean print, boolean output) {
        this.name = name;
        this.command = command;
        this.solo = solo;
        this.print = print;
        this.output = output;
    }
    
    public String getName() { return name; }
    public String getCommand() { return command; }
    public boolean execSolo() { return solo; }
    public boolean shouldPrint() { return print; }
    public boolean waitoutput() { return output; }
}
