/**
 * 
 */
package model.entities;

import model.Location;

/**
 * Clase de la criatura que extiende de living...
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public abstract class Creature extends LivingEntity {

	/**
	 * Constructor de Creature
	 * 
	 * @param loc    Posicion de la criatura
	 * @param health Salud de la criatura
	 */
	public Creature(Location loc, double health) {
		super(loc, health);
	}
}
