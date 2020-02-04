/**
 * 
 */
package model;

import java.util.Random;

/**
 * Esta clase es el enum Material. En ella están todos los tipos de materiales
 * que nos ppodemos encontrar
 * 
 * @author Jeronimo Llorens Vera
 * @author 74371079G
 */
public enum Material {
	// BLOQUES
	BEDROCK(-1, '*'), CHEST(0.1, 'C'), SAND(0.5, 'n'), DIRT(0.5, 'd'), GRASS(0.6, 'g'), STONE(1.5, 's'),
	GRANITE(1.5, 'r'), OBSIDIAN(5, 'o'),
	// COMIDA
	WATER_BUCKET(1, 'W'), APPLE(4, 'A'), BREAD(5, 'B'), BEEF(8, 'F'),
	// HERRAMIENTA
	IRON_SHOVEL(0.2, '>'), IRON_PICKAXE(0.5, '^'),
	// ARMA
	WOOD_SWORD(1, 'i'), IRON_SWORD(2, 'I'),
	// BLOQUES LIQUIDOS
	LAVA(1, '#'), WATER(0, '@'),;

	/**
	 * Variable value
	 */
	double value;
	/**
	 * Variable symbol
	 */
	char symbol;

	/**
	 * Variable para el material random
	 */
	static Random rng = new Random(1L);

	/**
	 * Constructor de la clase Material
	 * 
	 * @param value  El valor que va a tener el material
	 * @param symbol El simbolo que va a tener el material
	 */
	Material(double value, char symbol) {
		this.value = value;
		this.symbol = symbol;
	}

	/**
	 * Getter
	 * 
	 * @return the value El valor del material
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Getter
	 * 
	 * @return the symbol El simbolo del material
	 */
	public char getSymbol() {
		return symbol;
	}

	/**
	 * Función que dice si es bloque
	 * 
	 * @return bool diciendo si es bloque
	 */
	public boolean isBlock() {
		boolean bloque = false;

		if (getSymbol() == '*')
			bloque = true;
		else if (getSymbol() == 'C')
			bloque = true;
		else if (getSymbol() == 'n')
			bloque = true;
		else if (getSymbol() == 'd')
			bloque = true;
		else if (getSymbol() == 'g')
			bloque = true;
		else if (getSymbol() == 's')
			bloque = true;
		else if (getSymbol() == 'r')
			bloque = true;
		else if (getSymbol() == 'o')
			bloque = true;
		else if (getSymbol() == '@')
			bloque = true;
		else if (getSymbol() == '#')
			bloque = true;
		else
			bloque = false;

		return bloque;
	}

	/**
	 * Indica si el material es comida
	 * 
	 * @return true si es comida o false si no lo es
	 */
	public boolean isEdible() {
		boolean comida = false;

		if (getSymbol() == 'W')
			comida = true;
		else if (getSymbol() == 'A')
			comida = true;
		else if (getSymbol() == 'B')
			comida = true;
		else if (getSymbol() == 'F')
			comida = true;
		else
			comida = false;

		return comida;
	}

	/**
	 * Indica si el material es una herramienta
	 * 
	 * @return boolean diciendo si es una herramienta o no
	 */
	public boolean isTool() {
		boolean herramienta = false;

		if (getSymbol() == '>')
			herramienta = true;
		else if (getSymbol() == '^')
			herramienta = true;
		else
			herramienta = false;

		return herramienta;
	}

	/**
	 * Indica si el material es un arma
	 * 
	 * @return boolean diciendo si es un arma o no
	 */
	public boolean isWeapon() {
		boolean arma = false;

		if (getSymbol() == 'i')
			arma = true;
		else if (getSymbol() == 'I')
			arma = true;
		else
			arma = false;

		return arma;
	}

	/**
	 * Indica si el bloque es liquido
	 * 
	 * @return boolean True si es liquido, false si no
	 */
	public boolean isLiquid() {
		if (getSymbol() == '@')
			return true;
		else if (getSymbol() == '#')
			return true;
		else
			return false;
	}

	/**
	 * Devuelve un material que está en la posicion de un numRandom entre 2 numeros
	 * 
	 * @param first primera posicion
	 * @param last  ultima posicion
	 * @return el material que está en la posición del numRandom
	 */
	public static Material getRandomItem(int first, int last) {
		int i = rng.nextInt(last - first + 1) + first;
		return values()[i];
	}
}
