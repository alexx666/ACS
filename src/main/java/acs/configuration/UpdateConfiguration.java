package main.java.acs.configuration;

public class UpdateConfiguration extends Configuration {

	public UpdateConfiguration() {
		//TODO
	}

	@Override
	public void run() {
		getPm().setProcesses(ExternalProcess.OINKMASTER);
		getPm().startAll(true);
	}
}
