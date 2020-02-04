package model.exceptions.score;

/**
 * Clase de la excepcion EmptyRankingException
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 *
 */
@SuppressWarnings("serial")
public class EmptyRankingException extends Exception {

	/**
	 * Constructor de la excepción
	 */
	public EmptyRankingException() {
		super("Ranking de puntuaciones vacío");
	}
}
