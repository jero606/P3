/**
 * 
 */
package model.score;

import model.Location;

/**
 * Representa una puntuación basada en la distancia recorrida por un jugador en
 * su deambular por un mundo BlockWorld
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class PlayerMovementScore extends Score<Location> {

	/**
	 * Posicion previa del player
	 */
	private Location previousLocation;

	/**
	 * Constructor
	 * 
	 * @param name del player
	 */
	public PlayerMovementScore(String name) {
		super(name);
		previousLocation = null;
	}

	/**
	 * Compara puntuaciones por su valor
	 * 
	 * @param loc posciion a comparar
	 * @return -1,0,1
	 */
	public int compareTo(Score<Location> loc) {
		if (this.score < loc.score) {
			return -1;
		} else if (this.score == loc.score) {
			return 0;
		} else {
			return 1;
		}

	}

	/**
	 * Incrementa la puntuacion del jugador en una cantidad igual a la distancia
	 * entre la posicion previa y la posicion loc
	 * 
	 * @param loc posicion
	 */
	public void score(Location loc) {
		if (previousLocation == null) {
			score = 0;
			previousLocation = new Location(loc);
		} else {
			score = score + previousLocation.distance(loc);
			previousLocation = new Location(loc);
		}
	}
}
