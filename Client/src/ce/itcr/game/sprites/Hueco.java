package ce.itcr.game.sprites;

import org.newdawn.slick.Image;

public class Hueco extends Sprite implements Poder {

	public Hueco(Image image, Integer position, Boolean activo) {
		super(image, position, activo);
		// TODO Auto-generated constructor stub
	}

	public void implementar(Jugador objetivo) {
		objetivo.aceleracion = 0f;
	}
}
