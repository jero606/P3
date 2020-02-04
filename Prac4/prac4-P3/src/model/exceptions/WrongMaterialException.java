/**
 * 
 */
package model.exceptions;

import model.Material;

/**
 * Clase del error WrongMaterial
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 */
public class WrongMaterialException extends Exception {
	/**
	 * Constructor
	 * 
	 * @param m Material
	 */
	public WrongMaterialException(Material m) {
		super("Wrong material: " + m);
	}
}
