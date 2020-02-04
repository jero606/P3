/**
 * 
 */
package model.persistence;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import model.Block;
import model.ItemStack;
import model.Location;
import model.entities.Creature;

/**
 * Esta interfaz representa un mundo de Minetest.
 * 
 * @author Jeronimo Llorens Vera
 * @autho 74371079G
 *
 */
public interface IWorld {

	/**
	 * Obtiene un mapa ordenado de bloques indexado por objetos Location
	 * 
	 * @param loc posicion
	 * @return mapa ordenado
	 */
	public NavigableMap<Location, Block> getMapBlock(Location loc);

	/**
	 * Obtiene el valor limite negativo de las posiciones de este mundo
	 * 
	 * @return limite negativo
	 */
	public int getNegativeLimit();

	/**
	 * Obtiene el jugador de este mmundo
	 * 
	 * @return jugador
	 */
	public IPlayer getPlayer();

	/**
	 * Obtiene el limite positivo de las posiciones del mundo
	 * 
	 * @return limite positivo
	 */
	public int getPositiveLimit();

	/**
	 * Obtiene una lista con todas las criaturas del mundo
	 * 
	 * @param loc Posicion
	 * @return lista de criaturas
	 */
	public List<Creature> getCreatures(Location loc);

	/**
	 * Obtiene un mapa de objetos ItemStack indexado por su posicion en elmundo
	 * 
	 * @param loc Posicion
	 * @return mapa de objetos
	 */
	public Map<Location, ItemStack> getItems(Location loc);
}
