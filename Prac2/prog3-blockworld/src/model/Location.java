package model;

import java.util.HashSet;
import java.util.Set;

import model.exceptions.BadLocationException;

/**
 * Clase Location, donde están sus métodos y atributos implementados
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class Location {
	// ----------------------ATRIBUTOS----------------------
	/**
	 * Variable del maximo valor de Y
	 */
	public static double UPPER_Y_VALUE = 255;

	/**
	 * Variable del nivel del mar
	 */
	public static double SEA_LEVEL = 63;

	/**
	 * Variable que representa la asociación direccional con multiplicidad 1
	 */
	private World world;

	/**
	 * Variable x
	 */
	private double x;

	/**
	 * Variable y
	 */
	private double y;

	/**
	 * Variable z
	 */
	private double z;

	// -------------MÉTODOS----------------------
	/**
	 * Constructor que recibe los parámetros del mundo
	 * 
	 * @param world Mundo
	 * @param x     Valor x
	 * @param y     Valor y
	 * @param z     Valor z
	 */
	public Location(World world, double x, double y, double z) {
		super();
		this.world = world;
		this.x = x;
		setY(y);
		this.z = z;
	}

	/**
	 * Constructor de copia
	 * 
	 * @param l Valor de una Location
	 */
	public Location(Location l) {
		this.world = l.world;
		this.x = l.x;
		setY(l.y);
		this.z = l.z;
	}

	/**
	 * Función add que añade Localizaciones del mismo mundo
	 * 
	 * @param l Valor de una Location
	 * @return La suma de dos localizaciones
	 */
	public Location add(Location l) {
		if (l.world != world)
			System.err.println("Cannot add Locations of differing worlds.");
		else {
			x += l.x;
			setY(y + l.y);
			z += l.z;
		}
		return this;
	}

	/**
	 * Funcion distance que calcula la distancia entre dos localizaciones del mismo
	 * mundo
	 * 
	 * @param loc Valor de una Location
	 * @return double La distancia entre 2 localizaciones
	 */
	public double distance(Location loc) {
		if (loc.getWorld() == null || getWorld() == null) {
			System.err.println("Cannot measure distance to a null world");
			return -1.0;
		} else if (loc.getWorld() != getWorld()) {
			System.err.println("Cannot measure distance between " + world.getName() + " and " + loc.world.getName());
			return -1.0;
		}

		double dx = x - loc.x;
		double dy = y - loc.y;
		double dz = z - loc.z;

		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Getter del mundo
	 * 
	 * @return world El mundo
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Setter del mundo
	 * 
	 * @param world the world to set
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * Getter de la coord X
	 * 
	 * @return x La coord
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter de coord X
	 * 
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Getter de coord Y
	 * 
	 * @return y La coord
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter de coord Y
	 * 
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Getter de Z
	 * 
	 * @return z La coord
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter de coord Z
	 * 
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Funcion lenght que calcula la longitud de la localizacion
	 * 
	 * @return double La longitud de la localizacion
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Funcion multiply que multiplica las coordenadas de la localizacion por el
	 * factor
	 * 
	 * @param factor Valor de factor
	 * @return location La localizacion con sus coordenadas multiplicadas por el
	 *         factor
	 */
	public Location multiply(double factor) {
		x *= factor;
		setY(y * factor);
		z *= factor;
		return this;
	}

	/**
	 * Funcion substrac que resta dos localizaciones del mismo mundo
	 * 
	 * @param loc Valor de Location
	 * @return location La resta de 2 localizaciones
	 */
	public Location substract(Location loc) {
		if (loc.world != world)
			System.err.println("Cannot substract Locations of differing worlds.");
		else {
			x -= loc.x;
			setY(y - loc.y);
			z -= loc.z;
		}
		return this;
	}

	/**
	 * Función zero que pone los atributos x,y,z a 0
	 * 
	 * @return location La localizacion con sus atributos a 0
	 */
	public Location zero() {
		x = y = z = 0.0;
		return this;
	}

	/**
	 * Función equals(Object obj)
	 * 
	 * @param obj Objeto
	 * @return boolean true si son iguales y false si no lo son
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Location other = (Location) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;

		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;

		return true;
	}

	/**
	 * Funcion hashCode()
	 * 
	 * @return result of hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Funcion toString()
	 * 
	 * @return String El String de Location{world=...}
	 */
	public String toString() {
		String resultado = "Location{world=";

		if (world == null)
			resultado += "NULL";
		else
			resultado = resultado += world.getName();

		resultado += ",x=" + getX() + ",y=" + getY() + ",z=" + getZ() + "}";
		return resultado;
	}

	/**
	 * Funcion que comprueba si la posicion está libre
	 * 
	 * @return boolean True si está libre, false si no
	 * @throws BadLocationException Si la posicion es incorrecta
	 */
	public boolean isFree() {
		if (getWorld() == null) {
			return false;
		} else {
			try {
				if (world.getBlockAt(this) == null && !world.getPlayer().getLocation().equals(this)) {
					return true;
				} else {
					return false;
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		return true;

	}

	/**
	 * Función que devuelve la posicion justo debajo de ésta
	 * 
	 * @return l Location que está debajo
	 * @throws BadLocationException Posicion erronea
	 */
	public Location below() throws BadLocationException {
		if (world != null && y == 0) {
			throw new BadLocationException("Error_BadLocationException");
		}
		Location l = new Location(world, x, y - 1, z);

		return l;
	}

	/**
	 * Funcion que devuelve la posicion que está arriba
	 * 
	 * @return l Location que está encima
	 * @throws BadLocationException Posicion erronea
	 */
	public Location above() throws BadLocationException {
		if (world != null && y == Location.UPPER_Y_VALUE) {
			// System.err.println("Error");
			throw new BadLocationException("Error_BadLocationException");
		}
		Location l = new Location(world, x, y + 1, z);

		return l;
	}

	/**
	 * Funcion que comprueba si una posicion está dentro de los limites del mundo
	 * 
	 * @param w Mundo
	 * @param x Valor de X
	 * @param y Valor de Y
	 * @param z Valor de Z
	 * @return boolean True si está dentro, false si no
	 */
	public static boolean check(World w, double x, double y, double z) {

		int limitePositivo = 0;
		int limiteNegativo = 0;

		if (w == null) {
			return true;
		}

		if (w.getSize() % 2 != 0) {
			limitePositivo = (w.getSize() - 1) / 2;
			limiteNegativo = 0 - limitePositivo;
		} else {
			limitePositivo = w.getSize() / 2;
			limiteNegativo = 0 - (limitePositivo - 1);
		}

		if (x <= limitePositivo && x >= limiteNegativo && z <= limitePositivo && z >= limiteNegativo
				&& y <= Location.UPPER_Y_VALUE && y >= 0)
			return true;
		else
			return false;
	}

	/**
	 * Funcion que comprueba si una posicion está dentro de los limites del mundo
	 * 
	 * @param l Posicion a comprobar
	 * @return boolean True si está dentro, false si no
	 */
	public static boolean check(Location l) {
		return check(l.getWorld(), l.getX(), l.getY(), l.getZ());
	}

	/**
	 * Funcion que devuelve las posiciones adyacentes que están dentro del mundo de
	 * la posicion actual
	 * 
	 * @return Set<Location> Set que contiene las posiciones adyacentes que están
	 *         dentro de los límites del mundo
	 */
	public Set<Location> getNeighborhood() {

		Set<Location> result = new HashSet<>();

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					Location vecino = new Location(world, x + i, y + j, z + k);
					if (!vecino.equals(this)) {
						if (check(vecino)) {
							result.add(vecino);
						}
					}
				}
			}
		}

		return result;
	}
}
