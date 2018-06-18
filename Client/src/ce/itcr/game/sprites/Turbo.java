package ce.itcr.game.sprites;

import org.newdawn.slick.Image;

public class Turbo extends Sprite implements Poder {

	public Turbo(Image image, Integer position, Boolean activo) {
		super(image, position, activo);
	}

	public void implementar(Jugador objetivo) {
		objetivo.aceleracion *= 2;
	}
}
