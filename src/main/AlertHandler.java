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
import main.utils.DataBaseConnection;
import main.utils.ProcessManager;
import main.utils.RunnableTask;
import main.utils.Task;

public class AlertHandler extends Task implements RunnableTask {

	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private static final String FIFO = "/var/log/suricata/fast.pipe";

	private volatile boolean running;
	
	private BufferedReader in;	
	
	private DataBaseConnection dbCon;
	private ProfileDao profileDao;
	private SnapshotDao snapshotDao;
	
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertHandler() {
		this.dbCon = new DataBaseConnection("cxtracker", "cxtracker", "cxtracker");
	}
	
	public void run() { async(); }

	@Override
	public void async() {
		
		running = true;
		
		this.dbCon.connect();
		this.profileDao = new ProfileDao(dbCon.getConn());
		this.snapshotDao = new SnapshotDao(dbCon.getConn());
		
		ProcessManager.start(Commands.SURICATA, Commands.SNAPSHOT2DB, Commands.PRADS);
		
		System.out.println("[acs] Listening...");
						
		try {
			
			in = new BufferedReader(new FileReader(FIFO));
			
			while (running) {
				
				String line;
				
				if ((line = in.readLine()) != null) {
					
					int counter = 0;
					
					Alert alert = new Alert(line);
					
					System.out.print("[acs][1] Alert recieved: " + alert.getMessage() + " at: [UTC] " + alert.getDate());
					
					sleep(100); //give prads a little time to save connections
					
					ProcessManager.start(Commands.PRADS);
					
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
	
					ProcessManager.start(Commands.PRADS);
				}
			}
		}catch (IOException ex) {
			LOGGER.warning(ex.getMessage());
			System.exit(-1);
		}finally{
			dbCon.close();
		}
	}

	@Override
	public void sync(Object lock) {/* ignored */}

	public void stop() {
		running = false;
		ProcessManager.stop(Commands.SURICATA, Commands.SNAPSHOT2DB, Commands.PRADS);
		System.out.println("[acs] Monitor has been interrupted.");
	}
}
