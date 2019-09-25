package model;

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
	 * Constructor que recibe los parámetros
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
	 * @return La distancia entre 2 localizaciones
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
	 * Getter
	 * 
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Setter
	 * 
	 * @param world the world to set
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * Getter
	 * 
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter
	 * 
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Getter
	 * 
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter
	 * 
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = (y <= UPPER_Y_VALUE) ? (y >= 0.0 ? y : 0.0) : UPPER_Y_VALUE;
	}

	/**
	 * Getter
	 * 
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter
	 * 
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Funcion lenght que calcula la longitud de la localizacion
	 * 
	 * @return La longitud de la localizacion
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Funcion multiply que multiplica las coordenadas de la localizacion por el
	 * factor
	 * 
	 * @param factor Valor de factor
	 * @return La localizacion con sus coordenadas multiplicadas por el factor
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
	 * @return La resta de 2 localizaciones
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
	 * @return La localizacion con sus atributos a 0
	 */
	public Location zero() {
		x = y = z = 0.0;
		return this;
	}

	/**
	 * Función equals(Object obj)
	 * 
	 * @return true si son iguales y false si no lo son
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
	 * @return the result of hashCode
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
	 * @return El String de Location{world=...}
	 */
	public String toString() {
		String resultado = "Location{world=";

		if (world == null)
			resultado += "null";
		else
			resultado = resultado += world.getName();

		resultado += ",x=" + getX() + ",y=" + getY() + ",z=" + getZ() + "}";
		return resultado;
	}

}
