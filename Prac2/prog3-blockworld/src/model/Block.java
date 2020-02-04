/**
 * 
 */
package model;

import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

/**
 * Clase de que representa un Block. Un bloque está formado por un tipo de
 * material para bloques y puede contener en su interior una pila de items.
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 *
 */
public class Block {
	/**
	 * Variable que hace referencia a la asociación con ItemStack
	 */
	private ItemStack drops;

	/**
	 * Variable que hace referencia a la asociación con Material
	 */
	private Material type;

	/**
	 * Constructor de Block
	 * 
	 * @param type Tipo del bloque
	 * @throws WrongMaterialException Si el material no es adecuado
	 */
	public Block(Material type) throws WrongMaterialException {
		if (!type.isBlock()) {
			throw new WrongMaterialException(type);
			// System.err.println("Error");
		} else {
			this.type = type;
		}
	}

	/**
	 * Constructor de copia
	 * 
	 * @param b1 Bloque a copiar
	 */
	public Block(Block b1) {
		this.type = b1.getType();
		this.drops = b1.getDrops();
	}

	/**
	 * Getter de los drops
	 * 
	 * @return drops Los drops
	 */
	public ItemStack getDrops() {
		return drops;
	}

	/**
	 * Setter de los drops
	 * 
	 * @param type   Tipo del material
	 * @param amount Cantidad del material
	 * @throws StackSizeException si el tamaño no es el adecuado
	 */
	public void setDrops(Material type, int amount) throws StackSizeException {

		if (this.type != Material.CHEST && amount != 1) {
			throw new StackSizeException();
		} else if (amount < 1 || amount > ItemStack.MAX_STACK_SIZE) {
			throw new StackSizeException();
		} else {
			drops = new ItemStack(type, amount);
		}
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
	 * Funcion toString
	 * 
	 * @return String La cadena
	 */
	public String toString() {
		return "[" + type + "]";
	}

	/**
	 * Función hashCode()
	 * 
	 * @return result Resultado
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj objeto
	 * @return boolean True si son iguales, false si no
	 */
	@Override
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
}
