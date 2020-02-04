/**
 * 
 */
package model.exceptions;

/**
 * Clase del error UnknownGameCommandException
 * 
 * @author jero
 * @author 74371079G
 */
public class UnknownGameCommandException extends Exception {

	/**
	 * Constructor de la excepcion
	 * 
	 * @param name Comando erroneo
	 */
	public UnknownGameCommandException(String name) {
		super(name);
	}
}
