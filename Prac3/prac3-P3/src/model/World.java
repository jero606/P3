/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.util.noise.CombinedNoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import model.World.HeightMap;
import model.entities.Animal;
import model.entities.Creature;
import model.entities.LivingEntity;
import model.entities.Monster;
import model.entities.Player;
import model.exceptions.BadLocationException;
import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

/**
 * Clase de world con sus metodos y sus atributos
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class World {

	/**
	 * name of the world
	 */
	private String name;

	/**
	 * Size of the world in the (x,z) plane.
	 */
	private int worldSize;

	/**
	 * World seed for procedural world generation
	 */
	private long seed;

	/**
	 * bloques de este mundo
	 */
	private Map<Location, Block> blocks;

	/**
	 * Items depositados en algún lugar de este mundo.
	 */
	private Map<Location, ItemStack> items;

	/**
	 * Monstruos en algun lugar del mundo
	 */
	private Map<Location, Creature> creatures;

	/**
	 * El jugador
	 */
	private Player player;

	/**
	 * Esta clase interna representa un mapa de alturas bidimiensional que nos
	 * servirá para guardar la altura del terreno (coordenada 'y') en un array
	 * bidimensional, e indexarlo con valores 'x' y 'z' positivos o negativos.
	 * 
	 * la localización x=0,z=0 queda en el centro del mundo. Por ejemplo, un mundo
	 * de tamaño 51 tiene su extremo noroeste a nivel del mar en la posición
	 * (-25,63,-25) y su extremo sureste, también a nivel del mar, en la posición
	 * (25,63,25). Para un mundo de tamaño 50, estos extremos serán (-24,63,-24) y
	 * (25,63,25), respectivamente.
	 * 
	 * Por ejemplo, para obtener la altura del terreno en estas posiciones,
	 * invocaríamos al método get() de esta clase: get(-24,24) y get(25,25)
	 * 
	 * de forma análoga, si queremos modificar el valor 'y' almacenado, haremos
	 * set(-24,24,70)
	 *
	 */
	class HeightMap {
		double[][] heightMap;

		int positiveWorldLimit;
		int negativeWorldLimit;

		/**
		 * Constructor HeighMap
		 * 
		 * @param worldsize Tamaño del mundo
		 */
		HeightMap(int worldsize) {
			heightMap = new double[worldsize][worldsize];
			positiveWorldLimit = worldsize / 2;
			negativeWorldLimit = (worldsize % 2 == 0) ? -(positiveWorldLimit - 1) : -positiveWorldLimit;
		}

		/**
		 * obtiene la atura del terreno en la posición (x,z)
		 * 
		 * @param x coordenada 'x' entre 'positiveWorldLimit' y 'negativeWorldLimit'
		 * @param z coordenada 'z' entre 'positiveWorldLimit' y 'negativeWorldLimit'
		 * @return la altura
		 */
		double get(double x, double z) {
			return heightMap[(int) x - negativeWorldLimit][(int) z - negativeWorldLimit];
		}

		/**
		 * Setter
		 * 
		 * @param x valor x
		 * @param z valor y
		 * @param y valor z
		 */
		void set(double x, double z, double y) {
			heightMap[(int) x - negativeWorldLimit][(int) z - negativeWorldLimit] = y;
		}

	}

	/**
	 * Coordenadas 'y' de la superficie del mundo. Se inicializa en generate() y
	 * debe actualizarse cada vez que el jugador coloca un nuevo bloque en una
	 * posición vacía Puedes usarlo para localizar el bloque de la superficie de tu
	 * mundo.
	 */
	private HeightMap heightMap;

	/**
	 * Constructor del mundo
	 * 
	 * @param seed Semilla
	 * @param size Tamaño
	 * @param name Nombre del mundo
	 */
	public World(long seed, int size, String name) {
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		this.seed = seed;
		this.worldSize = size;
		this.name = name;
		blocks = new HashMap<Location, Block>();
		items = new HashMap<Location, ItemStack>();
		creatures = new HashMap<Location, Creature>();
		generate(seed, size);
	}

	/**
	 * Constructor que asigna un nombre a un mundo
	 * 
	 * @param name Nombre del mundo
	 */
	public World(String name) {
		this.name = name;
	}

	/**
	 * Obtiene el bloque que está en la posición indicada, o null si no hay ningún
	 * bloque ahí.
	 * 
	 * @param loc Posicion de la que quermeos el bloque
	 * @return bloque Bloque que hay en la posicion
	 * @throws BadLocationException Si la posicion no pertenece al mundo
	 */
	public Block getBlockAt(Location loc) throws BadLocationException {
		if (loc == null || (!this.equals(loc.getWorld())) || (loc.getWorld() == null)) {
			throw new BadLocationException("Bad Location");
		}

		Block bloque = blocks.get(loc);
		if (bloque != null) {
			return bloque.clone();
		} else {
			return null;
		}
	}

	/**
	 * Obtiene los items que están en la posición dada, o null si no había items en
	 * esa posición.
	 * 
	 * @param loc Posicion de la que queremos obtener el objeto
	 * @return item Objeto que está en la posicion.
	 * @throws BadLocationException Si la posicion no pertenece al mundo
	 */
	public ItemStack getItemsAt(Location loc) throws BadLocationException {
		if (loc == null || (!this.equals(loc.getWorld())) || (loc.getWorld() == null)) {
			throw new BadLocationException(name);
		}
		ItemStack item = items.get(loc);
		return item;
	}

	/**
	 * Getter de una criatura en una posicion
	 * 
	 * @param loc Posicion
	 * @return creature Criatura
	 * @throws BadLocationException Si la posicion es errónea
	 */
	public Creature getCreatureAt(Location loc) throws BadLocationException {
		if (loc == null || (!this.equals(loc.getWorld())) || (loc.getWorld() == null)) {
			throw new BadLocationException(name);
		}
		Creature c1 = creatures.get(loc);
		return c1;
	}

	/**
	 * Devuelve todas las criaturas vivas que estén ocupando posicion adyacentes a
	 * la dada
	 * 
	 * @param loc Posicion
	 * @return criaturas Collection de las criaturas
	 * @throws BadLocationException si la posicion es errónea
	 */
	public Collection<Creature> getNearbyCreatures(Location loc) throws BadLocationException {
		if ((!this.equals(loc.getWorld())) || (loc.getWorld() == null)) {
			throw new BadLocationException(name);
		} else {
			Collection<Creature> criaturas = new HashSet<>();
			Set<Location> adyacentes = loc.getNeighborhood();
			for (Location i : adyacentes) {
				if (getCreatureAt(i) != null && !getCreatureAt(i).isDead()) {
					criaturas.add(getCreatureAt(i));
				}
			}
			return criaturas;
		}

	}

	/**
	 * Obtiene la posición que representa el nivel del suelo en la posición (x,*,z)
	 * indicada por el argumento.
	 * 
	 * @param ground Posicion de la que quremos el nivel del suelo
	 * @return l Posicion deseada
	 * @throws BadLocationException Si la posicion no es de este mundo
	 */
	public Location getHighestLocationAt(Location ground) throws BadLocationException {
		if (!this.equals(ground.getWorld()) || ground.getWorld() == null) {
			throw new BadLocationException(name);
		} else {
			double altura = heightMap.get(ground.getX(), ground.getZ());
			Location l = new Location(ground.getWorld(), ground.getX(), altura, ground.getZ());
			return l;
		}
	}

	/**
	 * Getter
	 * 
	 * @return name Nombre dle mundo
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter
	 * 
	 * @return worldSize Tamaño del mundo
	 */
	public int getSize() {
		return worldSize;
	}

	/**
	 * Getter
	 * 
	 * @return seed La semilla
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * Getter
	 * 
	 * @return player El jugador
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Devuelve una cadena que contiene una representación de las posiciones
	 * adyacentes a una dada.
	 * 
	 * @param loc Posicion de lq que queremos las adyacentes
	 * @return vecinos Cadena con las posiciones adyacentes
	 * @throws BadLocationException Si la posicion es erronea
	 */
	public String getNeighbourhoodString(Location loc) throws BadLocationException {

		String vecinos = "";
		Set<Location> adyacentes = loc.getNeighborhood();
		char chr;
		int espacio = 0;
		int salto = 0;

		if (!this.equals(loc.getWorld()) || loc.getWorld() == null) {
			throw new BadLocationException("Posicion erronea");
		}

		for (int z = -1; z <= 1; z++)
			for (int y = 1; y >= -1; y--)
				for (int x = -1; x <= 1; x++) {
					Location lc = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
					if (adyacentes.contains(lc)) {
						for (Location locat : adyacentes) {
							if (lc.equals(locat)) {

								chr = comprobarvalor(lc);

								vecinos = vecinos + chr;
								espacio++;
								salto++;
								if (espacio == 3) {
									vecinos = vecinos + " ";
									espacio = 0;
								}
								if (salto == 9) {
									vecinos = vecinos + "\n";
									salto = 0;
								}
							}
						}

					} else if (lc.equals(loc)) {
						chr = comprobarvalor(lc);
						vecinos = vecinos + chr;
						espacio++;
						salto++;
					} else {
						chr = 'X';
						vecinos = vecinos + chr;
						espacio++;
						salto++;
						if (espacio == 3) {
							vecinos = vecinos + " ";
							espacio = 0;
						}
						if (salto == 9) {
							vecinos = vecinos + "\n";
							salto = 0;
						}
					}

				}

		return vecinos;
	}

	/**
	 * Obtiene el Symbol que tiene la posicion
	 * 
	 * @param lc Posicion
	 * @return chr Symbol de la posicion
	 */
	public char comprobarvalor(Location lc) {

		char chr = 0;

		try {
			if ((lc.equals(getPlayer().getLocation()) && getBlockAt(lc) == null)
					|| (getBlockAt(lc) != null && getBlockAt(lc).getType().isLiquid()
							&& getPlayer().getLocation().equals(lc))
					|| (getBlockAt(lc) == null && getPlayer().getLocation().equals(lc))) {
				chr = 'P';
			} else if (getBlockAt(lc) == null) {
				if (getItemsAt(lc) == null && getCreatureAt(lc) == null) {
					chr = '.';
				} else if (getCreatureAt(lc) != null && getItemsAt(lc) == null) {
					chr = getCreatureAt(lc).getSymbol();
				} else if (getCreatureAt(lc) == null && getItemsAt(lc) != null) {
					if (getItemsAt(lc).getType().isBlock()) {
						chr = getItemsAt(lc).getType().getSymbol();
						chr = Character.toUpperCase(chr);
					} else {
						chr = getItemsAt(lc).getType().getSymbol();
					}
				}

			} else if (getBlockAt(lc) != null && getBlockAt(lc).getType().isLiquid()) {
				if (getItemsAt(lc) == null && getCreatureAt(lc) == null) {
					chr = getBlockAt(lc).getType().getSymbol();
				} else if (getCreatureAt(lc) != null && getItemsAt(lc) == null) {
					chr = getCreatureAt(lc).getSymbol();
				} else if (getCreatureAt(lc) == null && getItemsAt(lc) != null) {
					if (getItemsAt(lc).getType().isBlock()) {
						chr = getItemsAt(lc).getType().getSymbol();
						chr = Character.toUpperCase(chr);
					} else {
						chr = getItemsAt(lc).getType().getSymbol();
					}
				}

			} else {
				Block bloque = getBlockAt(lc);
				chr = bloque.getType().getSymbol();
			}

			return chr;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		return chr;
	}

	/**
	 * Comprueba si la posicion dada está libre
	 * 
	 * @param loc Posicion para comprobar si esta libre
	 * @return boolean True si esta libre, false si no
	 * @throws BadLocationException si la posicion no pertenece al mundo
	 */
	public boolean isFree(Location loc) throws BadLocationException {
		if (!this.equals(loc.getWorld()) || loc.getWorld() == null) {
			throw new BadLocationException("Posicion no pertenece al mundo");
		} else if (getBlockAt(loc) != null && getBlockAt(loc).getType().isLiquid()
				&& !getPlayer().getLocation().equals(loc) && getCreatureAt(loc) == null) {
			return true;
		} else if ((getBlockAt(loc) != null && getBlockAt(loc).getType().isBlock())
				|| getPlayer().getLocation().equals(loc)
				|| (getCreatureAt(loc) != null && getCreatureAt(loc).getLocation().equals(loc))) {
			return false;
		} else if (getItemsAt(loc) != null) {
			return true;
		} else if (getBlockAt(loc) == null && getItemsAt(loc) == null && getCreatureAt(loc) == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Añade un bloque a la posicion dada
	 * 
	 * @param loc Posicion del bloque
	 * @param b   Bloque a añadir
	 * @throws BadLocationException si la posicion es errónea
	 */
	public void addBlock(Location loc, Block b) throws BadLocationException {
		if (!this.equals(loc.getWorld()) || loc.getWorld() == null || getPlayer().getLocation().equals(loc)
				|| !Location.check(loc)) {
			throw new BadLocationException("Posición errónea");
		} else if (getBlockAt(loc) == null && getItemsAt(loc) == null && getCreatureAt(loc) == null) {
			blocks.put(loc, b);
		} else if (getBlockAt(loc) != null) {
			blocks.replace(loc, blocks.get(loc), b);
		} else if (getItemsAt(loc) != null) {
			items.remove(loc, getItemsAt(loc));
			blocks.put(loc, b);
		} else if (getCreatureAt(loc) != null) {
			creatures.remove(loc, getCreatureAt(loc));
			blocks.put(loc, b);
		}

		// actualizar heightMap

		heightMap.set(loc.getX(), loc.getZ(), loc.getY());
	}

	/**
	 * Añade criatura a este método
	 * 
	 * @param c Criatura a añadir
	 * @throws BadLocationException si la posicion es errónea
	 */
	public void addCreature(Creature c) throws BadLocationException {
		if (!Location.check(c.getLocation()) || !this.equals(c.getLocation().getWorld())
				|| c.getLocation().getWorld() == null
				|| (!isFree(c.getLocation()) && getBlockAt(c.getLocation()) != null
						&& !getBlockAt(c.getLocation()).getType().isLiquid())
				|| getPlayer().getLocation().equals(c.getLocation()) || getCreatureAt(c.getLocation()) != null) {
			throw new BadLocationException("Posicion errónea");
		} else if (getItemsAt(c.getLocation()) != null) {
			// creatures.replace(c.getLocation(), creatures.get(c.getLocation()), c);
			items.remove(c.getLocation(), getItemsAt(c.getLocation()));
			creatures.put(c.getLocation(), c);
		} else {
			creatures.put(c.getLocation(), c);
		}
	}

	/**
	 * Añade una pila de items al mundo
	 * 
	 * @param loc  Posicion del item
	 * @param item item para añadir
	 * @throws BadLocationException si la posicion es errónea
	 */
	public void addItems(Location loc, ItemStack item) throws BadLocationException {
		if (!this.equals(loc.getWorld()) || loc.getWorld() == null || !isFree(loc) || !Location.check(loc)) {
			throw new BadLocationException("Posicion errónea");
		} else if (getItemsAt(loc) != null) {
			items.replace(loc, items.get(loc), item);
		} else {
			items.put(loc, item);
		}
	}

	/**
	 * Destruye el bloque en la posicion dada
	 * 
	 * @param loc posicion del bloque
	 * @throws BadLocationException si la posicion es errónea
	 */
	public void destroyBlockAt(Location loc) throws BadLocationException {
		if (!this.equals(loc.getWorld()) || loc.getWorld() == null || getBlockAt(loc) == null || loc.getY() == 0) {
			throw new BadLocationException("Posicion erronea");
		} else if (getBlockAt(loc) != null && getBlockAt(loc).getType().isBlock()) {
			SolidBlock b1;
			b1 = (SolidBlock) getBlockAt(loc).clone();
			if (b1.getDrops() != null) {
				items.put(loc, b1.getDrops());
				blocks.remove(loc, b1);
			} else {
				blocks.remove(loc, b1);
			}
		} else {
			blocks.remove(loc, getBlockAt(loc));
		}
		
		
		for(double i = loc.getY(); i >= 0; i--) {
			if(getBlockAt(new Location(loc.getWorld(),loc.getX(),i,loc.getZ())) != null) {
				heightMap.set(loc.getX(), loc.getZ(), i);
				break;
			}
		}
		
	}

	/**
	 * Elimina del mundo a la criatura que está en la pos dada.
	 * 
	 * @param loc posicion de la criatura a matar
	 * @throws BadLocationException si la posicion es errónea
	 */
	public void killCreature(Location loc) throws BadLocationException {
		if (!this.equals(loc.getWorld()) || loc.getWorld() == null || getCreatureAt(loc) == null) {
			throw new BadLocationException("Posicion erronea");
		} else {
			creatures.remove(loc, getCreatureAt(loc));
		}
	}

	/**
	 * Elimina los items de la posición dada.
	 * 
	 * @param loc Posicion para borrar su item
	 * @throws BadLocationException Si la posicion no es del mundo o no hay objetos
	 *                              en esa posicion
	 */
	public void removeItemsAt(Location loc) throws BadLocationException {
		if (!this.equals(loc.getWorld()) || (loc.getWorld().getItemsAt(loc) == null)) {
			throw new BadLocationException(name);
		}
		items.remove(loc);
	}

	/**
	 * Funcion toString()
	 * 
	 * @return name String del nombre
	 */
	public String toString() {
		return name;
	}

	/**
	 * Funcion hashCode()
	 * 
	 * @return result Resultado del hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (seed ^ (seed >>> 32));
		result = prime * result + worldSize;
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto
	 * @return boolean True si son iguales, false si no
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		World other = (World) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (seed != other.seed)
			return false;
		if (worldSize != other.worldSize)
			return false;
		return true;
	}

	/**
	 * Genera un mundo nuevo del tamaño size*size en el plano (x,z). Si existían
	 * elementos anteriores en el mundo, serán eliminados. Usando la misma semilla y
	 * el mismo tamaño podemos generar mundos iguales
	 * 
	 * @param seed semilla para el algoritmo de generación.
	 * @param size tamaño del mundo para las dimensiones x y z
	 */
	private void generate(long seed, int size) {

		Random rng = new Random(getSeed());

		blocks.clear();
		creatures.clear();
		items.clear();

		// Paso 1: generar nuevo mapa de alturas del terreno
		heightMap = new HeightMap(size);
		CombinedNoiseGenerator noise1 = new CombinedNoiseGenerator(this);
		CombinedNoiseGenerator noise2 = new CombinedNoiseGenerator(this);
		OctaveGenerator noise3 = new PerlinOctaveGenerator(this, 6);

		System.out.println("Generando superficie del mundo...");
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				double heightLow = noise1.noise(x * 1.3, z * 1.3) / 6.0 - 4.0;
				double heightHigh = noise2.noise(x * 1.3, z * 1.3) / 5.0 + 6.0;
				double heightResult = 0.0;
				if (noise3.noise(x, z, 0.5, 2) / 8.0 > 0.0)
					heightResult = heightLow;
				else
					heightResult = Math.max(heightHigh, heightLow);
				heightResult /= 2.0;
				if (heightResult < 0.0)
					heightResult = heightResult * 8.0 / 10.0;
				heightMap.heightMap[x][z] = Math.floor(heightResult + Location.SEA_LEVEL);
			}
		}

		// Paso 2: generar estratos
		SolidBlock block = null;
		Location location = null;
		Material material = null;
		OctaveGenerator noise = new PerlinOctaveGenerator(this, 8);
		System.out.println("Generando terreno...");
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				double dirtThickness = noise.noise(x, z, 0.5, 2.0) / 24 - 4;
				double dirtTransition = heightMap.heightMap[x][z];
				double stoneTransition = dirtTransition + dirtThickness;
				for (int y = 0; y <= dirtTransition; y++) {
					if (y == 0)
						material = Material.BEDROCK;
					else if (y <= stoneTransition)
						material = Material.STONE;
					else // if (y <= dirtTransition)
						material = Material.DIRT;
					try {
						location = new Location(this, x + heightMap.negativeWorldLimit, y,
								z + heightMap.negativeWorldLimit);
						block = new SolidBlock(material);
						if (rng.nextDouble() < 0.5) // los bloques contendrán item con un 50% de probabilidad
							block.setDrops(block.getType(), 1);
						blocks.put(location, block);
					} catch (WrongMaterialException | StackSizeException e) {
						// Should never happen
						e.printStackTrace();
					}
				}

			}
		}

		// Paso 3: Crear cuevas
		int numCuevas = size * size * 256 / 8192;
		double theta = 0.0;
		double deltaTheta = 0.0;
		double phi = 0.0;
		double deltaPhi = 0.0;

		System.out.print("Generando cuevas");
		for (int cueva = 0; cueva < numCuevas; cueva++) {
			System.out.print(".");
			System.out.flush();
			Location cavePos = new Location(this, rng.nextInt(size), rng.nextInt((int) Location.UPPER_Y_VALUE),
					rng.nextInt(size));
			double caveLength = rng.nextDouble() * rng.nextDouble() * 200;
			// cave direction is given by two angles and corresponding rate of change in
			// those angles,
			// spherical coordinates perhaps?
			theta = rng.nextDouble() * Math.PI * 2;
			deltaTheta = 0.0;
			phi = rng.nextDouble() * Math.PI * 2;
			deltaPhi = 0.0;
			double caveRadius = rng.nextDouble() * rng.nextDouble();

			for (int i = 1; i <= (int) caveLength; i++) {
				cavePos.setX(cavePos.getX() + Math.sin(theta) * Math.cos(phi));
				cavePos.setY(cavePos.getY() + Math.cos(theta) * Math.cos(phi));
				cavePos.setZ(cavePos.getZ() + Math.sin(phi));
				theta += deltaTheta * 0.2;
				deltaTheta *= 0.9;
				deltaTheta += rng.nextDouble();
				deltaTheta -= rng.nextDouble();
				phi /= 2.0;
				phi += deltaPhi / 4.0;
				deltaPhi *= 0.75;
				deltaPhi += rng.nextDouble();
				deltaPhi -= rng.nextDouble();
				if (rng.nextDouble() >= 0.25) {
					Location centerPos = new Location(cavePos);
					centerPos.setX(centerPos.getX() + (rng.nextDouble() * 4.0 - 2.0) * 0.2);
					centerPos.setY(centerPos.getY() + (rng.nextDouble() * 4.0 - 2.0) * 0.2);
					centerPos.setZ(centerPos.getZ() + (rng.nextDouble() * 4.0 - 2.0) * 0.2);
					double radius = (Location.UPPER_Y_VALUE - centerPos.getY()) / Location.UPPER_Y_VALUE;
					radius = 1.2 + (radius * 3.5 + 1) * caveRadius;
					radius *= Math.sin(i * Math.PI / caveLength);
					try {
						fillOblateSpheroid(centerPos, radius, null);
					} catch (WrongMaterialException e) {
						// Should not occur
						e.printStackTrace();
					}
				}

			}
		}
		System.out.println();

		// Paso 4: crear vetas de minerales
		// Abundancia de cada mineral
		double abundance[] = new double[2];
		abundance[0] = 0.5; // GRANITE
		abundance[1] = 0.3; // OBSIDIAN
		int numVeins[] = new int[2];
		numVeins[0] = (int) (size * size * 256 * abundance[0]) / 16384; // GRANITE
		numVeins[1] = (int) (size * size * 256 * abundance[1]) / 16384; // OBSIDIAN

		Material vein = Material.GRANITE;
		for (int numVein = 0; numVein < 2; numVein++, vein = Material.OBSIDIAN) {
			System.out.print("Generando vetas de " + vein);
			for (int v = 0; v < numVeins[numVein]; v++) {
				System.out.print(vein.getSymbol());
				Location veinPos = new Location(this, rng.nextInt(size), rng.nextInt((int) Location.UPPER_Y_VALUE),
						rng.nextInt(size));
				double veinLength = rng.nextDouble() * rng.nextDouble() * 75 * abundance[numVein];
				// cave direction is given by two angles and corresponding rate of change in
				// those angles,
				// spherical coordinates perhaps?
				theta = rng.nextDouble() * Math.PI * 2;
				deltaTheta = 0.0;
				phi = rng.nextDouble() * Math.PI * 2;
				deltaPhi = 0.0;
				// double caveRadius = rng.nextDouble() * rng.nextDouble();
				for (int len = 0; len < (int) veinLength; len++) {
					veinPos.setX(veinPos.getX() + Math.sin(theta) * Math.cos(phi));
					veinPos.setY(veinPos.getY() + Math.cos(theta) * Math.cos(phi));
					veinPos.setZ(veinPos.getZ() + Math.sin(phi));
					theta += deltaTheta * 0.2;
					deltaTheta *= 0.9;
					deltaTheta += rng.nextDouble();
					deltaTheta -= rng.nextDouble();
					phi /= 2.0;
					phi += deltaPhi / 4.0;
					deltaPhi *= 0.9; // 0.9 for veins
					deltaPhi += rng.nextDouble();
					deltaPhi -= rng.nextDouble();
					double radius = abundance[numVein] * Math.sin(len * Math.PI / veinLength) + 1;

					try {
						fillOblateSpheroid(veinPos, radius, vein);
					} catch (WrongMaterialException ex) {
						// should not ocuur
						ex.printStackTrace();
					}
				}
			}
			System.out.println();
		}

		System.out.println();

		// flood-fill water
		char water = Material.WATER.getSymbol();

		int numWaterSources = size * size / 800;

		System.out.print("Creando fuentes de agua subterráneas");
		int x = 0;
		int z = 0;
		int y = 0;
		for (int w = 0; w < numWaterSources; w++) {
			System.out.print(water);
			x = rng.nextInt(size) + heightMap.negativeWorldLimit;
			z = rng.nextInt(size) + heightMap.negativeWorldLimit;
			y = (int) Location.SEA_LEVEL - 1 - rng.nextInt(2);
			try {
				floodFill(Material.WATER, new Location(this, x, y, z));
			} catch (WrongMaterialException | BadLocationException e) {
				// no debe suceder
				throw new RuntimeException(e);
			}
		}
		System.out.println();

		System.out.print("Creando erupciones de lava");
		char lava = Material.LAVA.getSymbol();
		// flood-fill lava
		int numLavaSources = size * size / 2000;
		for (int w = 0; w < numLavaSources; w++) {
			System.out.print(lava);
			x = rng.nextInt(size) + heightMap.negativeWorldLimit;
			z = rng.nextInt(size) + heightMap.negativeWorldLimit;
			y = (int) ((Location.SEA_LEVEL - 3) * rng.nextDouble() * rng.nextDouble());
			try {
				floodFill(Material.LAVA, new Location(this, x, y, z));
			} catch (WrongMaterialException | BadLocationException e) {
				// no debe suceder
				throw new RuntimeException(e);
			}
		}
		System.out.println();

		// Paso 5. crear superficie, criaturas e items
		// Las entidades aparecen sólo en superficie (no en cuevas, por ejemplo)

		OctaveGenerator onoise1 = new PerlinOctaveGenerator(this, 8);
		OctaveGenerator onoise2 = new PerlinOctaveGenerator(this, 8);
		boolean sandChance = false;
		double entitySpawnChance = 0.05;
		double itemsSpawnChance = 0.10;
		double foodChance = 0.8;
		double toolChance = 0.1;
		double weaponChance = 0.1;

		System.out.println("Generando superficie del terreno, entidades e items...");
		for (x = 0; x < size; x++) {
			for (z = 0; z < size; z++) {
				sandChance = onoise1.noise(x, z, 0.5, 2.0) > 8.0;
				y = (int) heightMap.heightMap[(int) x][(int) z];
				Location surface = new Location(this, x + heightMap.negativeWorldLimit, y,
						z + heightMap.negativeWorldLimit); // la posición (x,y+1,z) no está ocupada (es AIR)
				try {
					if (sandChance) {
						SolidBlock sand = new SolidBlock(Material.SAND);
						if (rng.nextDouble() < 0.5)
							sand.setDrops(Material.SAND, 1);
						blocks.put(surface, sand);
					} else {
						SolidBlock grass = new SolidBlock(Material.GRASS);
						if (rng.nextDouble() < 0.5)
							grass.setDrops(Material.GRASS, 1);
						blocks.put(surface, grass);
					}
				} catch (WrongMaterialException | StackSizeException ex) {
					// will never happen
					ex.printStackTrace();
				}
				// intenta crear una entidad en superficie
				try {
					Location aboveSurface = surface.above();

					if (rng.nextDouble() < entitySpawnChance) {
						Creature entity = null;
						double entityHealth = rng.nextInt((int) LivingEntity.MAX_HEALTH) + 1;
						if (rng.nextDouble() < 0.75) // generamos Monster (75%) o Animal (25%) de las veces
							entity = new Monster(aboveSurface, entityHealth);
						else
							entity = new Animal(aboveSurface, entityHealth);
						creatures.put(aboveSurface, entity);
					} else {
						// si no, intentamos crear unos items de varios tipos (comida, armas,
						// herramientas)
						// dentro de cofres
						Material itemMaterial = null;
						int amount = 1; // p. def. para herramientas y armas
						if (rng.nextDouble() < itemsSpawnChance) {
							double rand = rng.nextDouble();
							if (rand < foodChance) { // crear comida
								// hay cuatro tipos de item de comida, en las posiciones 8 a 11 del array
								// 'materiales'
								itemMaterial = Material.getRandomItem(8, 11);
								amount = rng.nextInt(5) + 1;
							} else if (rand < foodChance + toolChance)
								// hay dos tipos de item herramienta, en las posiciones 12 a 13 del array
								// 'materiales'
								itemMaterial = Material.getRandomItem(12, 13);
							else
								// hay dos tipos de item arma, en las posiciones 14 a 15 del array 'materiales'
								itemMaterial = Material.getRandomItem(14, 15);

							items.put(aboveSurface, new ItemStack(itemMaterial, amount));
						}
					}
				} catch (BadLocationException | StackSizeException e) {
					// BadLocationException : no hay posiciones más arriba, ignoramos creación de
					// entidad/item sin hacer nada
					// StackSizeException : no se producirá
					throw new RuntimeException(e);
				}

			}
		}

		// TODO: Crear plantas

		// Generar jugador
		player = new Player("Steve", this);
		// El jugador se crea en la superficie (posición (0,*,0)). Asegurémonos de que
		// no hay nada más ahí
		Location playerLocation = player.getLocation();
		creatures.remove(playerLocation);
		items.remove(playerLocation);

	}

	/**
	 * Where fillOblateSpheroid() is a method which takes a central point, a radius
	 * and a material to fill to use on the block array.
	 * 
	 * @param centerPos central point
	 * @param radius    radius around central point
	 * @param material  material to fill with
	 * @throws WrongMaterialException if 'material' is not a block material
	 */
	private void fillOblateSpheroid(Location centerPos, double radius, Material material)
			throws WrongMaterialException {

		for (double x = centerPos.getX() - radius; x < centerPos.getX() + radius; x += 1.0) {
			for (double y = centerPos.getY() - radius; y < centerPos.getY() + radius; y += 1.0) {
				for (double z = centerPos.getZ() - radius; z < centerPos.getZ() + radius; z += 1.0) {
					double dx = x - centerPos.getX();
					double dy = y - centerPos.getY();
					double dz = z - centerPos.getZ();

					if ((dx * dx + 2 * dy * dy + dz * dz) < radius * radius) {
						// point (x,y,z) falls within level bounds ?
						// we don't need to check it, just remove or replace that location from the
						// blocks map.
						Location loc = new Location(this, Math.floor(x + heightMap.negativeWorldLimit), Math.floor(y),
								Math.floor(z + heightMap.negativeWorldLimit));
						if (material == null)
							blocks.remove(loc);
						else
							try { // if ((Math.abs(x) < worldSize/2.0-1.0) && (Math.abs(z) < worldSize/2.0-1.0) &&
									// y>0.0 && y<=Location.UPPER_Y_VALUE)
								SolidBlock veinBlock = new SolidBlock(material);
								// los bloques de veta siempre contienen material
								veinBlock.setDrops(material, 1);
								blocks.replace(loc, veinBlock);
							} catch (StackSizeException ex) {
								// will never happen
								ex.printStackTrace();
							}
					}
				}
			}
		}
	}

	/**
	 * Funcion floodFill ya implementada
	 * 
	 * @param liquid Material liquido
	 * @param from   posicion
	 * @throws WrongMaterialException si el material es erróneo
	 * @throws BadLocationException   si la posicion es erronea
	 */
	private void floodFill(Material liquid, Location from) throws WrongMaterialException, BadLocationException {
		if (!liquid.isLiquid())
			throw new WrongMaterialException(liquid);
		if (!blocks.containsKey(from)) {
			blocks.put(from, BlockFactory.createBlock(liquid));
			items.remove(from);
			Set<Location> floodArea = getFloodNeighborhood(from);
			for (Location loc : floodArea)
				floodFill(liquid, loc);
		}
	}

	/**
	 * Obtiene las posiciones adyacentes a esta que no están por encima y están
	 * libres
	 * 
	 * @param location Posicion para obtener sus adyacentes
	 * @return si esta posición pertenece a un mundo, devuelve sólo aquellas
	 *         posiciones adyacentes válidas para ese mundo, si no, devuelve todas
	 *         las posiciones adyacentes
	 * @throws BadLocationException cuando la posición es de otro mundo
	 */
	private Set<Location> getFloodNeighborhood(Location location) throws BadLocationException {
		if (location.getWorld() != null && location.getWorld() != this)
			throw new BadLocationException("Esta posición no es de este mundo");
		Set<Location> neighborhood = location.getNeighborhood();
		Iterator<Location> iter = neighborhood.iterator();
		while (iter.hasNext()) {
			Location loc = iter.next();
			try {
				if ((loc.getY() > location.getY()) || getBlockAt(loc) != null)
					iter.remove();
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
				// no sucederá
			}
		}
		return neighborhood;
	}
}
