/**
 * 
 */
package model;

import model.exceptions.WrongMaterialException;

/**
 * Clase de LiquidBlock con sus métodos y sus atributos
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */
public class LiquidBlock extends Block {

	/**
	 * Variable daño
	 */
	private double damage;

	/**
	 * Constructor de LiquidBlock
	 * 
	 * @param material Tipo del material
	 * @throws WrongMaterialException Si el material no es liquido
	 */
	public LiquidBlock(Material material) throws WrongMaterialException {
		super(material);
		this.damage = material.getValue();

		if (!material.isLiquid()) {
			throw new WrongMaterialException(material);
		}
	}

	/**
	 * Constructor de copia
	 * 
	 * @param lb Bloque liquido a copiar
	 */
	protected LiquidBlock(LiquidBlock lb) {
		super(lb);
		this.damage = lb.damage;
	}

	/**
	 * Getter del damage
	 * 
	 * @return damage Daño que inflingue el bloque
	 */
	public double getDamage() {
		return damage;
	}

	/**
	 * Método clone
	 * 
	 * @return Copia de un bloque
	 */
	@Override
	public Block clone() {
		return new LiquidBlock(this);
	}

	/**
	 * Funcion hashCode
	 * 
	 * @return resultado del hashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(damage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Funcion equals
	 * 
	 * @param obj Objeto
	 * @return true si son iguales, false si no lo son
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LiquidBlock other = (LiquidBlock) obj;
		if (Double.doubleToLongBits(damage) != Double.doubleToLongBits(other.damage))
			return false;
		return true;
	}

}
