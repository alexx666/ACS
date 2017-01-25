package main;

import java.util.Scanner;

import main.utils.ProcessManager;

public class ACS {

	private Scanner sc; //TODO user arguments instead
	private ProcessManager pm;

	public ACS() {

		this.sc = new Scanner(System.in);
		this.pm = new ProcessManager();

		System.out.println();
		System.out.println("############################################");
		System.out.println("#         Alert Correlation System         #");
		System.out.println("############################################");
		manage(Options.HELP);
	}

	public static void main(String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println();
				System.out.println("############################################");
				System.out.println("#            Exiting : Bye Bye!            #");
				System.out.println("############################################");
				System.out.println();
				
				try { Thread.sleep(500); } 
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		}));
		
		(new ACS()).run();
	}

	private void run() {
		System.out.print("[acs]: ");

		switch (sc.next()) {
		case "1": manage(Options.NIDS); break;
		case "2": manage(Options.PROFILES); break;
		case "3": manage(Options.RULES); break;
		case "h": manage(Options.HELP); break;
		case "s": break;
		default: run(); break;
		}
		
		System.exit(1);
	}

	private void manage(Options option) {
		switch (option){
		case NIDS:
			AlertHandler ah = new AlertHandler();
			pm.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
			System.out.println("[acs] Inicializing tools...");
			pm.start(true);
			ah.start();
			if (!sc.next().contentEquals("s"));
			ah.stop();
			pm.stop(true);
			break;
		case PROFILES:
			pm.setProcesses(ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER);
			System.out.println("[acs] Inicializing tools...");
			pm.start(true);
			System.out.println("[acs] Profiling...");
			if (!sc.next().contentEquals("s"));
			pm.stop(true);
			break;
		case RULES:
			pm.setProcesses(ExternalProcess.OINKMASTER);
			System.out.println("[acs] Inicializing update...");
			pm.start(true);
			System.out.println("[acs] Update finnished.");
			break;
		case HELP:
			System.out.println();
			System.out.println("Select an option:");
			System.out.println();
			for (int i = 0; i < java.util.Arrays.asList(Options.values()).size(); i++) {
				System.out.println("	" + java.util.Arrays.asList(Options.values()).get(i));
			}
			System.out.println();
			break;
		}
		run();
	}
}