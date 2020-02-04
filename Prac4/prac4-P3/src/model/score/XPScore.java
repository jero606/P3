/**
 * 
 */
package model.score;

import java.util.ArrayList;

import model.entities.Player;
import model.exceptions.score.ScoreException;

/**
 * Esta clase representa una agregación de puntuaciones para un jugador.
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class XPScore extends Score<Player> {

	/**
	 * Variable del jugador
	 */
	private Player player;

	/**
	 * Variable que hace referencia a la agregación
	 */
	private ArrayList<Score<Player>> scores;

	/**
	 * Constructor a partir de un jugador
	 * 
	 * @param player jugador
	 */
	public XPScore(Player player) {
		super(player.getName());
		this.player = player;
		scores = new ArrayList<Score<Player>>();
	}

	/**
	 * Las puntuaciones se comparan por su valor.
	 * 
	 * @param sc Puntuacion del player
	 * @return -1,0,1
	 */
	public int compareTo(Score<Player> sc) {
		if (this.getScoring() > sc.getScoring()) {
			return -1;
		} else if (this.getScoring() == sc.getScoring()) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Calcula y almacena la media de las puntuaciones más el nivel de salud y el
	 * nivel de alimento del jugador ‘p’.
	 * 
	 * @param p Jugador
	 */
	public void score(Player p) {
		if (!player.equals(p)) {
			throw new ScoreException("Bad");
		} else if (scores.size() == 0) {
			score = p.getFoodLevel() + p.getHealth();
		} else {
			double total = 0;
			for (Score<Player> i : scores) {
				total = total + i.score;
			}
			score = total / scores.size() + p.getFoodLevel() + p.getHealth();
			
		}
	}

	/**
	 * Recalcula la puntuación y la devuelve.
	 * 
	 * @return puntuacion recalculada
	 */
	@Override
	public double getScoring() {
		score(player);
		return score;
	}

	/**
	 * Añade una nueva puntuación y recalcula la puntuación agregada.
	 * 
	 * @param score puntuacion
	 */
	public void addScore(Score<?> score) {
		scores.add((Score<Player>) score);
		getScoring();
	}
}
