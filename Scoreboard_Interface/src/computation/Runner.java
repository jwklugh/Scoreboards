
package computation;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fileIO.FileIn;
import fileIO.FileOut;
import ui.Window;

public class Runner {

    private static Runner theRunner;
    private Window mainWindow;
    private FileIn in;
    private FileOut out;
    private Deriver dev;

    public static final String SAVES_FOLDER = "scoreboardSaves";

    private Runner() throws IOException {
        in  = new FileIn("PlayerNames.txt", "Config.txt");
        out = new FileOut("PlayerNames.txt");
        dev = new Deriver(in.getConfigs());
        mainWindow = new Window(in.getPlayerNames());
        populateLists();
    }

    public static Runner getRunner() throws IOException {
        if(theRunner == null)
            theRunner = new Runner();
        return theRunner;
    }

    public void populateLists() throws IOException {
        mainWindow.fillLists(in.getPlayerNames());
    }

    public void autoFillFields() {
        try {
            mainWindow.autoFillFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void focusAutoFillField() {
        mainWindow.selectField();
    }

    public void updateValues() {
        boolean[] checkBoxes = mainWindow.getAutoCheckBoxes();

        if(checkBoxes[0]) {
            mainWindow.getMatch().disableRace(
                    dev.deriveRace(
                            mainWindow.getPlayer(
                                    Window.DEFENDER).getPlayerRank()));
        } else { mainWindow.getMatch().enableRace(); }


        if(checkBoxes[1]) {
            mainWindow.getMatch().disableScoreCap(
                    dev.deriveCap(mainWindow.getMatch().getRace()));
        } else { mainWindow.getMatch().enableScoreCap(); }


        if(checkBoxes[2]) {
            mainWindow.getMatch().disableTimeLimit(
                    dev.deriveTime(mainWindow.getMatch().getRace()));
        } else { mainWindow.getMatch().enableTimeLimit(); }
    }

    public void addName(String name) throws IOException {
        out.addName(name);
        populateLists();
    }

    public boolean removeName(String name) throws IOException {
        out.removeName(name, in.getPlayerNames());
        populateLists();
        return true;
    }

    public boolean removeName(int which, boolean edit) throws IOException {
        String name = mainWindow.getPlayer(which).getSelection();
        if(name != null)
            return removeName(name);
        else
            JOptionPane.showMessageDialog(
                    mainWindow.getMatch(),
                    "Please select a name to " + (edit?"edit":"remove") + ".",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public void editName(String oldName, String newName) throws IOException {
        if(removeName(oldName))
            addName(newName);
    }

    public void editName(int which, String newName) throws IOException {
        if(removeName(which, true))
            addName(newName);
    }

    public void startNewScoreboard() throws IOException {
        //call out to create new file and fill with parameters from window
        //no selected name should be passed as "Rank #"
        //call in with that file path to get params string
        //launch scoreboard with params and outfile

        String cname = mainWindow.getPlayer(Window.CHALLENGER).getPlayerName();
        String dname = mainWindow.getPlayer(Window.DEFENDER).getPlayerName();
        int crank = mainWindow.getPlayer(Window.CHALLENGER).getPlayerRank();
        int drank = mainWindow.getPlayer(Window.DEFENDER).getPlayerRank();
        int cscore = 0;
        int dscore = 0;
        int[] settings = mainWindow.getSettings();

        String start = "" + cname       + "," +
                dname       + "," +
                crank       + "," +
                drank       + "," +
                cscore      + "," +
                dscore      + "," +
                settings[0] + "," +
                settings[1] + "," +
                settings[2] + "," +
                0           + "," +
                "true";

        String year  = "" + Calendar.getInstance().get(Calendar.YEAR) + ",";
        String month = "" +(Calendar.getInstance().get(Calendar.MONTH)+1) + ",";
        String day   = "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ",";
        String hour  = "" + Calendar.getInstance().get(Calendar.HOUR) + ",";
        String min   = "" + Calendar.getInstance().get(Calendar.MINUTE) + ",";
        String sec   = "" + Calendar.getInstance().get(Calendar.SECOND) + ",";

        String fileName =  "scoreboardSaves/" +
                year + month + day + hour + min + sec + " " +
                cname + " vs " +dname+ " .txt";

        String filePath = out.createFile(fileName, start);

        launchScoreboard(start,filePath);
    }

    public void continueScoreboard() throws IOException {

        JFileChooser fileBrowser = new JFileChooser();
        fileBrowser.setCurrentDirectory(new File(SAVES_FOLDER));
        int retVal = fileBrowser.showDialog(mainWindow.getMatch(),"Load");

        if(retVal == JFileChooser.APPROVE_OPTION) {
            String file = fileBrowser.getSelectedFile().getAbsolutePath();
            String params = in.loadScoreBoard(file);
            if(isValidParams(params)) {
                launchScoreboard(params,file);
            } else {
                JOptionPane.showMessageDialog(
                        mainWindow.getMatch(),
                        "Invalid parameters in file.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void launchScoreboard(String params, String outFile) throws IOException {
        launchScoreboard(params,outFile,mainWindow.getMatch().isStreamTextChecked());
    }

    public void launchScoreboard(String params, String outFile, boolean showText) throws IOException {
        //TODO figure out how to launch a processing program with parameters
        out.setScoreboardSettings(outFile,params);
        if(showText)
            mainWindow.showStreamText();
        mainWindow.close();
        out.launchScoreboard();
        System.exit(0);
    }

    private boolean isValidParams(String params) {
        String[] parse = params.split(",");
        if(parse.length != 11)
            return false;
        for(int i=2; i<10; i++) {
            try {
                Integer.parseInt(parse[i]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

}
