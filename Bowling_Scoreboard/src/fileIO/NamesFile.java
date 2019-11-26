package fileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NamesFile {
	
	private SettingsFile settings;
	private String filename;
	private String[] playerNames;
	
	public NamesFile(SettingsFile file) throws IOException {
		settings = file;
		filename = SettingsFile.PREFIX+settings.playersFileName;
		playerNames = importNames();
	}
	
	private String[] importNames() throws IOException {
		BufferedReader reader;
		reader = new BufferedReader(
				new FileReader(new File(filename)));
		
		int count = 0;
		while(reader.readLine() != null) count++;
		reader.close();
		
		reader = new BufferedReader(
				new FileReader(new File(filename)));
		
		String[] result = new String[count];
		for(int i=0; i<result.length; i++)
			result[i] = reader.readLine();
		
		return result;
	}
	
	public void addName(String newName) throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new FileWriter(new File(filename)));
		
		for(String name : playerNames) {
			writer.write(name + "\n");
		}
		writer.write(newName);
		writer.close();
		update();
	}
	
	public ArrayList<String> getNames() {
		ArrayList<String> l = new ArrayList<>();
		for(String n:playerNames)
			l.add(n);
		return l;
		
	}
	
	public void update() throws IOException {
		playerNames = importNames();
	}
	
	public void removeName(String oldName) throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new FileWriter(filename));
		
		for(String name : playerNames) {
			if(name != oldName)
			writer.write(name + "\n");
		}
		writer.close();
		update();
	}
	
}
