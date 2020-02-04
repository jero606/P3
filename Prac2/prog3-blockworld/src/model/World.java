/**
 * 
 */
package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.bukkit.util.noise.CombinedNoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import model.exceptions.BadLocationException;
import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

/**
 * class World
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
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
		 * Constructor del heightMap
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
		 * @return altura del terreno
		 */
		double get(double x, double z) {
			return heightMap[(int) x - negativeWorldLimit][(int) z - negativeWorldLimit];
		}

		/**
		 * Setter
		 * 
		 * @param x Valor X
		 * @param z Valor Y
		 * @param y Valor Z
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
	 * Constrcutor World
	 * 
	 * @param seed Semilla
	 * @param size Tamaño
	 * @param name Nombre
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
		// heightMap = new HeightMap(size);
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
	 * Genera un mundo nuevo del tamaño size*size en el plano (x,z). Si existían
	 * elementos anteriores en el mundo, serán eliminados. Usando la misma semilla y
	 * el mismo tamaño podemos generar mundos iguales
	 * 
	 * @param seed semilla para el algoritmo de generación.
	 * @param size tamaño del mundo para las dimensiones x y z
	 * @throws StackSizeException
	 * @throws BadLocationException
	 */
	private void generate(long seed, int size) {

		Random rng = new Random(getSeed());

		blocks.clear();
		items.clear();

		// Paso 1: generar nuevo mapa de alturas del terreno
		heightMap = new HeightMap(size);
		CombinedNoiseGenerator noise1 = new CombinedNoiseGenerator(this);
		CombinedNoiseGenerator noise2 = new CombinedNoiseGenerator(this);
		OctaveGenerator noise3 = new PerlinOctaveGenerator(this, 6);

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
		Block block = null;
		Location location = null;
		Material material = null;
		OctaveGenerator noise = new PerlinOctaveGenerator(this, 8);
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
						block = new Block(material);
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
		// TODO: Crear varias cuevas (numCuevas)
		Location cavePos = new Location(this, rng.nextInt(size), rng.nextInt((int) Location.UPPER_Y_VALUE),
				rng.nextInt(size));
		double caveLength = rng.nextDouble() * rng.nextDouble() * 200;
		// cave direction is given by two angles and corresponding rate of change in
		// those angles,
		// spherical coordinates perhaps?
		double theta = rng.nextDouble() * Math.PI * 2;
		double deltaTheta = 0.0;
		double phi = rng.nextDouble() * Math.PI * 2;
		double deltaPhi = 0.0;
		double caveRadius = rng.nextDouble() * rng.nextDouble();

		for (int i = 0; i < (int) caveLength; i++) {
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

		// Paso 4: crear vetas de minerales
		// Abundancia de cada mineral
		double abundance[] = new double[2];
		abundance[0] = 0.9; // GRANITE
		abundance[1] = 0.5; // OBSIDIAN
		int numVeins[] = new int[2];
		numVeins[0] = size * size * 256 * (int) abundance[0] / 16384; // GRANITE
		numVeins[1] = size * size * 256 * (int) abundance[1] / 16384; // OBSIDIAN

		Material vein = Material.GRANITE;
		for (int numVein = 0; numVein < 2; numVein++, vein = Material.OBSIDIAN) {
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

		// We obviate flood-fill water and lava

		// Paso 5. crear superficie y entidades
		// Las entidades aparecen sólo en superficie (no en cuevas, por ejemplo)

		OctaveGenerator onoise1 = new PerlinOctaveGenerator(this, 8);
		OctaveGenerator onoise2 = new PerlinOctaveGenerator(this, 8);
		boolean sandChance = false;
		double y = 0.0;
		double entitySpawnChance = 0.05;
		double itemsSpawnChance = 0.10;
		double foodChance = 0.8;
		double toolChance = 0.1;
		double weaponChance = 0.1;

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				sandChance = onoise1.noise(x, z, 0.5, 2.0) > 8.0;
				y = heightMap.heightMap[x][z];
				Location surface = new Location(this, x + heightMap.negativeWorldLimit, y,
						z + heightMap.negativeWorldLimit); // la posición (x,y+1,z) no está ocupada (es AIR)
				try {
					if (sandChance) {
						Block sand = new Block(Material.SAND);
						if (rng.nextDouble() < 0.5)
							sand.setDrops(Material.SAND, 1);
						blocks.put(surface, sand);
					} else {
						Block grass = new Block(Material.GRASS);
						if (rng.nextDouble() < 0.5)
							grass.setDrops(Material.GRASS, 1);
						blocks.put(surface, grass);
					}
				} catch (WrongMaterialException | StackSizeException ex) {
					// will never happen
					ex.printStackTrace();
				}
				try {
					Location aboveSurface = surface.above();
					// intentamos crear unos items de varios tipos (comida, armas, herramientas)
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
				} catch (BadLocationException | StackSizeException e) {
					// BadLocationException : no hay posiciones más arriba, ignoramos creación de
					// entidad/item sin hacer nada
					// StackSizeException : no se producirá
					// WrongMaterialException : al crear el cofre, no se producirá
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
		items.remove(playerLocation);

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
	 * Obtiene el bloque que está en la posición indicada, o null si no hay ningún
	 * bloque ahí.
	 * 
	 * @param loc Posicion de la que quermeos el bloque
	 * @return bloque Bloque que hay en la posicion
	 * @throws BadLocationException Si la posicion no pertenece al mundo
	 */
	public Block getBlockAt(Location loc) throws BadLocationException {
		if ((!this.equals(loc.getWorld())) || (loc.getWorld() == null)) {
			throw new BadLocationException("Bad Location");
		}
		Block bloque = blocks.get(loc);
		return bloque;
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
		if ((!this.equals(loc.getWorld())) || (loc.getWorld() == null)) {
			throw new BadLocationException(name);
		}
		ItemStack item = items.get(loc);
		return item;
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
		ItemStack item = items.remove(loc);
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
	 * Comprueba si la posicion dada está libre
	 * 
	 * @param loc Posicion para comprobar si esta libre
	 * @return boolean True si esta libre, false si no
	 * @throws BadLocationException si la posicion no pertenece al mundo
	 */
	public boolean isFree(Location loc) throws BadLocationException {
		if (!this.equals(loc.getWorld()) || loc.getWorld() == null) {
			throw new BadLocationException("Posicion no pertenece al mundo");
		} else {
			if (getBlockAt(loc) == null && !getPlayer().getLocation().equals(loc)) {
				return true;
			} else {
				return false;
			}
		}
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
						Location loc = new Location(this, x + heightMap.negativeWorldLimit, y,
								z + heightMap.negativeWorldLimit);
						if (material == null)
							blocks.remove(loc);
						else
							try { // if ((Math.abs(x) < worldSize/2.0-1.0) && (Math.abs(z) < worldSize/2.0-1.0) &&
									// y>0.0 && y<=Location.UPPER_Y_VALUE)
								Block veinBlock = new Block(material);
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

		if (!this.equals(loc.getWorld())) {
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
	 * @throws BadLocationException Si la posicion es erronea
	 */
	public char comprobarvalor(Location lc) {

		char chr = 0;

		try {
			if (lc.equals(getPlayer().getLocation())) {
				chr = 'P';
			} else if (getBlockAt(lc) == null) {
				if (getItemsAt(lc) == null) {
					chr = '.';
				} else {
					chr = getItemsAt(lc).getType().getSymbol();
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

}
