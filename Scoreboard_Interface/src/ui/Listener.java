package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import computation.Runner;

public class Listener implements ActionListener, ChangeListener, ListSelectionListener, FocusListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(((Container) e.getSource()).getName()) {
            case "Start" :
                try { Runner.getRunner().startNewScoreboard();}
                catch (IOException e1) {}
                break;
            case "Continue" :
                try { Runner.getRunner().continueScoreboard();}
                catch (IOException e1) {}
                break;
            case "EditName" :
            case "RemoveName" :
                int which =
                ((Component) e.getSource()).getParent()
                .getParent().getParent().getParent().getName().equals(
                        "Challenger") ? Window.CHALLENGER : Window.DEFENDER;
                try {
                    switch(((Component) e.getSource()).getName()) {
                        case "EditName" :
                            String name = getName(e,getSelection(e));
                            if(name != null)
                                Runner.getRunner().editName(which,name);
                            break;
                        case "RemoveName" :
                            Runner.getRunner().removeName(which, false);
                            break;
                    }} catch (IOException e1) {}
                break;
            case "AutoFillField" :
            case "AutoFillButton" :
                try {Runner.getRunner().autoFillFields();}
                catch (IOException e2) {e2.printStackTrace();}
                break;
            default :
                try { Runner.getRunner().updateValues();}
                catch (IOException e1) {}
                break;
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        try { Runner.getRunner().updateValues();}
        catch (IOException e1) {}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(((JList<String>) e.getSource()).getSelectedValue() != null)
            if(((JList<String>) e.getSource()).getSelectedValue().equals("+Add Name+")) {
                String name = getName(e,null);
                if(name != null)
                    try { Runner.getRunner().addName(name);}
                catch (IOException e1) {}
            }
    }

    private String getName(EventObject e, String initialName) {
        if(initialName == null) initialName = "Player Name";
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
                if(name.split(" ").length != 2) {
                    JOptionPane.showMessageDialog(
                            (Component) e.getSource(),
                            "Enter First and Last Name Only.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    return name;
                }
            }
        }
        return null;
    }

    private String getSelection(EventObject e) {
        return ((PlayerDetailPanel)((Component) e.getSource()).getParent()
                .getParent().getParent().getParent()).getSelection();
    }

    @Override
    public void focusGained(FocusEvent e) {
        switch(((Container) e.getSource()).getName()) {
            case "AutoFillField" :
                try { Runner.getRunner().focusAutoFillField(); }
                catch (IOException e1) {}
                break;
            default:
        }

    }

    @Override
    public void focusLost(FocusEvent arg0) {
        // TODO Auto-generated method stub

    }

}
