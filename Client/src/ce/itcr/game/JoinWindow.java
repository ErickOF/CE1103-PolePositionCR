package ce.itcr.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import ce.itcr.errors.ColorException;
import ce.itcr.socket.ClientSocket;
import ce.itcr.sound.MusicPlayer;

public class JoinWindow extends JFrame implements KeyListener {
	// CONSTANTS
	private static final Color BLACK = new Color(0, 0, 0);
	private static final Color RED = new Color(255, 0, 0);
	private static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 60);
	private static final int HEIGHT = 700;
	private static final int WIDTH = 1024;
	private final JTextField tfNickname;
	private static final long serialVersionUID = 1L;
	private static final String IMG_LOGO_PATH = "res/img/logo.png";
	private static final String TITLE = "polePositionCR";

	// Variables
	private final JComboBox<String> cbColors;

	/***************************** TEST *****************************/
	private String colors[];

	/***************************** TEST *****************************/

	/**
	 * Class constructor
	 * 
	 * @throws IOException
	 * @throws ColorException
	 */
	public JoinWindow() throws IOException, ColorException {
		// Setup Window
		this.setTitle(TITLE);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.getContentPane().setBackground(BLACK);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create panel to image
		JPanel pane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				// Load image
				ImageIcon imgLogo = new ImageIcon(IMG_LOGO_PATH);
				super.paintComponent(g);
				// Draw image
				g.drawImage(imgLogo.getImage(), 62, 0, null);
			}
		};

		// Setup pane
		pane.setBackground(BLACK);
		pane.setSize(WIDTH, 350);
		pane.setLocation(0, 0);

		// Create labels
		JLabel lbNickname = new JLabel("Nickname");
		lbNickname.setSize(450, 400);
		lbNickname.setForeground(RED);
		lbNickname.setFont(FONT);
		lbNickname.setLocation(70, 200);

		JLabel lbColor = new JLabel("Car Color");
		lbColor.setSize(450, 400);
		lbColor.setForeground(RED);
		lbColor.setFont(FONT);
		lbColor.setLocation(70, 300);

		// Create text field to user nickname
		tfNickname = new JTextField();
		tfNickname.setSize(500, 65);
		tfNickname.setFont(FONT);
		tfNickname.setLocation(450, 361);
		tfNickname.addKeyListener(this);

		// Get colors from server
		if (!getColores()) {
			throw new ColorException("");
		}

		// Create Combo Box to show colors
		cbColors = new JComboBox<String>(colors);
		cbColors.setSize(300, 65);
		cbColors.setLocation(450, 460);
		cbColors.setFont(FONT);
		cbColors.addKeyListener(this);

		// Add new components to Window
		this.getContentPane().setLayout(null);
		this.getContentPane().add(pane);
		this.getContentPane().add(lbNickname);
		this.getContentPane().add(lbColor);
		this.getContentPane().add(tfNickname);
		this.getContentPane().add(cbColors);

		// Add Key Listener
		addKeyListener(this);
	}

	/**
	 * This method send request to the server to get available colors
	 * 
	 * @return
	 */
	private boolean getColores() {
		// Get socket instance
		ClientSocket.getInstance();
		// Send request to server and get the respond
		String response = ClientSocket.send(" get,,,");
		// Verify if request was done correctly
		if (response.equalsIgnoreCase("")) {
			return false;
		}
		// Get and save colors
		String tempColors[] = response.split(",");
		colors = new String[tempColors.length - 1];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = tempColors[i];
		}
		System.out.println(colors.length);
		return true;
	}

	/*
	 * This method detect key pressed events
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		// Start game
		if (key == KeyEvent.VK_ENTER) {
			try {
				// Get values to send
				String nickname = tfNickname.getText();
				String color = String.valueOf(cbColors.getSelectedItem());
				// Create request
				String request = " new," + nickname + "," + color + ",";
				// Get socket instance
				ClientSocket.getInstance();
				// Send request to server and verify if request was done
				// correctly
				String response = ClientSocket.send(request);
				System.out.println(response + "-" + response.length());
				System.out.println(response == "rcv");
				System.out.println(response.equals("rcv"));
				System.out.println(response.length());
				if (response.equals("rcv")) {
					// Call game window
					AppGameContainer appgc = new AppGameContainer(
							new GameWindow(TITLE));
					appgc.setDisplayMode(WIDTH, HEIGHT, false);
					this.dispose();
					appgc.start();
				} // If response fail
				else if (response.equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(null, "Server not available");
				} // Incorrect selected color
				else {
					colors = response.split(",");
				}
			} catch (SlickException e2) {
				System.out.println("Game Window Error");
			}
		}
		// Close window
		if (key == KeyEvent.VK_ESCAPE) {
			MusicPlayer.stopMainTheme();
			this.dispose();
		}
	}

	/*
	 * This method get key released events
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0) {
	}

	/*
	 * This method get key typed events
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent arg0) {
	}
}