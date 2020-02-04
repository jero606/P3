/**
 * 
 */
package model.entities;

import model.Location;

/**
 * Clase Monster con sus atributos y métodos
 * 
 * @author Jerónimo Llorens Vera
 * @authot 74371079G
 *
 */
public class Monster extends Creature {

	/**
	 * Variable que contiene el simbolo del monstruo M
	 */
	private static char symbol = 'M';

	/**
	 * Constructor del monstruo
	 * 
	 * @param loc    Posicion del monstruo
	 * @param health Salud del monstruo
	 */
	public Monster(Location loc, double health) {
		super(loc, health);
	}

	/**
	 * Getter del simbolo
	 * 
	 * @return symbol Simbolo del monstruo
	 */
	public char getSymbol() {
		return symbol;
	}
	
	/**
	 * Funcion toString
	 * 
	 * @return la cadena del monster
	 */
	@Override
	public String toString() {
		return "Monster [location=" + location + ", health=" + getHealth() + "]";
	}
	
	
}
