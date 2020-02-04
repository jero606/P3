/**
 * 
 */
package model.exceptions.score;

/**
 * Clase del error ScoreException
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */

@SuppressWarnings("serial")
public class ScoreException extends RuntimeException {

	/**
	 * Constructor
	 * 
	 * @param n mensaje
	 */
	public ScoreException(String n) {
		super(n);
	}
}
