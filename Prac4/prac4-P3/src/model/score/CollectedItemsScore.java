/**
 * 
 */
package model.score;

import model.ItemStack;

/**
 * Representa una puntuación basada en el valor de los items recogidos por un
 * jugador en su deambular por un mundo BlockWorld.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class CollectedItemsScore extends Score<ItemStack> {

	/**
	 * Constructor
	 * 
	 * @param player nombre del jugador
	 */
	public CollectedItemsScore(String player) {
		super(player);
	}

	/**
	 * Comparamos las puntuaciones
	 * 
	 * @param i objeto a comparar
	 * @return -1,0,1
	 */
	public int compareTo(Score<ItemStack> i) {
		if (this.score > i.score) {
			return -1;
		} else if (this.score == i.score) {
			return 0;
		} else {
			return 1;
		}

	}

	/**
	 * Incrementa la puntuación del jugador en una cantidad igual al valor del
	 * material de los items multiplicado por la cantidad de items que contiene el
	 * ItemStack.
	 * 
	 * @param item objeto
	 */
	public void score(ItemStack item) {
		score = score + item.getAmount() * item.getType().getValue();
	}
}
