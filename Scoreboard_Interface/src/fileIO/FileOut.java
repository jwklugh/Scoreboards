package fileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileOut {

    private File playerNames;

    public FileOut(String playerNamesPath) {
        playerNames = new File(playerNamesPath);
        checkValidFiles();
    }

    private boolean checkValidFiles() {
        if( playerNames.exists() )
            return playerNames.canWrite();
        return false;
    }

    public void addName(String name) throws IOException {
        if(!name.equals("+Add Name+")) {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(playerNames,true));
            writer.write("\n" + name);
            writer.flush();
            writer.close();
        }
    }

    public void removeName(String name, ArrayList<String> names) throws IOException {
        names.remove(name);
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(playerNames,false));
        for(String n:  names) {
            if(!n.equals("+Add Name+") && !n.trim().equals(""))
                writer.write("\n" + n);
            writer.flush();
        }
        writer.close();
    }

    public String createFile(String filename, String params) throws IOException {
        File newFile = new File(filename);
        if(newFile.exists()) {
            return null;
        } else {
            newFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(newFile));
            writer.write(params);
            writer.close();
            return newFile.getAbsolutePath();
        }
    }

    public void setScoreboardSettings(String filename, String params) throws IOException {
        File settings = new File("Scoreboard/data/ScoreboardSettings.txt");
        if(!settings.exists()) {
            settings.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(settings, false));
        writer.write(filename + "\r\n" + params);
        writer.flush();
        writer.close();
    }

    public void launchScoreboard() throws IOException {
        File dir = new File("Scoreboard");
        String command =
                dir.getAbsolutePath()+
                "/GenericScorekeeper.exe";
        Runtime.getRuntime().exec(command,null,dir);
    }
}
