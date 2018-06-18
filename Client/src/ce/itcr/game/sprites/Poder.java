/**
 *
 */
package ce.itcr.game.sprites;

/**
 * @author anava
 * 
 */
public interface Poder {

	static final Integer duracion = 1000 * 5; // 5 ms

	void implementar(Jugador objetivo);
}
