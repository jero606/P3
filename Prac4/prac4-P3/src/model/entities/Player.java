/**
 * 
 */
package model.entities;

import java.util.Set;

import model.Inventory;
import model.ItemStack;
import model.Location;
import model.Material;
import model.World;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;

/**
 * Clase del player con sus métodos y atributos
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 *
 */
public class Player extends LivingEntity {

	/**
	 * Nombre del jugador
	 */
	private String name;

	/**
	 * Nivel de comida del jugador
	 */

	private double foodLevel;
	/**
	 * Maximo nivel de comida que puede tener el jugador
	 */
	public final static double MAX_FOODLEVEL = 20;

	/**
	 * Variable del simbolo del Player
	 */
	private static char symbol = 'P';

	/**
	 * Variable de la composicion con orientacion
	 */
	private Location orientation;

	/**
	 * Variable de la composicion con inventory
	 */
	private Inventory inventory;

	/**
	 * Constructor del jugador
	 * 
	 * @param name  Nombre del jugador
	 * @param world Nombre del mundo al que pertenece el jugador
	 */
	public Player(String name, World world) {
		super(new Location(world, 0, 0, 0), LivingEntity.MAX_HEALTH);
		this.name = name;
		this.foodLevel = Player.MAX_FOODLEVEL;

		Location l1 = new Location(world, 0.0, 0.0, 0.0);
		Location l2;
		try {
			l2 = new Location(world.getHighestLocationAt(l1));
			location = new Location(l2.getWorld(), l2.getX(), l2.getY() + 1, l2.getZ());
			orientation = new Location(location.getWorld(), 0, 0, 1);
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
	 * Getter de la orientacion
	 * 
	 * @return Location Orientacion del player
	 */
	public Location getOrientation() {
		return new Location(orientation.getWorld(), orientation.getX() + location.getX(),
				orientation.getY() + location.getY(), +orientation.getZ() + location.getZ());
	}

	/**
	 * Getter del simbolo del player
	 * 
	 * @return symbol Simbolo del player
	 */
	public char getSymbol() {
		return symbol;
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
	 * Getter del inventario
	 * 
	 * @return inventory Inventario
	 */
	public Inventory getInventory() {
		return new Inventory(inventory);
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
	 * Usa el objeto qeu tenemos en la mano
	 * 
	 * @param times Num de veces a usar el objeto
	 * @return objeto en la mano
	 * @throws EntityIsDeadException Si el jgador esta muerto
	 */
	public ItemStack useItemInHand(int times) throws EntityIsDeadException {
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
							// ItemStack i1 = new ItemStack(inventory.getItemInHand().getType(),
							// inventory.getItemInHand().getAmount() - times);
							// inventory.setItemInHand(i1);
							inventory.getItemInHand().setAmount(inventory.getItemInHand().getAmount() - times);
						} catch (StackSizeException e) {
							e.printStackTrace();
						}
					}

				}
			} else {
				decreaseFoodLevel(0.1 * times);
			}
		}

		return inventory.getItemInHand();
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
			if ((getHealth() + (foodLevel - d)) < 0) {
				setHealth(getHealth() + (foodLevel - d));
				setFoodLevel(0);

			} else {
				setHealth(getHealth() + (foodLevel - d));
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
			if ((d + getHealth()) >= Player.MAX_HEALTH) {
				setHealth(Player.MAX_HEALTH);
			} else {
				setHealth(d + getHealth());
			}
		} else {
			if ((d + foodLevel) >= Player.MAX_FOODLEVEL) {
				if (((d + foodLevel) - Player.MAX_FOODLEVEL) >= Player.MAX_HEALTH) {
					setFoodLevel(Player.MAX_FOODLEVEL);
					setHealth(Player.MAX_HEALTH);
				} else {
					setHealth(getHealth() + (d + foodLevel) - Player.MAX_FOODLEVEL);
					setFoodLevel(Player.MAX_FOODLEVEL);
				}
			} else {
				setFoodLevel(d + foodLevel);
			}
		}
	}

	/**
	 * Orienta al jugador a una posicion
	 * 
	 * @param dx Valor x
	 * @param dy Valor y
	 * @param dz Valor z
	 * @return Location Posicion a la que será orientado
	 * @throws EntityIsDeadException Si jugador está muerto
	 * @throws BadLocationException  Si la posicion es incorrecta
	 */
	public Location orientate(int dx, int dy, int dz) throws EntityIsDeadException, BadLocationException {
		Set<Location> adyacentes = getLocation().getAllNeighborhood();

		if (isDead()) {
			throw new EntityIsDeadException();
		} else if (dx == 0 && dy == 0 && dz == 0) {
			throw new BadLocationException("Posición errónea");
		} else if (!adyacentes.contains(
				new Location(location.getWorld(), location.getX() + dx, location.getY() + dy, location.getZ() + dz))) {
			throw new BadLocationException("Posición errónea");
		} else {
			orientation.setX(dx);
			orientation.setY(dy);
			orientation.setZ(dz);

			return new Location(location.getWorld(), location.getX() + dx, location.getY() + dy, location.getZ() + dz);
		}
	}

	/**
	 * Mueve al jugador
	 * 
	 * @param dx Numero que vamos a sumar a la posicion actual
	 * @param dy Numero que vamos a sumar a la posicion actual
	 * @param dz Numero que vamos a sumar a la posicion actual
	 * @return nueva posicion del player
	 * @throws EntityIsDeadException Si jugador está muerto
	 * @throws BadLocationException  Si la location no correcta
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
		} else if (!posicionDestinoEnAdyacentes || !Location.check(destino)
				|| (!destino.isFree() && !getLocation().getWorld().getBlockAt(destino).getType().isLiquid())) {
			throw new BadLocationException("Posicion erronea");
		} else {
			location = new Location(destino);
			decreaseFoodLevel(0.05);
			return location;
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
	 * Funcion hashCode()
	 * 
	 * @return result Resultado del hashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(foodLevel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto a comparar
	 * @return boolean True si son iguales, false si no
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (Double.doubleToLongBits(foodLevel) != Double.doubleToLongBits(other.foodLevel))
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orientation == null) {
			if (other.orientation != null)
				return false;
		} else if (!orientation.equals(other.orientation))
			return false;
		return true;
	}

	/**
	 * Devuelve una cadena con la informacion del jugador
	 * 
	 * @return resultado Cadena con informacion del jugador
	 */
	public String toString() {
		String resultado = "Name=" + name + "\n";
		resultado = resultado + location.toString() + "\n";
		resultado = resultado + "Orientation=" + orientation.toString() + "\n";
		resultado = resultado + "Health=" + getHealth() + "\n";
		resultado = resultado + "Food level=" + foodLevel + "\n";
		resultado = resultado + "Inventory=" + inventory.toString();

		return resultado;
	}

}
