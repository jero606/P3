/**
 * 
 */
package model.entities;

import model.ItemStack;
import model.Location;
import model.Material;
import model.exceptions.StackSizeException;

/**
 * @author jero
 *
 */
public class Animal extends Creature {

	/**
	 * Variable simbolo del animal
	 */
	private static char symbol = 'L';

	/**
	 * Constructor del animal
	 * 
	 * @param loc    Posicion del animal
	 * @param health Salud del animal
	 */
	public Animal(Location loc, double health) {
		super(loc, health);
	}

	/**
	 * Getter del simbolo
	 * 
	 * @return symbol Simbolo del animal
	 */
	@Override
	public char getSymbol() {
		return symbol;
	}

	/**
	 * Getter de los drops
	 * 
	 * @return los drops del animal
	 */
	public ItemStack getDrops() {
		ItemStack i1 = null;
		try {
			i1 = new ItemStack(Material.BEEF, 1);
		} catch (StackSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i1;
	}

	/**
	 * Funcion toString
	 * 
	 * @return la cadena
	 */
	@Override
	public String toString() {
		return "Animal [location=" + location + ", health=" + getHealth() + "]";
	}

}
