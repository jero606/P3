/**
 * 
 */
package model;

import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

/**
 * Clase de SolidBlock con sus métodos y atributos
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class SolidBlock extends Block {

	/**
	 * Variable que hace referencia a la asociación con ItemStack
	 */
	private ItemStack drops;

	/**
	 * Constructor de SolidBlock
	 * 
	 * @param material Tipo del material
	 * @throws WrongMaterialException Si el material es liquido
	 */
	public SolidBlock(Material material) throws WrongMaterialException {
		super(material);
		drops = null;

		if (material.isLiquid()) {
			throw new WrongMaterialException(material);
		}
	}

	/**
	 * Constructor de copia
	 * 
	 * @param sb Bloque solido a realizar la copia
	 */
	protected SolidBlock(SolidBlock sb) {
		super(sb);
		this.drops = sb.drops;
	}

	/**
	 * Devuelve cierto si la cantidad de daño es >= que la dureza del material del
	 * bloque
	 * 
	 * @param damage Cantidad de daño
	 * @return boolean True si es cierto, false si no
	 */
	public boolean breaks(double damage) {
		return (damage >= super.getType().getValue());
	}

	/**
	 * Getter de los drops
	 * 
	 * @return drops Los drops
	 */
	public ItemStack getDrops() {
		return drops;
	}

	/**
	 * Setter de los drops
	 * 
	 * @param type   Tipo del material
	 * @param amount Cantidad del material
	 * @throws StackSizeException si el tamaño no es el adecuado
	 */
	public void setDrops(Material type, int amount) throws StackSizeException {

		if (this.getType() != Material.CHEST && amount != 1) {
			throw new StackSizeException();
		} else if (amount < 1 || amount > ItemStack.MAX_STACK_SIZE) {
			throw new StackSizeException();
		} else {
			drops = new ItemStack(type, amount);
		}
	}

	/**
	 * Clona el objeto
	 * 
	 * @return copia del objeto
	 */
	@Override
	public Block clone() {
		return new SolidBlock(this);
	}

	/**
	 * Funcion hashCode
	 * 
	 * @return resultado del hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((drops == null) ? 0 : drops.hashCode());
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto
	 * @return true si son iguales, false si no
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolidBlock other = (SolidBlock) obj;
		if (drops == null) {
			if (other.drops != null)
				return false;
		} else if (!drops.equals(other.drops))
			return false;
		return true;
	}

}
