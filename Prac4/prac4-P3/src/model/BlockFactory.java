/**
 * 
 */
package model;

import model.exceptions.WrongMaterialException;

/**
 * Clase factoría que tiene como cometido crear bloques según el tipo de
 * material utilizado
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 */
public class BlockFactory {

	/**
	 * Crea bloques según el tipo de material utilizado
	 * 
	 * @param m Material del bloque a construir
	 * @return Block Puede ser Liquido o Solido
	 * @throws WrongMaterialException Si el material no es correcto
	 */
	public static Block createBlock(Material m) throws WrongMaterialException {
		if (m.isLiquid()) {
			return new LiquidBlock(m);
		} else {
			return new SolidBlock(m);
		}
	}
}
