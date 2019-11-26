package run;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main implements Runnable{
	
	public Main() {
		run();
	}
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Main());
		} catch (InvocationTargetException e) {
			JOptionPane.showMessageDialog(
					new JFrame(),
				    "An InvocationTargetException occured while running:\n" +
					e.toString(),
				    "Runtime Error",
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(
					new JFrame(),
				    "An InteruptedException occured while running:\n" +
					e.toString(),
				    "Runtime Error",
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Runner.getRunner();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					new JFrame(),
				    "An IOException occured while running:\n" +
					e.toString(),
				    "Runtime Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
}
