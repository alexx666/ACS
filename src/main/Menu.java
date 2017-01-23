package main;

import java.util.Scanner;

import main.utils.ProcessManager;

public class Menu {

	private Scanner sc;
	private AlertHandler ah;

	public Menu() {

		this.sc = new Scanner(System.in);
		sc.useDelimiter("[;\r\n]+");
		this.ah = new AlertHandler();

		System.out.println();
		System.out.println("############################################");
		System.out.println("#         Alert Correlation System         #");
		System.out.println("############################################");
		manage(Options.HELP);
		run();
	}

	public static void main(String[] args) {
		new Menu();
	}

	private void run() {
		System.out.print("[acs]: ");

		switch (sc.next()) {
		case "1": manage(Options.NIDS); break;
		case "2": manage(Options.PROFILES); break;
		case "3": manage(Options.RULES); break;
		case "v": manage(Options.VERSIONS); break;
		case "f": manage(Options.FILES); break;
		case "h": manage(Options.HELP); break;
		case "s": break;
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
			ah.start();
			if (!sc.next().contentEquals("s"));
			ah.stop();
			break;
		case PROFILES:
			ProcessManager.start(Commands.PROFILE2DB, Commands.CXTRACKER);
			System.out.print("[acs] Profiler is running. Insert any input to stop profiling: ");
			if (!sc.next().contentEquals("s"));
			ProcessManager.stop(Commands.CXTRACKER, Commands.PROFILE2DB);
			System.out.println("[acs] Profiler has been interrupted.");
			break;
		case RULES:
			System.out.println("[acs] Updating rules, please wait...");
			ProcessManager.start(Commands.OINKMASTER);
			System.out.println("[acs] Update finnished.");
			break;
		case VERSIONS:
			System.out.println("[acs] Listing tool versions, please wait...");
			ProcessManager.start(Commands.VERSION_SURICATA, Commands.VERSION_OINKMASTER);
			break;
		case FILES:
			System.out.println("[acs] Listing configuration files, please wait...");
			ProcessManager.start(Commands.FILE_SURICATA, Commands.FILE_OINKMASTER);
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
