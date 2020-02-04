/**
 * 
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import model.entities.Animal;
import model.entities.Creature;
import model.entities.Player;
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

		if (world.getBlockAt(loc) != null && world.getBlockAt(loc).getType().isLiquid()) {
			player.move(dx, dy, dz);
			player.damage(world.getBlockAt(loc).getType().getValue());
		} else {
			player.move(dx, dy, dz);
		}

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
		ItemStack itemInHand = player.getInventory().getItemInHand();
		double danyo = 0;

		if (itemInHand == null || itemInHand.getType().isEdible() || !Location.check(player.getOrientation())) {
			player.useItemInHand(times);
		} else if (itemInHand != null && itemInHand.getType().isBlock()) {
			danyo = 0.1 * times;

		} else if (itemInHand != null && (itemInHand.getType().isTool() || itemInHand.getType().isWeapon())) {
			danyo = itemInHand.getType().getValue() * times;
		}

		try {
			if (world.getBlockAt(player.getOrientation()) != null
					&& world.getBlockAt(player.getOrientation()).getType().isBlock()) {
				if (((SolidBlock) world.getBlockAt(player.getOrientation())).breaks(danyo)) {
					player.useItemInHand(times);
					world.destroyBlockAt(player.getOrientation());
				} else {
					player.useItemInHand(times);
				}
			} else if (world.getCreatureAt(player.getOrientation()) != null) {
				player.useItemInHand(times);
				world.getCreatureAt(player.getOrientation()).damage(danyo);
				if (world.getCreatureAt(player.getOrientation()).isDead()) {
					if (world.getCreatureAt(player.getOrientation()).getSymbol() == 'L') {
						Animal animal = (Animal) world.getCreatureAt(player.getOrientation());
						ItemStack carne = animal.getDrops();
						world.killCreature(player.getOrientation());
						world.addItems(player.getOrientation(), carne);

					} else {
						world.killCreature(player.getOrientation());
					}

				} else if (!world.getCreatureAt(player.getOrientation()).isDead()
						&& world.getCreatureAt(player.getOrientation()).getSymbol() == 'M') {
					player.damage(times * 0.5);
				}
			} else if (world.getBlockAt(player.getOrientation()) == null
					&& world.getCreatureAt(player.getOrientation()) == null
					&& world.getItemsAt(player.getOrientation()) == null && itemInHand.getType().isBlock()) {
				Block b1 = new SolidBlock(itemInHand.getType());
				world.addBlock(player.getOrientation(), b1);
				player.useItemInHand(times);
			} else if (Location.check(player.getOrientation())) {
				player.useItemInHand(times);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Invoca al metodo orientate() del juador
	 * 
	 * @param player Jugador
	 * @param dx     valor x
	 * @param dy     valor y
	 * @param dz     valor z
	 * @throws EntityIsDeadException si la entidad esta muerta
	 * @throws BadLocationException  si la posicion es erronea
	 */
	public void orientatePlayer(Player player, int dx, int dy, int dz)
			throws EntityIsDeadException, BadLocationException {
		player.orientate(dx, dy, dz);
	}

	/**
	 * Abre el fichero de entrada indicado y ejecuta cada una de sus órdene
	 * 
	 * @param path Ruta del fichero
	 * @throws FileNotFoundException si el fichero no se encuentra
	 */
	public void playFile(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path));
		play(sc);
		sc.close();

	}

	/**
	 * Funcion playFromConsole
	 */
	public void playFromConsole() {
		Scanner sc = new Scanner(System.in);
		play(sc);
	}

	/**
	 * Ejecuta las órdenes que va leyendo, línea a línea
	 * 
	 * @param sc Scanner con el texto del fichero
	 */
	private void play(Scanner sc) {

		int constructorWorld = 0;
		while (sc.hasNext() || (world != null && world.getPlayer().isDead())) {
			try {
				if (constructorWorld == 0) {
					int seed = sc.nextInt();
					int size = sc.nextInt();
					String name = sc.nextLine();
					constructorWorld = 1;
					world = createWorld(seed, size, name);
				}
				String orden = sc.next();
				if (orden.equals("move")) {
					int x = sc.nextInt();
					int y = sc.nextInt();
					int z = sc.nextInt();
					movePlayer(world.getPlayer(), x, y, z);
				} else if (orden.equals("orientate")) {
					int x = sc.nextInt();
					int y = sc.nextInt();
					int z = sc.nextInt();
					orientatePlayer(world.getPlayer(), x, y, z);
				} else if (orden.equals("useItem")) {
					int times = sc.nextInt();
					useItem(world.getPlayer(), times);
				} else if (orden.equals("selectItem")) {
					int pos = sc.nextInt();
					selectItem(world.getPlayer(), pos);
				} else if (orden.equals("show")) {
					System.out.println(showPlayerInfo(world.getPlayer()));
				} else {
					System.err.println("Comando " + orden + " erróneo.");
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
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
	 */
	public String showPlayerInfo(Player player) {
		String resultado = "";
		try {
			resultado = player.toString() + "\n" + world.getNeighbourhoodString(world.getPlayer().getLocation());
			return resultado;
		} catch (BadLocationException e) {
			System.err.println(e.getMessage());
		}
		return resultado;

	}
}
