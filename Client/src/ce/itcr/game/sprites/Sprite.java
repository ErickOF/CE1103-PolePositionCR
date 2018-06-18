package ce.itcr.game.sprites;

import java.awt.Rectangle;

import org.newdawn.slick.Image;

public abstract class Sprite {
	protected Integer position;
	public Image icon;
	public Boolean activo = false;
	protected Integer coordX = 0, coordY = 0;
	public Float scale = 3f;

	public synchronized Integer getCoordX() {
		return coordX;
	}

	public synchronized void setCoordX(Integer coordX) {
		this.coordX = coordX;
	}

	public synchronized Integer getCoordY() {
		return coordY;
	}

	public synchronized void setCoordY(Integer coordY) {
		this.coordY = coordY;
	}

	public Sprite(Image image, Integer position, Boolean activo) {
		this.position = position;
		this.icon = image;
		this.activo = activo;
	}

	public Boolean checkCollision(Sprite jugadorPrincipal) {
		;
		Rectangle rect = new Rectangle(coordX, coordY, icon.getWidth(),
				icon.getHeight());
		// System.out.println("coordX "+coordX+ " coordY "+coordY);

		Rectangle rect2 = new Rectangle(jugadorPrincipal.coordX,
				jugadorPrincipal.coordY, jugadorPrincipal.icon.getWidth(),
				jugadorPrincipal.icon.getHeight());

		return rect.intersects(rect2);

	}

	public synchronized Integer getPosition() {
		return this.position;
	}

	public synchronized void setPosition(Integer position) {
		this.position = position;
	}

}
