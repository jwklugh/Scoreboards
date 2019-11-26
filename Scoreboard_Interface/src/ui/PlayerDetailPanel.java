package ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

public class PlayerDetailPanel extends JPanel{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JPanel playerListPanel;
    private JScrollPane playerListPane;
    private JPanel detailsPanel;
    private JPanel nameEditPanel;
    private JPanel nameSelectPanel;
    private JPanel firstNamePanel;
    private JPanel lastNamePanel;
    private JPanel rankSelectPanel;

    private JLabel panelLabel; //Label for the Challenger Panel
    private JList<String> playerList; //List of players to select from
    private DefaultListModel<String> playerListModel;
    private JButton editNameButton; //Button to allow the user to edit a name in the list
    private JButton removeNameButton; //Button to allow the user to remove a name from the list
    private JCheckBox useFirstName; //Check box to select to use the player's first name on the scoreboard or not
    private JCheckBox useLastName; //Check box to select to use the player's last name on the scoreboard or not
    private JCheckBox abbrFirst; //Check box to select to abbreviate the player's first name on the scoreboard or not
    private JCheckBox abbrLast; //Check box to select to abbreviate the player's last name on the scoreboard or not
    private JSpinner rank; //Spinner to specify the rank of the player
    private JLabel rankLabel; //Label to accompany the rank Spinner


    public PlayerDetailPanel(String label, List<String> players) {
        createElements(label);
        fillPlayerList(players);
        createLayout();
        createSettings();
        createListener(label);
    }

    private void createElements(String label) {
        mainPanel        = new JPanel();
        detailsPanel     = new JPanel();
        nameEditPanel    = new JPanel();
        nameSelectPanel  = new JPanel();
        firstNamePanel   = new JPanel();
        lastNamePanel    = new JPanel();
        rankSelectPanel  = new JPanel();
        playerListPanel  = new JPanel();

        panelLabel       = new JLabel(label);
        playerListModel  = new DefaultListModel<>();
        playerList       = new JList<>(playerListModel);
        playerListPane   = new JScrollPane(playerList);
        editNameButton   = new JButton("Edit Name");
        removeNameButton = new JButton("Remove Name");
        useFirstName     = new JCheckBox("Use First Name",        true );
        useLastName      = new JCheckBox("Use Last Name",         true );
        abbrFirst        = new JCheckBox("Abbreviate First Name", false);
        abbrLast         = new JCheckBox("Abbreviate Last Name",  true );
        rank             = new JSpinner(new SpinnerNumberModel(
                label.equals("  Challenger")?2:1,1,50,-1));
        rankLabel        = new JLabel("  Rank  ");
    }

    private void createLayout() {
        this.setLayout(new BorderLayout());
        this.add(mainPanel,BorderLayout.CENTER);

        mainPanel.       setLayout(new BorderLayout());
        detailsPanel.    setLayout(new BorderLayout());
        nameEditPanel.   setLayout(new BorderLayout());
        nameSelectPanel. setLayout(new BorderLayout());
        firstNamePanel.  setLayout(new BorderLayout());
        lastNamePanel.   setLayout(new BorderLayout());
        rankSelectPanel. setLayout(new BorderLayout());

        playerListPanel.setPreferredSize(new Dimension(230,300));
        playerListPane.setPreferredSize(new Dimension(230,245));
        playerListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.add(panelLabel, BorderLayout.NORTH);
        mainPanel.add(playerListPanel, BorderLayout.CENTER);
        playerListPanel.add(playerListPane);
        mainPanel.add(detailsPanel, BorderLayout.SOUTH);
        detailsPanel.add(nameEditPanel, BorderLayout.NORTH);
        nameEditPanel.add(editNameButton,BorderLayout.WEST);
        nameEditPanel.add(removeNameButton, BorderLayout.EAST);
        detailsPanel.add(nameSelectPanel, BorderLayout.CENTER);
        nameSelectPanel.add(firstNamePanel, BorderLayout.NORTH);
        firstNamePanel.add(useFirstName,BorderLayout.WEST);
        firstNamePanel.add(abbrFirst, BorderLayout.EAST);
        nameSelectPanel.add(lastNamePanel, BorderLayout.SOUTH);
        lastNamePanel.add(useLastName,BorderLayout.WEST);
        lastNamePanel.add(abbrLast,BorderLayout.EAST);
        detailsPanel.add(rankSelectPanel, BorderLayout.SOUTH);
        rankSelectPanel.add(rank,BorderLayout.CENTER);
        rankSelectPanel.add(rankLabel, BorderLayout.EAST);
    }

    private void createSettings() {

        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerList.setLayoutOrientation(JList.VERTICAL);
        playerList.setVisibleRowCount(-1);
    }

    private void createListener(String label) {
        this.            setName(label);
        rank.            setName("rank");
        editNameButton.  setName("EditName");
        removeNameButton.setName("RemoveName");
        playerList.      setName("PlayerList");

        Listener l = new Listener();
        editNameButton.addActionListener(l);
        removeNameButton.addActionListener(l);
        rank.addChangeListener(l);
        playerList.addListSelectionListener(l);
    }

    public void fillPlayerList(List<String> players) {
        playerListModel.clear();
        for(String player : players) {
            playerListModel.addElement(player);
        }
    }

    public String getSelection() {
        return playerList.getSelectedValue();
    }

    public boolean setSelection(String name) {
        playerList.setSelectedValue(name, true);
        String selection = playerList.getSelectedValue();
        return selection != null ? selection.equals(name) : false;
    }

    public String getPlayerName() {
        String formattedName = "";

        if(getSelection() != null) {
            String[] fullName = playerList.getSelectedValue().split(" ");
            String firstName = fullName[0];
            String lastName  = fullName[1];

            if(useFirstName.isSelected()) {
                if(abbrFirst.isSelected()) {
                    formattedName += firstName.charAt(0) + ". ";
                } else {
                    formattedName += firstName + " ";
                }
            }
            if(useLastName.isSelected()) {
                if(abbrLast.isSelected()) {
                    formattedName += lastName.charAt(0) + ".";
                } else {
                    formattedName += lastName;
                }
            }
        }

        if(formattedName.length()<1)
            formattedName = "Rank " + getPlayerRank();
        return formattedName;
    }

    public int getPlayerRank() {
        return (int) rank.getValue();
    }

    public void setPlayerRank(int r) {
        rank.setValue(r);
    }

    @Override
    public String toString() {
        String str = getPlayerName();
        if(!str.contains("Rank")) {
            str += " (" + getPlayerRank() + ")";
        }
        return str;
    }
}
