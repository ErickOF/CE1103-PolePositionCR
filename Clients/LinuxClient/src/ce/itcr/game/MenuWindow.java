package ce.itcr.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ce.itcr.sound.MusicPlayer;

public class MenuWindow extends JFrame implements KeyListener {
	// CONSTANTS
	private static final Color BLACK = new Color(0, 0, 0);
	private static final Color RED = new Color(255, 0, 0);
	private static final Color YELLOW = new Color(255, 255, 0);
	private static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 60);
	private static final int HEIGHT = 700;
	private static final int WIDTH = 1024;
	private static final long serialVersionUID = 1L;
	private static final String IMG_LOGO_PATH = "res/img/logo.png";
	private static final String TITLE = "polePositionCR";

	// Variables
	private boolean wait;
	private final JLabel lbTextJoin;
	private final JLabel lbTextExit;
	private final JPanel pane;
	private int color;
	private int option;

	public MenuWindow() throws IOException {
		// Setup Window
		this.setTitle(TITLE);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.getContentPane().setBackground(BLACK);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create panel to image
		pane = new JPanel() {
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

		// Create and setup menu options
		lbTextJoin = new JLabel("1.Join");
		lbTextJoin.setSize(450, 400);
		lbTextJoin.setForeground(RED);
		lbTextJoin.setFont(FONT);
		lbTextJoin.setLocation(430, 200);

		lbTextExit = new JLabel("0.Exit");
		lbTextExit.setSize(450, 400);
		lbTextExit.setForeground(RED);
		lbTextExit.setFont(FONT);
		lbTextExit.setLocation(430, 300);

		// Add new components to Window
		this.getContentPane().setLayout(null);
		this.getContentPane().add(pane);
		this.getContentPane().add(lbTextJoin);
		this.getContentPane().add(lbTextExit);

		// Add Key Listener
		addKeyListener(this);

		// Current option in menu
		option = 0;

		// Thread to update graphics
		new Thread() {
			@Override
			public void run() {
				try {
					// Current text color
					color = 0;
					wait = true;

					while (wait) {
						// Change text color
						Color text_color = YELLOW;
						if (color == 0) {
							text_color = RED;
						}
						// Restart text color
						lbTextJoin.setForeground(RED);
						lbTextExit.setForeground(RED);
						// Focus on current option
						if (option == 0) {
							lbTextJoin.setForeground(text_color);
						} else if (option == 1) {
							lbTextExit.setForeground(text_color);
						}
						// Change color
						color = 1 - color;
						// Sleep thread
						Thread.sleep(250);
					}
				} catch (InterruptedException v) {
					System.out.println(v);
				}
			}
		}.start();

		MusicPlayer.getInstance().playMainTheme();
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		// Previous option
		if (key == KeyEvent.VK_UP) {
			option -= 1;
		}
		// Next option
		if (key == KeyEvent.VK_DOWN) {
			option += 1;
		}
		// Select option
		if (key == KeyEvent.VK_ENTER) {
			wait = false;
			// If option was create game
			if (option == 0) {
				try {
					JoinWindow joinwindow = new JoinWindow();
					this.dispose();
					joinwindow.setVisible(true);
				} catch (IOException e1) {
					System.out.println(e1);
				}
			}
			// If option was exit
			else {
				MusicPlayer.stopMainTheme();
				this.dispose();
			}
		}
		// Close window
		if (key == KeyEvent.VK_ESCAPE) {
			wait = false;
			MusicPlayer.stopMainTheme();
			this.dispose();
		}
		// Limit options
		if (option == 2) {
			option = 0;
		} else if (option == -1) {
			option = 1;
		}
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}
}
