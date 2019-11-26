package ui;
import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MatchDetailsPanel extends JPanel{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JPanel settingsPanel;
    private JPanel racePanel;
    private JPanel scoreCapPanel;
    private JPanel timeLimitPanel;
    private JPanel timeSpinnerPanel;

    private JLabel raceLabel;
    private JSpinner raceTo;
    private JCheckBox autoSelectRaceTo;
    private JLabel scoreCapLabel;
    private JSpinner scoreCap;
    private JCheckBox autoSelectScoreCap;
    private JLabel timeLabel;
    private JSpinner timeLimit;
    private JLabel minuteLabel;
    private JCheckBox autoSelectTimeLimit;
    private JButton startButton;
    private JButton continueButton;
    private JPanel streamSettingsPanel;
    private JPanel giveStreamTextPanel;
    private JCheckBox giveStreamTextBox;

    public MatchDetailsPanel() {
        createElements();
        createLayout();
        createListener();

        disableRace(9);
        disableScoreCap(13);
        disableTimeLimit(150);
    }

    private void createElements() {
        mainPanel           = new JPanel();
        settingsPanel       = new JPanel();
        racePanel           = new JPanel();
        scoreCapPanel       = new JPanel();
        timeLimitPanel      = new JPanel();
        timeSpinnerPanel    = new JPanel();
        streamSettingsPanel = new JPanel();
        giveStreamTextPanel = new JPanel();
        raceLabel           = new JLabel("Race To:");
        raceTo              = new JSpinner(new SpinnerNumberModel(9,1,99,1));
        autoSelectRaceTo    = new JCheckBox("Auto-Select", true);
        scoreCapLabel       = new JLabel("ScoreCap:");
        scoreCap            = new JSpinner(new SpinnerNumberModel(13,1,99,1));
        autoSelectScoreCap  = new JCheckBox("AutoSelect", true);
        timeLabel           = new JLabel("Time Limit:");
        timeLimit           = new JSpinner(new SpinnerNumberModel(150,-1,1440,15));
        minuteLabel         = new JLabel("min");
        autoSelectTimeLimit = new JCheckBox("Auto-Select", true);
        startButton         = new JButton("Start");
        continueButton      = new JButton("Load Match");
        giveStreamTextBox   = new JCheckBox("<html>Copy Stream Text"
                + "<br>To Clipboard", true);

    }

    private void createLayout() {
        this.setLayout(new BorderLayout());
        this.add(mainPanel,BorderLayout.CENTER);

        mainPanel.          setLayout(new BorderLayout());
        settingsPanel.      setLayout(new BoxLayout(settingsPanel,BoxLayout.Y_AXIS));
        racePanel.          setLayout(new BorderLayout());
        scoreCapPanel.      setLayout(new BorderLayout());
        timeLimitPanel.     setLayout(new BorderLayout());
        timeSpinnerPanel.   setLayout(new BorderLayout());
        streamSettingsPanel.setLayout(new BoxLayout(streamSettingsPanel,BoxLayout.Y_AXIS));
        giveStreamTextPanel.setLayout(new BorderLayout());


        mainPanel.add(startButton,BorderLayout.SOUTH);
        mainPanel.add(settingsPanel, BorderLayout.CENTER);
        settingsPanel.add(racePanel);
        racePanel.add(raceLabel, BorderLayout.NORTH);
        racePanel.add(raceTo, BorderLayout.CENTER);
        racePanel.add(autoSelectRaceTo, BorderLayout.SOUTH);
        settingsPanel.add(scoreCapPanel);
        scoreCapPanel.add(scoreCapLabel, BorderLayout.NORTH);
        scoreCapPanel.add(scoreCap, BorderLayout.CENTER);
        scoreCapPanel.add(autoSelectScoreCap, BorderLayout.SOUTH);
        settingsPanel.add(timeLimitPanel);
        timeLimitPanel.add(timeLabel, BorderLayout.NORTH);
        timeLimitPanel.add(timeSpinnerPanel, BorderLayout.CENTER);
        timeSpinnerPanel.add(timeLimit, BorderLayout.CENTER);
        timeSpinnerPanel.add(minuteLabel, BorderLayout.EAST);
        timeLimitPanel.add(autoSelectTimeLimit, BorderLayout.SOUTH);
        settingsPanel.add(streamSettingsPanel);
        streamSettingsPanel.add(giveStreamTextPanel);
        giveStreamTextPanel.add(giveStreamTextBox,BorderLayout.WEST);

        mainPanel.add(continueButton, BorderLayout.NORTH);

        mainPanel.add(new JLabel("   "), BorderLayout.WEST);
        mainPanel.add(new JLabel("   "), BorderLayout.EAST);
    }

    private void createListener() {
        startButton.        setName("Start");
        continueButton.     setName("Continue");
        autoSelectRaceTo.   setName("autoRace");
        autoSelectScoreCap. setName("autoScore");
        autoSelectTimeLimit.setName("autoTime");

        Listener l = new Listener();
        startButton.        addActionListener(l);
        continueButton.     addActionListener(l);
        autoSelectRaceTo.   addActionListener(l);
        autoSelectScoreCap. addActionListener(l);
        autoSelectTimeLimit.addActionListener(l);
        raceTo.             addChangeListener(l);
        scoreCap.           addChangeListener(l);
        timeLimit.          addChangeListener(l);
    }


    public void disableRace(int value) {
        raceTo.setEnabled(false);
        raceTo.setValue(value);
    }

    public void enableRace() {
        raceTo.setEnabled(true);
    }

    public void disableScoreCap(int value) {
        scoreCap.setEnabled(false);
        scoreCap.setValue(value);
    }

    public void enableScoreCap() {
        scoreCap.setEnabled(true);
    }

    public void disableTimeLimit(int value) {
        timeLimit.setEnabled(false);
        timeLimit.setValue(value);
    }

    public void enableTimeLimit() {
        timeLimit.setEnabled(true);
    }

    public int getRace() {
        return (int) raceTo.getValue();
    }

    public int getCap() {
        return (int) scoreCap.getValue();
    }

    public int getTime() {
        return (int) timeLimit.getValue();
    }

    public boolean[] getAutoCheckBoxes() {
        return new boolean[] {
                autoSelectRaceTo.isSelected(),
                autoSelectScoreCap.isSelected(),
                autoSelectTimeLimit.isSelected()
        };
    }

    public boolean isStreamTextChecked() {
        return giveStreamTextBox.isSelected();
    }
}
