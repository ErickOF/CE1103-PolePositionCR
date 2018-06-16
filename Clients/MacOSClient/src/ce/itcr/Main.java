package ce.itcr;

import java.io.IOException;

import ce.itcr.game.MenuWindow;

public class Main {

	public static void main(String[] args) {
		try {
			MenuWindow menu = new MenuWindow();
			menu.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}