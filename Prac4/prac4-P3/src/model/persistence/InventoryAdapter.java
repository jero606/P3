/**
 * 
 */
package model.persistence;

import model.Inventory;
import model.ItemStack;

/**
 * Esta clase implementa los métodos del interfaz IInventory, apoyándose en los
 * métodos de la clase Inventory.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class InventoryAdapter implements IInventory {

	/**
	 * Variable del inventario
	 */
	private Inventory inventory;

	/**
	 * Constructor sobrecargado
	 * 
	 * @param inv inventario
	 */
	public InventoryAdapter(Inventory inv) {
		//this.inventory = new Inventory(inv);
		inventory = inv;
	}

	/**
	 * Obtiene el item que se encuentra en pos
	 * 
	 * @param pos posicion
	 * @return item
	 */
	public ItemStack getItem(int pos) {
		return inventory.getItem(pos);
	}

	/**
	 * Obtiene el tamaño del inventario
	 * 
	 * @return tamaño del inv
	 */
	public int getSize() {
		return inventory.getSize();
	}

	/**
	 * Obtiene el item del inventario que el player lleva en la mano
	 * 
	 * @return item
	 */
	public ItemStack inHandItem() {
		return inventory.getItemInHand();
	}
}
