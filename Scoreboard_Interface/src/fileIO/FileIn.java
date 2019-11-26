package fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileIn {

    File playerNames;
    File config;

    public FileIn(String playerNamesPath, String configPath) throws IOException {
        playerNames = new File(playerNamesPath);
        config = new File(configPath);
        makeValidFiles();
    }

    private void makeValidFiles() throws IOException {
        if(!playerNames.exists() )
            playerNames.createNewFile();
        if(!config.exists() )
            config.createNewFile();
    }

    public ArrayList<String> getPlayerNames() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(playerNames));
        ArrayList<String> names = new ArrayList<>();
        String line = reader.readLine();
        while(line != null) {
            if(!line.equals(""))
                names.add(line);
            line = reader.readLine();
        }
        reader.close();
        names.sort(null);
        names.add("+Add Name+");
        return names;
    }

    // Config file should be formatted with 1 line for info then as follows:
    // <raceTo>,<upperRank>,<lowerRank>,<scoreCap>,<timeLimit>
    // all are to be integers with no spaces
    public ArrayList<Integer[]> getConfigs() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(config));
        ArrayList<Integer[]> configs = new ArrayList<>();
        String line = reader.readLine();
        line = reader.readLine();
        while(line != null) {
            String[] parsedLine = line.split(",");
            Integer[] item = new Integer[5];
            for(int i=0; i<5; i++) {
                item[i] = Integer.parseInt(parsedLine[i]);
            }
            configs.add(item);
            line = reader.readLine();
        }
        reader.close();
        return configs;

    }

    public String loadScoreBoard(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String line = reader.readLine();
        String prev = line;
        while(line != null) {
            prev = line;
            line = reader.readLine();
        }
        reader.close();
        return prev;
    }
}
