package ce.itcr.game;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState {

	public String mouse = "no input yet";

	public Menu(int state) {
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.drawRect(50, 100, 60, 120);
		Image intro = new Image("res/img/intro2.png");
		g.drawImage(intro, 0, 0);
		g.drawString(mouse, 50, 50);

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		int xpos = Mouse.getX();
		int ypos = Mouse.getY();

		mouse = "Mouse pos x: " + xpos + " y: " + ypos;

	}

	@Override
	public int getID() {
		return 0;
	}
}