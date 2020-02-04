/**
 * 
 */
package model.score;

/**
 * Es una clase abstracta genérica que implementa el interfaz Comparable. El
 * tipo ‘T’ determinará el tipo de puntuación que se quiere definir.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 * @param <T> tipo de objeto
 *
 */
public abstract class Score<T> implements Comparable<Score<T>> {

	/**
	 * Nombre de la puntuacion
	 */
	private String playerName;

	/**
	 * Puntuacion
	 */
	protected double score;

	/**
	 * Metodo abstracto
	 * 
	 * @param e Objeto
	 */
	public abstract void score(T e);

	/**
	 * Constructor a partir del nombre del jugador
	 * 
	 * @param name del jugador
	 */
	public Score(String name) {
		this.playerName = name;
		score = 0;
	}

	/**
	 * Getter del name
	 * 
	 * @return name
	 */
	public String getName() {
		return playerName;
	}

	/**
	 * Getter del score
	 * 
	 * @return score
	 */
	public double getScoring() {
		return score;
	}

	/**
	 * Metodo toString()
	 * 
	 * @return cadena con el nombre y la puntuacion
	 */
	public String toString() {
		return playerName + ":" + score;
	}
}
