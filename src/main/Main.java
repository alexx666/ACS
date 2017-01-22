package main;

import java.util.Scanner;

import main.utils.ProcessManager;

public class Main {

	private Scanner sc;
	private AlertHandler ah;

	public Main() {

		this.sc = new Scanner(System.in);
		this.ah = new AlertHandler();

		System.out.println();
		System.out.println("############################################");
		System.out.println("#         Alert Correlation System         #");
		System.out.println("############################################");
		System.out.println();
		manage(Options.HELP);
		System.out.println();
		run();
	}

	public static void main(String[] args) {
		new Main();
	}

	private void run() {

		System.out.print("[acs-#]: ");

		switch (sc.next()) {
		case "1": manage(Options.NIDS); break;
		case "2": manage(Options.PROFILES); break;
		case "3": manage(Options.RULES); break;
		case "v": manage(Options.VERSIONS); break;
		case "f": manage(Options.FILES); break;
		case "s": break;
		case "h": manage(Options.HELP); break;
		default: manage(Options.HELP); break;
		}
		
		System.out.println();
		System.out.println("############################################");
		System.out.println("#                 Bye Bye!                 #");
		System.out.println("############################################");
		System.out.println();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit(1);
	}

	private void manage(Options option) {
		switch (option){
		case NIDS:
			ProcessManager.start(Commands.BARNYARD2, Commands.SURICATA);
			ah.start();
			System.out.println();
			if (!sc.next().contentEquals("s"));
			ah.stop();
			ProcessManager.stop(Commands.BARNYARD2, Commands.SURICATA);
			System.out.println("[acs-1] Monitor has been interrupted.");
			break;
		case PROFILES:
			ProcessManager.start(Commands.PROFILE2DB, Commands.CXTRACKER);
			System.out.print("[acs-2] Profiler is running. Insert any input to stop profiling: ");
			if (!sc.next().contentEquals("s"));
			ProcessManager.stop(Commands.CXTRACKER, Commands.PROFILE2DB);
			System.out.println("[acs-2] Profiler has been interrupted.");
			break;
		case RULES:
			System.out.println("[acs-3] Updating rules, please wait...");
			ProcessManager.start(Commands.OINKMASTER);
			System.out.println("[acs-3] Update finnished.");
			break;
		case VERSIONS:
			System.out.println("[acs-v] Listing tool versions, please wait...");
			ProcessManager.start(Commands.VERSION_SURICATA, Commands.VERSION_OINKMASTER, Commands.VERSION_BARNUARD2);
			break;
		case FILES:
			System.out.println("[acs-f] Listing configuration files, please wait...");
			ProcessManager.start(Commands.FILE_SURICATA, Commands.FILE_OINKMASTER, Commands.FILE_BARNUARD2);
			break;
		case HELP: 
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
