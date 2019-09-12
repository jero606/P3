/**
 * 
 */
package Prac1;

/**
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */

/**
 * Clase Location
 */
public class Location {
	
	//----------------------ATRIBUTOS----------------------
	/**
	 * Variable del maximo valor de Y
	 */
	public final static double UPPER_Y_VALUE = 255;
	
	/**
	 * Variable del nivel del mar
	 */
	public final static double SEA_LEVEL = 63;
	
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

	//----------------------MÉTODOS-------------------
	
	/**
	 * @param World, double, double, double
	 */
	public Location(World w, double x, double y, double z) {
		
		super();
		setWorld(w);
		setX(x);
		setY(y);
		setZ(z);
	}
	
	/**
	 * Constructor de copia
	 * @param l
	 */
	public Location(Location l) {
		
		setWorld(l.world);
		setX(l.getX());
		setY(l.getY());
		setZ(l.getZ());
	}
	
	/**
	 * Función que suma las coordenadas de 2 localizaciones
	 * @param l
	 * @return Location
	 */
	public Location add(Location l) {
		if(world.equals(l.world)) {
			setX(x + l.x);
			setY(y + l.y);
			setZ(z + l.z);
		}
		else {
			System.err.println("Cannot add Locations of differing worlds.");
		}
		
		return this;
	}
	
	
	/**
	 * Función que calcula la distancia entre 2 localizaciones del mismo mundo
	 * @param l
	 * @return double
	 */
	public double distance(Location l) {
		if(l.getWorld() == null || getWorld() == null) {
			System.err.println("Cannot measure distance to a null world");
			return -1.0;
		}
		else if(l.getWorld().equals(getWorld())) {
			double dx = getX() - l.getX();
			double dy = getY() - l.getY();
			double dz = getZ() - l.getZ();
			
			return Math.sqrt(dx*dx + dy*dy + dz*dz);
		}
		else {
			System.err.println("Cannot measure distance between " + world.getName() + " and " + l.world.getName());
			return -1.0;
		}
	}
	/**
	 * Devuelve el mundo de la localización
	 * @return world
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * Funcion que modifica el mundo
	 * @param w
	 */
	public void setWorld(World w) {
		this.world = w;
	}
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		if(y <= UPPER_Y_VALUE) {
			if(y >= 0.0) this.y = y;
			else this.y = 0.0;
		}
		else this.y = UPPER_Y_VALUE;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Función que devuelve la longitud
	 * @return double
	 */
	public double length() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Funcion que multiplica las coordenadas por el factor
	 * @param factor
	 * @return Location
	 */
	public Location multipliy(double factor) {
		
		setX(getX() * factor);
		setY(getY() * factor);
		setZ(getZ() * factor);
		return this;
	}
	
	/**
	 * Función que resta 2 localizaciones
	 * @param l
	 * @return Location
	 */
	public Location substract(Location l) {
		
		if(world.equals(l.world)) {
			setX(x - l.x);
			setY(y - l.y);
			setZ(z - l.z);
		}
		else {
			System.err.println("Cannot substract Locations of differing worlds.");
		}
		
		return this;
	}
	
	/**
	 * Función que establece x,y,z a 0
	 * @return this
	 */
	public Location zero() {
		setX(0);
		setY(0);
		setZ(0);
		return this;
	}
	
	/**
	 * Función hashCode
	 * @return result
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
	 * Función que compara si el estado de 2 objetos es igual 
	 * @param obj
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Location other = (Location) obj;
		
		if (Double.compare(getX(), other.getX()) == 0 && Double.compare(getY(), other.getY()) == 0 && Double.compare(getZ(), other.getZ()) == 0)
			return true;
		else return false;
	}

	/**
	 * Función toString()
	 * @return string
	 */
	public String toString() {
		String resultado;
		resultado = "Location{world=";
		if(world == null) resultado = resultado + "NULL";
		else resultado = resultado + world.getName();
		
		resultado = resultado + ",x=" + getX() + ",y=" + getY() + ",z=" + getZ() + "}";
		return resultado;
	}
	
}
