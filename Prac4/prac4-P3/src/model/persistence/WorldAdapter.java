/**
 * 
 */
package model.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import model.Block;
import model.BlockFactory;
import model.ItemStack;
import model.Location;
import model.Material;
import model.World;
import model.entities.Creature;
import model.exceptions.BadLocationException;

/**
 * Esta clase implementa la interfaz IWorld apoyándose en los métodos de World.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class WorldAdapter implements IWorld {

	/**
	 * Variable de world
	 */
	private World world;

	/**
	 * Variable de player
	 */
	private IPlayer player;

	/**
	 * Constructor de WorldAdapter
	 * 
	 * @param w mundo
	 */
	public WorldAdapter(World w) {
		world = w;
		this.player = new PlayerAdapter(w.getPlayer());
	}

	/**
	 * Devuelve el limite negativo
	 * 
	 * @return limite negativo
	 */
	public int getNegativeLimit() {
		int limitePositivo = 0;
		int limiteNegativo = 0;

		if (world.getSize() % 2 != 0) {
			limitePositivo = (world.getSize() - 1) / 2;
			limiteNegativo = 0 - limitePositivo;
		} else {
			limitePositivo = world.getSize() / 2;
			limiteNegativo = 0 - (limitePositivo - 1);
		}
		return limiteNegativo;
	}

	/**
	 * Devuelve el player
	 * 
	 * @return player
	 */
	public IPlayer getPlayer() {
		return player;
	}

	/**
	 * Devuelve el limite positivo
	 * 
	 * @return limite positivo
	 */
	public int getPositiveLimit() {
		int limitePositivo = 0;

		if (world.getSize() % 2 != 0) {
			limitePositivo = (world.getSize() - 1) / 2;
		} else {
			limitePositivo = world.getSize() / 2;
		}
		return limitePositivo;
	}

	/**
	 * Devuelve todas las criaturas
	 * 
	 * @param loc posicion
	 * @return lista de las criaturas
	 */
	public List<Creature> getCreatures(Location loc) {
		List<Creature> creatures = new ArrayList<Creature>();
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 16; k++) {
					Location pos = new Location(loc.getWorld(), loc.getX() + i, loc.getY() + j, loc.getZ() + k);
					// if(Location.check(loc)) {
					try {
						if (loc.getWorld().getCreatureAt(pos) != null) {
							creatures.add(loc.getWorld().getCreatureAt(pos));
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					// }
				}
			}
		}
		return creatures;
	}

	/**
	 * Obtiene un mapa de objetos ItemStack indexado por su posición en el mundo
	 * 
	 * @param loc posicion
	 * @return mapa de los objetos
	 */
	public Map<Location, ItemStack> getItems(Location loc) {
		Map<Location, ItemStack> items = new HashMap<Location, ItemStack>();
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 16; k++) {
					Location pos = new Location(loc.getWorld(), loc.getX() + i, loc.getY() + j, loc.getZ() + k);

					try {
						if (loc.getWorld().getItemsAt(pos) != null) {
							items.put(pos, loc.getWorld().getItemsAt(pos));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		return items;
	}

	/**
	 * Obtiene un mapa ordenado de bloques
	 * 
	 * @param loc posicion
	 * @return mapa ordenado de bloques
	 */
	public NavigableMap<Location, Block> getMapBlock(Location loc) {
		NavigableMap<Location, Block> bloques = new TreeMap<Location, Block>();

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 16; k++) {
					Location pos = new Location(loc.getWorld(), loc.getX() + i, loc.getY() + j, loc.getZ() + k);
					Location relativa = new Location(loc.getWorld(), i, j, k);
					try {
						if(loc.getWorld().getBlockAt(pos) == null && Location.check(pos)) {
							bloques.put(relativa, BlockFactory.createBlock(Material.AIR));
						}
						else {
							bloques.put(relativa, loc.getWorld().getBlockAt(pos));
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bloques;
	}
}
