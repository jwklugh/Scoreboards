package run;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fileIO.NamesFile;
import fileIO.SettingsFile;
import ui.Window;

public class Runner {
	
	private static Runner theRunner;
	private Window mainWindow;
	private NamesFile names;
	private SettingsFile settings;

	private Runner() throws IOException {
		settings = new SettingsFile();
		names = new NamesFile(settings);
		mainWindow = new Window(new ArrayList<String>(names.getNames()),
				settings.frameSize);
	}
	
	public static Runner getRunner() throws IOException {
		if(theRunner == null)
			theRunner = new Runner();
		return theRunner;
	}
	
	public void addName(String name) throws IOException {
		names.addName(name);
		mainWindow.update(names.getNames());
	}
	
	public void removeName() throws IOException {
		names.removeName(mainWindow.getSelectedPlayer());
		mainWindow.update(names.getNames());
	}
	
	public void launch(boolean load) throws IOException {
		settings.writeSettings(load ?
				"default.txt" :
				"saves/defaultSave.txt",
				"data/saves/defaultSave.txt", 
				mainWindow.getFrameSliderSelection());
		File dir = new File("app");
		String command = 
				dir.getAbsolutePath()+
				"/bowling.exe";
		Runtime.getRuntime().exec(command,null,dir);
		mainWindow.close();
		System.exit(0);
		
	}
}
