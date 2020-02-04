/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;

import model.exceptions.BadInventoryPositionException;

/**
 * Clase Inventory donde están sus métodos y sus atributos
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 *
 */
public class Inventory {

	/**
	 * Variable que hace referencia a la asociacion con ItemStack
	 */
	private ItemStack inHand;

	/**
	 * Variable que hace referencia a la agregación
	 */
	private ArrayList<ItemStack> items;

	/**
	 * Constructor que crea un inventario vacío
	 */
	public Inventory() {
		items = new ArrayList<ItemStack>();
	}

	/**
	 * Función que añade una pila de Items
	 * 
	 * @param items Item que queremos añadir
	 * @return int Num de items añadidos
	 */
	public int addItem(ItemStack items) {
		this.items.add(items);
		return items.getAmount();
	}

	/**
	 * Funcion que vacia el inventario, incluyendo item de la mano
	 */
	public void clear() {
		inHand = null;
		items.clear();
	}

	/**
	 * Funcion que elimina el item del espacio indicado
	 * 
	 * @param slot Espacio del item que se desea eliminar
	 * @throws BadInventoryPositionException Si la posicion del inventorio no existe
	 */
	public void clear(int slot) throws BadInventoryPositionException {
		if (slot > ItemStack.MAX_STACK_SIZE || slot < 0 || slot >= items.size()) {
			throw new BadInventoryPositionException(slot);
		}
		items.remove(slot);
	}

	/**
	 * Funcion que obtiene el índice del primer espacio del inventario que contenga
	 * items de ese tipo
	 * 
	 * @param type Tipo del material que queremos
	 * @return int Indice del item
	 */
	public int first(Material type) {
		for (ItemStack i : items) {
			if (i.getType().equals(type)) {
				return items.indexOf(i);
			}
		}
		return -1;
	}

	/**
	 * Obtiene los items en una posición dada del inventario. Devuelve null si la
	 * posición no existe.
	 * 
	 * @param slot Posición del objeto a devolver
	 * @return ItemStack Item quee stá en el slot
	 */
	public ItemStack getItem(int slot) {
		if (items.isEmpty() || slot >= items.size() || slot < 0) {
			return null;
		} else {
			return items.get(slot);
		}

	}

	/**
	 * Funcion que obtiene los items que el jugador lleva en la mano
	 * 
	 * @return inHand Objeto que lleva el jugador en la mano
	 */

	public ItemStack getItemInHand() {
		return inHand;
	}

	/**
	 * Obtiene el tamaño del inventario, sin incluir el item ‘inHand’.
	 * 
	 * @return int Tamaño inventario
	 */
	public int getSize() {
		return items.size();
	}

	/**
	 * Guarda los items en la posición dada del inventario
	 * 
	 * @param pos   Posicion del item que queremos añadir
	 * @param items Item que queremos añadir
	 * @throws BadInventoryPositionException Si la posicion del inventario es
	 *                                       erronea
	 */
	public void setItem(int pos, ItemStack items) throws BadInventoryPositionException {
		if (pos >= this.items.size() || pos < 0 || pos > ItemStack.MAX_STACK_SIZE) {
			throw new BadInventoryPositionException(pos);
		}
		this.items.set(pos, items);
	}

	/**
	 * Asigna los items a llevar en la mano
	 * 
	 * @param items Objeto que queremos añadir en la mano
	 */
	public void setItemInHand(ItemStack items) {
		inHand = items;
	}

	/**
	 * Funcion toString
	 * 
	 * @return resultado Cadena que representa el inventario
	 */
	public String toString() {
		String resultado = "(inHand=" + inHand + ",[";
		int tamArray = items.size();
		int cont = 1;

		for (ItemStack i : items) {
			if (cont == tamArray) {
				resultado = resultado + i.toString();
			} else {
				resultado = resultado + i.toString() + ", ";
				cont++;
			}
		}

		resultado = resultado + "])";

		return resultado;
	}

	/**
	 * Funcion hashCode
	 * 
	 * @return result Resultado del hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inHand == null) ? 0 : inHand.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto a comparar
	 * @return Boolean True si son iguales, false si no
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inventory other = (Inventory) obj;
		if (inHand == null) {
			if (other.inHand != null)
				return false;
		} else if (!inHand.equals(other.inHand))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

}
