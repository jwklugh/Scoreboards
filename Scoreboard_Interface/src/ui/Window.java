package ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import computation.Runner;

public class Window {
    public static final int CHALLENGER = 1400;
    public static final int DEFENDER = 1500;

    private JFrame window; //Main window of the program
    private JPanel divider; //Divides window into Challenger, Defender, and Match Details sections
    private JPanel autoFillPanel;
    private JTextField autoFillField; // Field to allow for auto filling of challenge information from the challenge spreadsheet
    private JButton autoFillButton;
    private PlayerDetailPanel challengerPanel; //Panel Identifying all details of the challenger
    private PlayerDetailPanel defenderPanel; //Panel identifying all details of the defender
    private MatchDetailsPanel matchDetailsPanel; //Panel identifying all details of the match, with start and continue buttons

    public Window(List<String> players) {
        createElements(players);
        createLayout();
        createListener();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private void createElements(List<String> players) {
        window = new JFrame("Scoreboard Setup");
        divider = new JPanel();
        autoFillPanel = new JPanel();
        autoFillField = new JTextField("Paste CSV Row Here");
        autoFillButton = new JButton("Auto Fill");
        challengerPanel = new PlayerDetailPanel("  Challenger",players);
        defenderPanel = new PlayerDetailPanel("  Defender",players);
        matchDetailsPanel = new MatchDetailsPanel();
    }

    private void createLayout() {
        window.setSize(700, 430);
        window.setMinimumSize(new Dimension(670,430));
        window.setContentPane(divider);
        divider.setLayout(new BorderLayout());
        autoFillPanel.setLayout(new BorderLayout());
        divider.add(autoFillPanel, BorderLayout.NORTH);
        autoFillPanel.add(autoFillField,BorderLayout.CENTER);
        autoFillPanel.add(autoFillButton,BorderLayout.EAST);
        divider.add(challengerPanel,BorderLayout.WEST);
        divider.add(defenderPanel, BorderLayout.EAST);
        divider.add(matchDetailsPanel, BorderLayout.CENTER);
    }

    public void createListener() {
        Listener l = new Listener();
        autoFillField.setName("AutoFillField");
        autoFillButton.setName("AutoFillButton");
        autoFillField.addActionListener(l);
        autoFillButton.addActionListener(l);
        autoFillField.addFocusListener(l);
    }

    public PlayerDetailPanel getPlayer(int p) {
        if( p==CHALLENGER )
            return challengerPanel;
        else if(p==DEFENDER)
            return defenderPanel;
        return null;
    }

    public MatchDetailsPanel getMatch() {
        return matchDetailsPanel;
    }

    public void fillLists(List<String> players) {
        challengerPanel.fillPlayerList(players);
        defenderPanel.fillPlayerList(players);
        window.repaint();
    }

    public boolean[] getAutoCheckBoxes() {
        return matchDetailsPanel.getAutoCheckBoxes();
    }

    public int[] getSettings() {
        return new int[]{matchDetailsPanel.getRace(),
                matchDetailsPanel.getCap(),
                matchDetailsPanel.getTime()};
    }

    public void autoFillFields() throws IOException {
        ChallengeCSV fields = new ChallengeCSV(autoFillField.getText());

        if(!challengerPanel.setSelection(fields.challenger))
            if(!autoFillPlayer(fields.challenger)) return;

        if(!defenderPanel.setSelection(fields.defender))
            if(!autoFillPlayer(fields.defender)) return;

        challengerPanel.setSelection(fields.challenger);
        defenderPanel.setSelection(fields.defender);
        challengerPanel.setPlayerRank(fields.challengerRank);
        defenderPanel.setPlayerRank(fields.defenderRank);

        Runner.getRunner().updateValues();
    }

    public boolean autoFillPlayer(String name) throws IOException {
        int s = JOptionPane.showConfirmDialog(window, "Would you like to add " + name +
                " to the Player List?",null,JOptionPane.OK_CANCEL_OPTION);
        if(s==JOptionPane.OK_OPTION)
            Runner.getRunner().addName(name);
        return s==JOptionPane.OK_OPTION;
    }

    public void selectField() {
        autoFillField.setSelectionStart(0);
        autoFillField.setSelectionEnd(autoFillField.getText().length());
    }

    public void close() {
        window.dispose();
    }

    public void showStreamText() {
        String titleAndDescription =
                "Ranked Challenge: "
                        + getPlayer(CHALLENGER).toString() + " vs "
                        + getPlayer(DEFENDER).toString() + "\n" +
                        "\n" +
                        "JMU Billiards Club Official Challenge: " +
                        getPlayer(CHALLENGER).getSelection() +
                        " challenges " +
                        getPlayer(DEFENDER).getSelection() +
                        " for their #" +
                        getPlayer(DEFENDER).getPlayerRank() +
                        " spot on the JMU Billiards Team.";
        JOptionPane.showMessageDialog(
                new JFrame(),
                "The following has been copied to the clipboard:\n"
                        + "\n\""
                        +titleAndDescription + "\"",
                        "Stream Text",
                        JOptionPane.PLAIN_MESSAGE);
        Toolkit.getDefaultToolkit().getSystemClipboard().
        setContents(new StringSelection(titleAndDescription), null);
    }

    @SuppressWarnings("unused")
    private class ChallengeCSV {

        String submissionTime;
        String challenger;
        int challengerRank;
        String defender;
        int defenderRank;
        String date;
        Time time;
        String approver;


        public ChallengeCSV(String line) {
            String[] parse = line.split("\t");
            submissionTime = parse[0].trim();
            challenger = parse[1].trim();
            challengerRank = Integer.parseInt(parse[2].trim());
            defender = parse[3].trim();
            defenderRank = Integer.parseInt(parse[4].trim());
            date = parse[5].trim();
            time = new Time(parse[6].trim());
            approver = parse.length > 7 ? parse[7].trim() : null;
        }

        private class Time {
            int hour;
            String minute;
            String xm;

            public Time(String time) {
                String[] parts = time.split(":");
                hour = Integer.parseInt(parts[0]);
                minute = parts[1];
                xm = "" + parts[2].substring(parts[2].indexOf(" ")).toLowerCase().trim();
            }
        }
    }
}
