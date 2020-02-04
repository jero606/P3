/**
 * 
 */
package model.persistence;

import model.ItemStack;

/**
 * Esta interfaz representa un inventario de Minetest.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public interface IInventory {

	/**
	 * Obtiene el item que se encuentra en la pos
	 * 
	 * @param pos Posicion
	 * @return item
	 */
	public ItemStack getItem(int pos);

	/**
	 * Obtiene el tamñao del invventerio
	 * 
	 * @return tamaño
	 */
	public int getSize();

	/**
	 * Obtiene el item en la mano
	 * 
	 * @return item en la mano
	 */
	public ItemStack inHandItem();
}
