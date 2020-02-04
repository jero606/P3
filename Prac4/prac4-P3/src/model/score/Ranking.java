/**
 * 
 */
package model.score;

import java.util.SortedSet;
import java.util.TreeSet;

import model.exceptions.score.EmptyRankingException;

/**
 * Esta clase genérica mantiene automáticamente un ranking de puntuaciones del
 * tipo indicado por el parámetro de tipo ScoreType.
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 * @param <ScoreType> objeto ScoreType
 *
 */
public class Ranking<ScoreType extends Score<?>> {

	/**
	 * Variable scores
	 */
	private SortedSet<ScoreType> scores;

	/**
	 * Constructor por defecto
	 */
	public Ranking() {
		scores = new TreeSet<ScoreType>();
	}

	/**
	 * Añade una puntuacion al ranking
	 * 
	 * @param e puntuacion para añadir
	 */
	public void addScore(ScoreType e) {
		scores.add(e);
	}

	/**
	 * Obtiene un conjunto ordenado de las puntuaciones
	 * 
	 * @return conjunto ordenado
	 */
	public SortedSet<ScoreType> getSortedRanking() {
		return scores;
	}

	/**
	 * Obtiene la puntuacion ganadora del ranking
	 * 
	 * @return al ganador
	 * @throws EmptyRankingException si puntuacion vacia
	 */
	public ScoreType getWinner() throws EmptyRankingException {
		if (scores.size() == 0) {
			throw new EmptyRankingException();
		} else {
			return scores.first();
		}
	}
}
