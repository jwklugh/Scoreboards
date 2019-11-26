package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;

public class Window {
	private JFrame window;
	private JPanel divider;
		private JPanel mainPanel;
			private JScrollPane playerListPane;
				private JList<String> playerList;
					private DefaultListModel<String> playerListModel;
			private JPanel namePanel;
				private JButton removeNameButton;
				private JButton addNameButton;
		private JPanel settingPanel;
			private JPanel sliderPanel;
				private JLabel sliderLabel;
				private JSlider frameSizeSlider;
			private JPanel buttonPanel;
				private JButton exitButton;
				private JPanel continuePanel;
					private JButton loadButton;
					private JButton launchButton;
				
	public Window(List<String> players, int fs) {
		createElements(fs);
		fillPlayerList(players);
		createLayout();
		createSettings();
		createListener();
		window.setVisible(true);
		window.repaint();
	}
	
	private void createElements(int fs) {
		window 			= new JFrame("Scoreboard Setup");
		divider 		= new JPanel();
		mainPanel 		= new JPanel();
		settingPanel 	= new JPanel();
		buttonPanel 	= new JPanel();
		continuePanel   = new JPanel();
		namePanel		= new JPanel();
		sliderPanel     = new JPanel();
		addNameButton	= new JButton("+");
		removeNameButton= new JButton("-");
		exitButton 		= new JButton("Exit");
		loadButton 		= new JButton("Load");
		launchButton 	= new JButton("Launch");
		frameSizeSlider = new JSlider(JSlider.HORIZONTAL,0,200,fs);
		sliderLabel     = new JLabel(" Frame Size: ");
		playerListModel = new DefaultListModel<>();
		playerList 		= new JList<>(playerListModel);
		playerListPane 	= new JScrollPane(playerList);
	}
	
	private void createLayout() {
		window.setSize(700, 400);
		window.setContentPane(divider);
		
		divider		.setLayout(new BorderLayout());
		mainPanel	.setLayout(new BorderLayout());
		settingPanel.setLayout(new BorderLayout());
		buttonPanel	.setLayout(new BorderLayout());
		namePanel	.setLayout(new BorderLayout());
		sliderPanel .setLayout(new BorderLayout());
		
		mainPanel.setPreferredSize(new Dimension(600,300));
		playerListPane.setPreferredSize(new Dimension(600,245));
		playerListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		divider.add(mainPanel,BorderLayout.CENTER);
		divider.add(settingPanel,BorderLayout.SOUTH);

		mainPanel.add(playerListPane,BorderLayout.CENTER);
		mainPanel.add(removeNameButton,BorderLayout.WEST);
		mainPanel.add(addNameButton,BorderLayout.EAST);
		
		settingPanel.add(sliderPanel,BorderLayout.NORTH);
			sliderPanel.add(sliderLabel,BorderLayout.WEST);
			sliderPanel.add(frameSizeSlider,BorderLayout.CENTER);
		settingPanel.add(buttonPanel,BorderLayout.SOUTH);
			buttonPanel.add(exitButton,BorderLayout.WEST);
			buttonPanel.add(continuePanel,BorderLayout.EAST);
				continuePanel.add(loadButton,BorderLayout.WEST);
				continuePanel.add(launchButton,BorderLayout.EAST);
	}
	
	private void createSettings() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerList.setLayoutOrientation(JList.VERTICAL);
		playerList.setVisibleRowCount(-1);
		
		//Turn on labels at major tick marks.
		frameSizeSlider.setMajorTickSpacing(50);
		frameSizeSlider.setMinorTickSpacing(10);
		frameSizeSlider.setPaintTicks(true);
		frameSizeSlider.setPaintLabels(true);
	}
	
	private void createListener() {
		exitButton		.setName("exit");
		loadButton		.setName("load");
		launchButton	.setName("launch");
		addNameButton	.setName("add");
		removeNameButton.setName("remove");
		playerList		.setName("PlayerList");
		
		Listener l = new Listener();
		exitButton		.addActionListener(l);
		loadButton		.addActionListener(l);
		launchButton	.addActionListener(l);
		addNameButton	.addActionListener(l);
		removeNameButton.addActionListener(l);
		playerList		.addListSelectionListener(l);
	}
	
	public void fillPlayerList(List<String> players) {
		playerListModel.clear();
		for(String player : players) {
			playerListModel.addElement(player);
		}
	}
	
	public String getSelectedPlayer() {
		return playerList.getSelectedValue();
	}
	
	public int getFrameSliderSelection() {
		return Math.max(frameSizeSlider.getValue(),15);
	}
	
	public void update(List<String> names) {
		fillPlayerList(names);
		window.repaint();
	}
	
	public void close() {
		window.dispose();
	}
				
}
