package ce.itcr.game.sprites;

import org.newdawn.slick.Image;

public class Jugador extends Sprite {
	
	public static final Float MAX_ACC =2f;
	
	public Jugador(Image image, Integer position, Boolean activo) {
		super(image, position, activo);
		// TODO Auto-generated constructor stub
	}

	protected Float aceleracion=0f;
	public Float getAceleracion() {
		return aceleracion;
	}

	public void increaseAceleracion(Float aceleracionInc) {
		if(this.aceleracion<MAX_ACC || aceleracionInc<=0) {
			this.aceleracion += aceleracionInc;
		}
	}

	public void setAceleracion(Float aceleracion) {
		
		this.aceleracion = aceleracion;
	}

	public static final Integer VELOCIDAD =200;
	public Integer vidas=3;

	
	public int getSpeed() {
		// TODO Auto-generated method stub
		return (int) (VELOCIDAD*aceleracion);
	}
	
	public void collided(Jugador jugador) {
		if(checkCollision(jugador)) {
			aceleracion=jugador.aceleracion=0f;
			jugador.vidas--;
			vidas--;
			System.out.println("colision");
			if(vidas<=0) {
				vidas=0;
			}
		}
	}



}
