package main;

import java.util.Scanner;


public class Menu {

	private Scanner sc; //TODO user arguments instead

	public Menu() {

		this.sc = new Scanner(System.in);

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
		System.out.println("#            Exiting : Bye Bye!            #");
		System.out.println("############################################");
		System.out.println();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit(1);
	}

	private void manage(Options option) {
		Commands [] cmds;
		switch (option){
		case NIDS:
			cmds = new Commands[]{Commands.SURICATA, Commands.SNAPSHOT2DB, Commands.PRADS};
			AlertHandler ah = new AlertHandler();
			ProcessManager.start(cmds, true);
			ah.start();
			if (!sc.next().contentEquals("s"));
			ah.stop();
			ProcessManager.stop(cmds, true);
			break;
		case PROFILES:
			cmds = new Commands[]{Commands.PROFILE2DB, Commands.CXTRACKER};
			ProcessManager.start(cmds, true);
			if (!sc.next().contentEquals("s"));
			ProcessManager.stop(cmds, true);
			break;
		case RULES:
			cmds = new Commands[]{Commands.OINKMASTER};
			System.out.println("[acs] Updating rules, please wait...");
			ProcessManager.start(cmds, true);
			System.out.println("[acs] Update finnished.");
			break;
		case VERSIONS:
			cmds = new Commands[]{Commands.VERSION_SURICATA, Commands.VERSION_OINKMASTER};
			ProcessManager.start(cmds, true);
			break;
		case FILES:
			cmds = new Commands[]{Commands.FILE_SURICATA, Commands.FILE_OINKMASTER};
			ProcessManager.start(cmds, true);
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
