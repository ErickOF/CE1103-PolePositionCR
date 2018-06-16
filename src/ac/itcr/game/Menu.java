package ac.itcr.game;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState{
	
	public String mouse ="no input yet";
	
	public Menu(int state) {
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawRect(50, 100, 60, 120);
		Image intro = new Image("res/img/intro2.png");
		g.drawImage(intro, 0, 0);
		g.drawString(mouse, 50, 50);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		int xpos = Mouse.getX();
		int ypos = Mouse.getY();
		
		mouse = "Mouse pos x: "+xpos+" y: "+ypos;
		
	}

	@Override
	public int getID() {
		
		return 0;
	}
	
}
