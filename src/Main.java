import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class Main {
	private Timer timer;
	private boolean over; 
	private Board gb;
	
	public Main() {
		this.over = false;
		JFrame mainFrame = new JFrame("Snake");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* Add a nice green color for the background */
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.getContentPane().setBackground(new Color(128, 218, 170));
		
		//So for some reason the panel takes up the entire frame.
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout()); 
		panel.setBackground(Color.BLACK);
		this.gb = new Board(15, 15);

		panel.add(gb);
		mainFrame.getContentPane().add(panel);
		mainFrame.setSize(new Dimension(800, 800));
		mainFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});
	}
}
