/**
 * 
 */
package model.score;

import model.Block;

/**
 * Representa una puntuación basada en el valor del material de los bloques
 * destruídos por un jugador en su deambular por un mundo BlockWorld.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class MiningScore extends Score<Block> {

	/**
	 * Constructor
	 * 
	 * @param name del player
	 */
	public MiningScore(String name) {
		super(name);
	}

	/**
	 * Compara puntuaciones por su valor
	 * 
	 * @param block bloque a comparar
	 * @return -1,0,1
	 */
	public int compareTo(Score<Block> block) {
		if (this.score > block.score) {
			return -1;
		} else if (this.score == block.score) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Incrementa puntuaion en una cantidad igual al valor del bloque
	 * 
	 * @param block Bloque
	 */
	public void score(Block block) {
		score = score + block.getType().getValue();
	}
}
