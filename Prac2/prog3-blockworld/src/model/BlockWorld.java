/**
 * 
 */
package model;

import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;

/**
 * Clase de BlockWorld con sus atributos y metodos
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 *
 */
public class BlockWorld {

	/**
	 * Variable de la instancia
	 */
	private static BlockWorld instance;

	/**
	 * Variable qeu hace referencia a la asociación
	 */
	private World world;

	/**
	 * Obtiene una referencia a la única instancia de esta clase.
	 * 
	 * @return instance Instancia del BlockWorld
	 */
	public static BlockWorld getInstance() {
		if (instance == null) {
			instance = new BlockWorld();
		}
		return instance;
	}

	/**
	 * Constructor de BlockWorld
	 */
	private BlockWorld() {
		world = null;
	}

	/**
	 * Mueve al jugador a la posición adyacente (x+dx, y+dy, z+dz), haciendo que
	 * recoja los items de esa posición, si los hubiere.
	 * 
	 * @param player Jugador
	 * @param dx     Posicion x
	 * @param dy     Posicion y
	 * @param dz     Posicion z
	 * @throws EntityIsDeadException Si jugador esta muerto
	 * @throws BadLocationException  Si la posicion es erronea
	 */
	public void movePlayer(Player player, int dx, int dy, int dz) throws EntityIsDeadException, BadLocationException {

		Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getX() + dx,
				player.getLocation().getY() + dy, player.getLocation().getZ() + dz);
		player.move(dx, dy, dz);

		if (world.getItemsAt(loc) != null) {
			player.getInventory().addItem(world.getItemsAt(loc));
			world.removeItemsAt(loc);
		}
	}

	/**
	 * Hace que el jugador use el item que lleva en la mano ‘times’ veces
	 * 
	 * @param player Jugador
	 * @param times  Veces a usar el objeto
	 * @throws EntityIsDeadException Si el jugador esta muerto
	 */
	public void useItem(Player player, int times) throws EntityIsDeadException {
		player.useItemInHand(times);
	}

	/**
	 * Invoca al método Player.selectItem() con el parámetro ‘pos’.
	 * 
	 * @param player Jugador
	 * @param pos    Posicion del objeto a usar
	 * @throws BadInventoryPositionException Si la posicion no existe
	 */
	public void selectItem(Player player, int pos) throws BadInventoryPositionException {
		player.selectItem(pos);
	}

	/**
	 * Invoca al constructor de World
	 * 
	 * @param seed Semilla del mundo
	 * @param size Tamaño del mundo
	 * @param name Nombre del mundo
	 * @return world El mundo creado
	 * @throws BadLocationException
	 * @throws StackSizeException
	 */
	public World createWorld(long seed, int size, String name) {
		world = new World(seed, size, name);
		return world;
	}

	/**
	 * Obtiene una cadena con información sobre el jugador y lo que hay en las
	 * posiciones adyacentes a él
	 * 
	 * @param player Jugador
	 * @return resultado Cadena con la informacion
	 * @throws BadLocationException si la posicion es erronea
	 */
	public String showPlayerInfo(Player player) {
		String resultado="";
		try {
			resultado = player.toString() + "\n" + world.getNeighbourhoodString(world.getPlayer().getLocation());
			return resultado;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
		
	}
}
