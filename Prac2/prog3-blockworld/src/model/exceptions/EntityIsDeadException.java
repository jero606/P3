/**
 * 
 */
package model.exceptions;

/**
 * Clase del error EntityIsDead
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 */
public class EntityIsDeadException extends Exception {
	/**
	 * Constructor
	 */
	public EntityIsDeadException() {
		super("El jugador está muerto.");
	}
}
