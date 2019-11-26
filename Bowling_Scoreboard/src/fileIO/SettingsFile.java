package fileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsFile {
	
	public final static String PREFIX = "app/data/";
	public final static String FILENAME = "settings.txt";
	
	String loadFileName;
	String saveFileName;
	String playersFileName;
	public int frameSize;
	
	public SettingsFile() {
		try {
			readSettings();
		} catch (Exception e) {
			defaults();
		}
	}
	
	private void readSettings() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(PREFIX+FILENAME));
		loadFileName = reader.readLine();
		saveFileName = reader.readLine();
		playersFileName = reader.readLine();
		defaults();
		frameSize = Integer.parseInt(reader.readLine());
		reader.close();
	}
	
	private void defaults() {
		loadFileName = "default.txt";
		saveFileName = "data/saves/defaultSave.txt";
		playersFileName = "players.txt";
		frameSize = 130;
	}
	
	@SuppressWarnings("unused")
	private void defaults(int s) {
		loadFileName = "default.txt";
		saveFileName = "data/saves/defaultSave.txt";
		playersFileName = "players.txt";
		frameSize = s;
	}
	
	public void writeSettings(String save, String load, int size) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(PREFIX+FILENAME));
		writer.write(save + "\n");
		writer.write(load + "\n");
		writer.write("players.txt" + "\n");
		writer.write(""+size);
		writer.close();
	}
}
