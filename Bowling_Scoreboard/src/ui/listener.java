package ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import run.Runner;

public class Listener implements ActionListener, ListSelectionListener {

//	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent e) {
//		if(((JList<String>) e.getSource()).getSelectedValue() != null)
//			if(((JList<String>) e.getSource()).getSelectedValue().equals("+Add Name+")) {
//				String name = getName(e,null);
//				if(name != null) {}
//					try { Runner.getRunner().addName(name);}
//					catch (IOException e1) {}
//			}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			switch(((Component) e.getSource()).getName()) {
				case "exit" :   System.exit(0); break;
				case "load" :   Runner.getRunner().launch(false); break;
				case "launch" : Runner.getRunner().launch(true); break;
				case "add" :    Runner.getRunner().addName(getName(e,null)); break;
				case "remove" : Runner.getRunner().removeName(); break;
				default :
			}
		} catch (Exception e1) {
			
		}
		
	}
	
	private String getName(EventObject e, String initialName) {
		if(initialName == null) initialName = "Player Initials";
		String name = (String)JOptionPane.showInputDialog(
                (Component) e.getSource(),
                "Add a Player to the List",
                "Add Player",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                initialName);
		
		if(name != null) {
			if(name.length() > 0) {
				if(name.split(" ").length != 1) {
					JOptionPane.showMessageDialog(
							(Component) e.getSource(),
						    "Enter up to 3 initials Only.",
						    "Input Error",
						    JOptionPane.ERROR_MESSAGE);
				} else {
					return name;
				}
			}
		}
		return null;
	}

}
