/**
 * 
 */
package model.persistence;

import model.Location;

/**
 * Esta interfaz representa un jugador de Minetest.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public interface IPlayer {

	/**
	 * Obtiene el nivel de salud del jugador
	 * 
	 * @return salud
	 */
	public double getHealth();

	/**
	 * Obtiene una copia del inventorio del jugador
	 * 
	 * @return inventario
	 */
	public IInventory getInventory();

	/**
	 * Obtiene la posicion del jugador
	 * 
	 * @return posicion
	 */
	public Location getLocation();

	/**
	 * Obtiene el nombre del jugador
	 * 
	 * @return nombrePlayer
	 */
	public String getName();
}
