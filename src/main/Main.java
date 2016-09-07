package main;

import java.util.Scanner;

import main.controllers.AlertHandler;
import main.controllers.ConfigController;
import main.controllers.CxtrackerController;
import main.controllers.OinkmasterController;
import main.controllers.SuricataController;

public class Main {

	private Scanner sc;
	private CxtrackerController cxt;
	private SuricataController nidsc;
	private AlertHandler ah;
	private OinkmasterController r;
	private ConfigController c;

	public Main() {

		this.sc = new Scanner(System.in);
		this.cxt = new CxtrackerController();
		this.nidsc = new SuricataController();
		this.ah = new AlertHandler();
		this.r = new OinkmasterController();
		this.c = new ConfigController();

		System.out.println();
		System.out.println("############################################");
		System.out.println("#         Alert Correlation System         #");
		System.out.println("############################################");
		System.out.println();
		System.out.println("Select an option:");
		System.out.println();
		showHelp();
		run();
	}

	public static void main(String[] args) {
		new Main();
	}

	private void run() {

		System.out.println();
		System.out.print("[acs-#]: ");

		switch (sc.next()) {
		case "1": // Monitor mode
			manageMonitor();
			break;
		case "2": // Profiler mode
			manageProfiler();
			break;
		case "3": // Rules update
			manageRules();
			break;
		case "v": // Show tool version
			showTools();
			break;
		case "f": // Show file locations
			showFiles();
			break;
		case "s": // Exit
			break;
		case "h": // Help
			System.out.println();
			System.out.println("   ACS help:");
			showHelp();
		default: // Show error and help
			System.out.println();
			System.out.println("   Invalid option! Please use the following:");
			showHelp();
			break;
		}

		System.exit(1);
	}

	private void manageMonitor() {
		nidsc.start();
		ah.start();
		System.out.println();
		if (!sc.next().contentEquals("s"));
		ah.stop();
		nidsc.stop();
		System.out.println("[acs-1] Monitor has been interrupted.");
		run();
	}

	private void manageProfiler() {
		cxt.start();
		System.out.println();
		System.out.print("[acs-2] Profiler is running. Insert any input to stop profiling: ");
		if (!sc.next().contentEquals("s"));
		cxt.stop();
		System.out.println("[acs-2] Profiler has been interrupted.");
		run();
	}

	private void manageRules() {
		System.out.println("[acs-3] Updating rules, please wait...");
		r.start();
		System.out.println("[acs-3] Update finnished.");
		run();
	}

	private void showTools() {
		c.showToolVersions();
		System.out.println("[acs-v] Listing configuration tools, please wait...");
		c.start();
		run();
	}

	private void showFiles() {
		c.showFileLocations();
		System.out.println("[acs-f] Listing configuration files, please wait...");
		c.start();
		run();
	}

	private void showHelp() {
		System.out.println("     1 - to start anomaly monitoring");
		System.out.println("     2 - to start network profiling");
		System.out.println("     3 - to update Suricata rules");
		System.out.println("     v - to see the tools used");
		System.out.println("     f - to see configuration file locations");
		System.out.println("     s - to stop ACS");
		System.out.println("     h - to display this message");
		run();
	}
}
