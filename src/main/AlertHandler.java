package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import main.dao.ProfileDao;
import main.dao.SnapshotDao;
import main.entities.Alert;
import main.entities.Anomaly;
import main.entities.Statistics;
import main.utils.Task;

public class AlertHandler extends Task {

	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private static final String FIFO = "/var/log/suricata/fast.pipe"; //TODO create pipe here!

	private volatile boolean running;
			
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertHandler() {
		super();
	}
	
	@Override
	public void async() {
		
		running = true;
		
		ProfileDao profileDao = new ProfileDao("cxtracker","cxtracker","cxtracker");
		SnapshotDao snapshotDao = new SnapshotDao("cxtracker","cxtracker","cxtracker");
		
		System.out.println("[acs] Listening...");
						
		try {
			
			Commands [] cmds = new Commands[]{Commands.PRADS};
			BufferedReader in = new BufferedReader(new FileReader(FIFO));
			
			while (running) {
				
				String line;
				
				if ((line = in.readLine()) != null) {
					
					int counter = 0;
					
					Alert alert = new Alert(line);
					
					System.out.println();
					System.out.print("[acs] Alert recieved: " + alert.getMessage() + " at: [UTC] " + alert.getDate());
					
					sleep(100); //TODO give prads a little time to save connections ??
					
					ProcessManager.stop(cmds, false);
					
					/* EXTRACT PROFILE DATA */
					if (profileDao.isProfileDataEnough()) {
						profile = profileDao.getProfile();
					}else{
						System.out.print("[P]");
						profile = profileDao.getFullProfile();
					}
					
					/* WAIT FOR SNAPSHOT */
					while(!snapshotDao.isSnapshotReady(alert.getDate()) && running) {
						if (counter==20 && snapshot != null) {
							System.out.print("[S]");
							break;
						}
						sleep(50);
						counter++;
					}
					
					if(!running) {break;}
					
					/* EXTRACT SNAPSHOT DATA */
					snapshot = snapshotDao.getSnapshot(alert.getDate());
					
					System.out.print(" ---> Network ANOMALY of: " + Math.round((new Anomaly(profile, snapshot)).getAnomaly()) + "/100");
	
					ProcessManager.start(cmds, false);
				}
			}
			
			in.close();
		}catch (IOException ex) {
			LOGGER.warning(ex.getMessage());
			System.exit(-1);
		}
	}
	
	@Override
	public void sync(Object lock) {}
	
	@Override
	public void stop() {
		running = false;
	}
}
