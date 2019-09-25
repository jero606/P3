package model;

/**
 * Clase World, donde están los atributos del World y sus respectivos métodos
 * 
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 */
public class World {
	// -------------ATRIBUTOS--------------

	/**
	 * Variable del nombre del mundo
	 */
	private String name;

	// -------------METODOS-----------------

	/**
	 * Constructor que asigna un nombre a un mundo
	 * 
	 * @param name Nombre del mundo
	 */
	public World(String name) {
		super();
		this.name = name;
	}

	/**
	 * Función que devuelve el nombre del mundo
	 * 
	 * @return El nombre de mundo
	 */
	public String getName() {
		return name;
	}

	/**
	 * Función equals()
	 * 
	 * @return true si son iguales, false si no lo son
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		World other = (World) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;

		return true;
	}

	/**
	 * Función hashCode()
	 * 
	 * @return El resultado obtenido del hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Función que muestra el nombre del mundo
	 * 
	 * @return El nombre del Mundo
	 */
	public String toString() {
		return getName();
	}
}
