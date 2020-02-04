/**
 * 
 */
package model.entities;

import model.Location;

/**
 * Clase Living entity con sus atributos y funciones
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public abstract class LivingEntity {
	/**
	 * Maxima vida que puede tener la entidad
	 */
	public final static double MAX_HEALTH = 20;

	/**
	 * Vida de la entidad
	 */
	private double health;

	/**
	 * Variable de la composicion
	 */
	protected Location location;

	/**
	 * Constructor de la entidad
	 * 
	 * @param loc    Posicion de la entidad
	 * @param health Salud de la entidad
	 */
	public LivingEntity(Location loc, double health) {
		location = loc;
		setHealth(health);
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
	 * Getter de la posicion
	 * 
	 * @return Location La posicion
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
		if (health >= LivingEntity.MAX_HEALTH) {
			this.health = LivingEntity.MAX_HEALTH;
		} else {
			this.health = health;
		}
	}

	/**
	 * Comprueba si la entidad está muerta
	 * 
	 * @return Boolean True si esta muerta, false si no
	 */
	public boolean isDead() {
		if (health <= 0)
			return true;
		else
			return false;
	}

	/**
	 * Getter del simbolo
	 * 
	 * @return char Simbolo a obtener
	 */
	public abstract char getSymbol();

	/**
	 * Resta amount a la salud
	 * 
	 * @param amount Cantidad a restar a la salud
	 */
	public void damage(double amount) {
		this.health = this.health - amount;
	}

	/**
	 * Funcion hashCode
	 * 
	 * @return resultado hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(health);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto a comprobar
	 * @return true si son iguales, false si no
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LivingEntity other = (LivingEntity) obj;
		if (Double.doubleToLongBits(health) != Double.doubleToLongBits(other.health))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	/**
	 * Funcion toString
	 * 
	 * @return la cadena de living
	 */
	public String toString() {
		return "LivingEntity [location=" + location + ", health=" + health + "]";
	}

}
