/**
 * 
 */
package model.exceptions;

/**
 * Clase del error StackSize
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 */
public class StackSizeException extends Exception {
	/**
	 * Constructor
	 */
	public StackSizeException() {
		super("Tamaño pila inválido");
	}
}
