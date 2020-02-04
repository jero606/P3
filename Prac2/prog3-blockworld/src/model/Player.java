/**
 * 
 */
package model;

import java.util.Set;

import model.World.HeightMap;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;

/**
 * Clase del Player donde están sus métodos y sus atributos
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 */
public class Player {
	/**
	 * Nombre del jugador
	 */
	private String name;

	/**
	 * Vida del jugador
	 */
	private double health;

	/**
	 * Nivel de comida del jugador
	 */
	private double foodLevel;

	/**
	 * Maximo nivel de comida que puede tener el jugador
	 */
	public final static double MAX_FOODLEVEL = 20;

	/**
	 * Maxima vida que puede tener el jugador
	 */
	public final static double MAX_HEALTH = 20;

	/**
	 * Variable de la composicion
	 */
	private Inventory inventory;

	/**
	 * Variable de la composicion
	 */
	private Location location;

	/**
	 * Constructor del jugador
	 * 
	 * @param name  Nombre del jugador
	 * @param world Nombre del mundo al que pertenece el jugador
	 * @throws StackSizeException   Si error al crear el ItemStack
	 * @throws BadLocationException Si error al crear Location
	 */
	public Player(String name, World world) {

		this.name = name;
		this.health = Player.MAX_HEALTH;
		this.foodLevel = Player.MAX_FOODLEVEL;

		Location l1 = new Location(world, 0.0, 0.0, 0.0);
		Location l2;
		try {
			l2 = new Location(world.getHighestLocationAt(l1));
			location = new Location(l2.getWorld(), l2.getX(), l2.getY() + 1, l2.getZ());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		ItemStack espada;
		try {
			espada = new ItemStack(Material.WOOD_SWORD, 1);
			inventory = new Inventory();
			inventory.setItemInHand(espada);
		} catch (StackSizeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Getter del inventario
	 * 
	 * @return inventory Inventario
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Getter del nombre
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter de la salud
	 * 
	 * @return the health
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * Getter del nivel de comida
	 * 
	 * @return the foodLevel
	 */
	public double getFoodLevel() {
		return foodLevel;
	}

	/**
	 * Getter de la location
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return new Location(location);
	}

	/**
	 * Establece el nivel de salud
	 * 
	 * @param health Salud a establecer
	 */
	public void setHealth(double health) {
		if (health >= Player.MAX_HEALTH) {
			this.health = Player.MAX_HEALTH;
		} else {
			this.health = health;
		}
	}

	/**
	 * Establece el nivel de alimento actual del jugador
	 * 
	 * @param food Nivel de comida a establecer
	 */
	public void setFoodLevel(double food) {
		if (food >= Player.MAX_FOODLEVEL) {
			this.foodLevel = Player.MAX_FOODLEVEL;
		} else {
			this.foodLevel = food;
		}
	}

	/**
	 * Comprueba si el jugadro está muerto
	 * 
	 * @return Boolean True si esta muerto, false si no
	 */
	public boolean isDead() {
		if (health <= 0)
			return true;
		else
			return false;
	}

	/**
	 * Mueve al jugador
	 * 
	 * @param dx Numero que vamos a sumar a la posicion actual
	 * @param dy Numero que vamos a sumar a la posicion actual
	 * @param dz Numero que vamos a sumar a la posicion actual
	 * @throws EntityIsDeadException Si jugador está muerto
	 * @throws BadLocationException  Si la location no correcta
	 * @return Location Posicion a la que nos movemos
	 */
	public Location move(int dx, int dy, int dz) throws EntityIsDeadException, BadLocationException {
		Location destino = new Location(getLocation().getWorld(), getLocation().getX() + dx, getLocation().getY() + dy,
				getLocation().getZ() + dz);
		Set<Location> adyacentes = getLocation().getNeighborhood();

		boolean posicionDestinoEnAdyacentes = false;

		for (Location a : adyacentes) {
			if (a.equals(destino)) {
				posicionDestinoEnAdyacentes = true;
			}
		}

		if (isDead()) {
			throw new EntityIsDeadException();
		} else if (!posicionDestinoEnAdyacentes || !Location.check(destino) || !destino.isFree()) {
			throw new BadLocationException("Posicion erronea");
		} else {
			location = new Location(destino);
			decreaseFoodLevel(0.05);
			return location;
		}
	}

	/**
	 * Decrementa el nivel de alimento/salud en ‘d’
	 * 
	 * @param d Valor a decrementar
	 */
	private void decreaseFoodLevel(double d) {
		if ((foodLevel - d) == 0) {
			setFoodLevel(0);
		} else if ((foodLevel - d) > 0) {
			setFoodLevel(foodLevel - d);
		} else {
			if ((health + (foodLevel - d)) < 0) {
				setHealth(health + (foodLevel - d));
				setFoodLevel(0);

			} else {
				setHealth(health + (foodLevel - d));
				setFoodLevel(0);
			}

		}
	}

	/**
	 * Incrementa el nivel de alimento/salud en ‘d’
	 * 
	 * @param d Valor a incrementar
	 */
	private void increaseFoodLevel(double d) {
		if (foodLevel == Player.MAX_FOODLEVEL) {
			if ((d + health) >= Player.MAX_HEALTH) {
				setHealth(Player.MAX_HEALTH);
			} else {
				setHealth(d + health);
			}
		} else {
			if ((d + foodLevel) >= Player.MAX_FOODLEVEL) {
				if (((d + foodLevel) - Player.MAX_FOODLEVEL) >= Player.MAX_HEALTH) {
					setFoodLevel(Player.MAX_FOODLEVEL);
					setHealth(Player.MAX_HEALTH);
				} else {
					setHealth(health + (d + foodLevel) - Player.MAX_FOODLEVEL);
					setFoodLevel(Player.MAX_FOODLEVEL);
				}
			} else {
				setFoodLevel(d + foodLevel);
			}
		}
	}

	/**
	 * Usa el objeto qeu tenemos en la mano
	 * 
	 * @param times Num de veces a usar el objeto
	 * @throws EntityIsDeadException Si el jgador esta muerto
	 */
	public void useItemInHand(int times) throws EntityIsDeadException {
		if (isDead()) {
			throw new EntityIsDeadException();
		} else if (times <= 0) {
			throw new IllegalArgumentException();
		} else if (inventory.getItemInHand() != null) {
			if (inventory.getItemInHand().getType().isEdible()) {
				if (times > inventory.getItemInHand().getAmount()) {
					increaseFoodLevel(
							inventory.getItemInHand().getAmount() * inventory.getItemInHand().getType().getValue());
					inventory.setItemInHand(null);
				} else {
					increaseFoodLevel(times * inventory.getItemInHand().getType().getValue());
					if ((inventory.getItemInHand().getAmount() - times) == 0) {
						inventory.setItemInHand(null);
					} else {
						try {
							ItemStack i1 = new ItemStack(inventory.getItemInHand().getType(),
									inventory.getItemInHand().getAmount() - times);
							inventory.setItemInHand(i1);
						} catch (StackSizeException e) {
							e.printStackTrace();
						}
					}

				}
			} else {
				decreaseFoodLevel(0.1 * times);
			}
		}
	}

	/**
	 * Intercambia item en la mano por el de la posicion pos
	 * 
	 * @param pos Posicion dle item que queremos en la mano
	 * @throws BadInventoryPositionException Si la posicion no es correcta
	 */
	public void selectItem(int pos) throws BadInventoryPositionException {
		if (pos >= inventory.getSize() || pos < 0 || pos > ItemStack.MAX_STACK_SIZE) {
			throw new BadInventoryPositionException(pos);
		} else if (inventory.getItemInHand() == null) {
			inventory.setItemInHand(inventory.getItem(pos));
			inventory.clear(pos);
		} else {
			ItemStack aux = inventory.getItemInHand();
			inventory.setItemInHand(inventory.getItem(pos));
			inventory.setItem(pos, aux);
		}
	}

	/**
	 * Obtiene tamaño del inventario
	 * 
	 * @return int Tamaño del inventario
	 */
	public int getInventorySize() {
		return inventory.getSize();
	}

	/**
	 * Añade los items en un espacio del inventario del jugador
	 * 
	 * @param items objeto que se añade en el inventario
	 */
	public void addItemsToInventory(ItemStack items) {
		inventory.addItem(items);
	}

	/**
	 * Devuelve una cadena con la informacion del jugador
	 * 
	 * @return resultado Cadena con informacion del jugador
	 */
	public String toString() {
		String resultado = "Name=" + name + "\n";
		resultado = resultado + location.toString() + "\n";
		resultado = resultado + "Health=" + health + "\n";
		resultado = resultado + "Food level=" + foodLevel + "\n";
		resultado = resultado + "Inventory=" + inventory.toString();

		return resultado;
	}

	/**
	 * Funcion hashCode
	 * 
	 * @return result Resultado hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(foodLevel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(health);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Player other = (Player) obj;
		if (Double.doubleToLongBits(foodLevel) != Double.doubleToLongBits(other.foodLevel))
			return false;
		if (Double.doubleToLongBits(health) != Double.doubleToLongBits(other.health))
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
