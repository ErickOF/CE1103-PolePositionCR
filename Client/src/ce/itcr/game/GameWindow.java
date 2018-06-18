package ce.itcr.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameWindow extends StateBasedGame {

	public static final int menu = 0;
	public static final int play = 1;

	public GameWindow(String gamename) {
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Play(play));
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.enterState(play);
	}
}