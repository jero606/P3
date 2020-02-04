/**
 * 
 */
package model.persistence;

import model.Location;
import model.entities.Player;

/**
 * Esta clase implementa la interfaz IPlayer apoyándose en los métodos de
 * Player.
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 *
 */
public class PlayerAdapter implements IPlayer {

	/**
	 * Variable del player
	 */
	private Player player;
	/**
	 * Variable del inventario
	 */
	private IInventory inventory;

	/**
	 * Constructor sobrecargado
	 * 
	 * @param p jugador
	 */
	public PlayerAdapter(Player p) {
		this.player = p;
		this.inventory = new InventoryAdapter(p.getInventory());
	}

	/**
	 * Obtiene la salud del player
	 * 
	 * @return salud
	 */
	public double getHealth() {
		return player.getHealth();
	}

	/**
	 * Obtiene una copia del inventario del jugador
	 * 
	 * @return copia del inventario
	 */
	public IInventory getInventory() {
		//IInventory i = new InventoryAdapter((Inventory) inventory);
		return inventory;
	}

	/**
	 * Obtiene posicion del pplayer
	 * 
	 * @return posicion
	 */
	public Location getLocation() {
		return player.getLocation();
	}

	/**
	 * Obtiene el nombre del player
	 * 
	 * @return nombre
	 */
	public String getName() {
		return player.getName();
	}
}
