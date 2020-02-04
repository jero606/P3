/**
 * 
 */
package model;

import model.exceptions.WrongMaterialException;

/**
 * Clase de Block con sus métodos y sus funciones
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public abstract class Block {
	/**
	 * Variable que hace referencia a la asociación con Material
	 */
	private Material type;

	/**
	 * Constructor de Block
	 * 
	 * @param m Material del bloque
	 * @throws WrongMaterialException Si el material no es del tipo bloque
	 */
	public Block(Material m) throws WrongMaterialException {
		if (!m.isBlock()) {
			throw new WrongMaterialException(m);
		} else {
			this.type = m;
		}
	}

	/**
	 * Constructor de copia
	 * 
	 * @param b Bloque
	 */
	protected Block(Block b) {
		type = b.type;
	}

	/**
	 * Getter del tipo
	 * 
	 * @return type El tipo
	 */
	public Material getType() {
		return type;
	}

	/**
	 * Devolver una copia del objeto ‘this’ al ser invocado
	 *
         * @return copia del bloque
	 */
	public abstract Block clone();

	/**
	 * Función hashCode
	 * 
	 * @return result Resultado del hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto
	 * @return boolean Si son iguales, true, si no, false
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * Funcion toString
	 * 
	 * @return String La cadena
	 */
	public String toString() {
		return "[" + type + "]";
	}
}
