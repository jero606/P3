/**
 * 
 */
package model;

import model.exceptions.StackSizeException;

/**
 * Clase ItemStack en la que se crea una pila de un tipo de material y una
 * cantidad
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 */
public class ItemStack {
	/**
	 * Variable que indica el numero de items en la pila
	 */
	private int amount;

	/**
	 * Variable que indica el tipo de material
	 */
	private Material type;

	/**
	 * Variable del maximo numero de objetos en la pila que se pueden crear
	 */
	public final static int MAX_STACK_SIZE = 64;

	/**
	 * Constructor del ItemStack
	 * 
	 * @param type   Tipo del material
	 * @param amount cantidad del material
	 * @throws StackSizeException si el tamaño del item es erroneo
	 */
	public ItemStack(Material type, int amount) throws StackSizeException {
		if ((amount < 1 || amount > ItemStack.MAX_STACK_SIZE) || ((type.isTool() || type.isWeapon()) && amount != 1)) {
			throw new StackSizeException();
		} else {
			if (type.isTool() || type.isWeapon()) {
				this.type = type;
				this.amount = 1;
			} else {
				this.type = type;
				this.amount = amount;
			}
		}
	}

	/**
	 * Constructor de copia
	 * 
	 * @param item objeto a copiar
	 */
	public ItemStack(ItemStack item) {
		this.type = item.type;
		this.amount = item.amount;
	}

	/**
	 * Getter del amount
	 * 
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Getter del type
	 * 
	 * @return the type
	 */
	public Material getType() {
		return type;
	}

	/**
	 * Setter del amount
	 * 
	 * @param amount Cantidad a cambiar
	 * @throws StackSizeException si el tamaño es erroneo
	 */
	public void setAmount(int amount) throws StackSizeException {
		if ((amount < 1 || amount > ItemStack.MAX_STACK_SIZE) || ((type.isTool() || type.isWeapon()) && amount != 1)) {
			throw new StackSizeException();
		} else {
			this.amount = amount;
		}
	}

	/**
	 * Funcion que devuelve una cadena con el formato (type,amount)
	 * 
	 * @return String La cadena (type,amount)
	 */
	public String toString() {
		return "(" + type + "," + amount + ")";
	}

	/**
	 * Funcion hashCode
	 * 
	 * @return result El resultado
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto
	 * @return boolean Si son iguales o no
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemStack other = (ItemStack) obj;
		if (amount != other.amount)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
