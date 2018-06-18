package ce.itcr.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {
	// Constants
	private static final String MAIN_THEME_PATH = "res/music/MainTheme.mp3";

	// Variables
	private static boolean playing;
	private static MusicPlayer musicPlayer;
	private static Player playerMainTheme;

	private MusicPlayer() {
	}

	// Singleton
	public static MusicPlayer getInstance() {
		// If not instance
		if (musicPlayer == null) {
			musicPlayer = new MusicPlayer();
		}
		return musicPlayer;
	}

	public void playMainTheme() {
		new Thread() {
			@Override
			public void run() {
				playing = true;
				try {
					while (playing) {
						// Load file
						FileInputStream fMainTheme = new FileInputStream(
								MAIN_THEME_PATH);
						BufferedInputStream buffer = new BufferedInputStream(
								fMainTheme);
						// Play main theme
						playerMainTheme = new Player(buffer);
						playerMainTheme.play();
					}
				} catch (FileNotFoundException e) {
					System.out.println(e);
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void stopMainTheme() {
		// Stop main theme
		playing = false;
		playerMainTheme.close();
	}
}
