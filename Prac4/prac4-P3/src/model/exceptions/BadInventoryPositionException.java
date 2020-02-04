/**
 * 
 */
package model.exceptions;

/**
 * Clase del error BadInventoryPosition
 * 
 * @author jero
 * @author 74371079G
 *
 */
public class BadInventoryPositionException extends Exception {
	/**
	 * Constructor de la excepcion
	 * 
	 * @param pos Posicion del inventario
	 */
	public BadInventoryPositionException(int pos) {
		super("Posición no válida: " + pos);
	}
}
