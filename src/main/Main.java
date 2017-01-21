package main;

import java.util.Scanner;

import main.controllers.AlertHandler;
import main.controllers.ConfigController;
import main.utils.ProcessManager;

public class Main {

	private Scanner sc;
	private AlertHandler ah;
	private ConfigController c;

	public Main() {

		this.sc = new Scanner(System.in);
		this.ah = new AlertHandler();
		this.c = new ConfigController();

		System.out.println();
		System.out.println("############################################");
		System.out.println("#         Alert Correlation System         #");
		System.out.println("############################################");
		System.out.println();
		System.out.println("Select an option:");
		System.out.println();
		for (int i = 0; i < java.util.Arrays.asList(Options.values()).size(); i++) {
			System.out.println("	" + java.util.Arrays.asList(Options.values()).get(i));
		}
		run();
	}

	public static void main(String[] args) {
		new Main();
	}

	private void run() {

		System.out.println();
		System.out.print("[acs-#]: ");

		switch (sc.next()) {
		case "1": manage(Options.NIDS);
		case "2": manage(Options.PROFILES);
		case "3": manage(Options.RULES);
		case "v": manage(Options.VERSIONS);	
		case "f": manage(Options.FILES);
		case "s": break;
		case "h": manage(Options.HELP);
		default: // Show error and help
			System.out.println();
			System.out.println("   Invalid option! Please use the following:");
			manage(Options.HELP);
			break;
		}

		System.exit(1);
	}

	private void manage(Options option) {
		switch (option){
		case NIDS:
			ProcessManager.start(Processes.SURICATA, Processes.BARNYARD2);
			ah.start();
			System.out.println();
			if (!sc.next().contentEquals("s"));
			ah.stop();
			ProcessManager.stop(Processes.SURICATA, Processes.BARNYARD2);
			System.out.println("[acs-1] Monitor has been interrupted.");
			break;
		case PROFILES:
			ProcessManager.start(Processes.CXTRACKER2DB, Processes.CXTRACKER);
			System.out.println();
			System.out.print("[acs-2] Profiler is running. Insert any input to stop profiling: ");
			if (!sc.next().contentEquals("s"));
			ProcessManager.stop(Processes.CXTRACKER2DB, Processes.CXTRACKER);
			System.out.println("[acs-2] Profiler has been interrupted.");
			break;
		case RULES:
			System.out.println("[acs-3] Updating rules, please wait...");
			ProcessManager.start(Processes.OINKMASTER);
			System.out.println("[acs-3] Update finnished.");
			break;
		case VERSIONS:
			System.out.println("[acs-v] Listing tool versions, please wait...");
			System.out.println();
			c.showToolVersions();
			break;
		case FILES:
			System.out.println("[acs-f] Listing configuration files, please wait...");
			System.out.println();
			c.showFileLocations();
			break;
		case HELP: 
			System.out.println();
			for (int i = 0; i < java.util.Arrays.asList(Options.values()).size(); i++) {
				System.out.println("	" + java.util.Arrays.asList(Options.values()).get(i));
			}
			break;
		}
		run();
	}
}
