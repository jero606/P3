/**
 * 
 */
package Prac1;

/**
 * @author Jerónimo Llorens Vera
 * @author 74371079G
 *
 */

/**
 * Clase World
 */
public class World {
	//-------------ATRIBUTOS--------------
	
	/**
	 * Variable del nombre del mundo
	 */
	private String name;
	
	//-------------METODOS-----------------
	
	/**
	 * Constructor que asigna un nombre a un mundo
	 * @param name
	 */
	public World(String name) {
		super();
		this.name = name;
	}

	/**
	 * Función que devuelve el nombre del mundo
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Función hashCode()
	 * @return result
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Función que compara si dos objetos tienen el mismo estado
	 * @param obj
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		World other = (World) obj;
		
		if (getName() == null && other.getName() != null) return false;
		else if (!getName().equals(other.getName())) return false;
		else return true;
	}
	

	/**
	 * Función que muestra el nombre del mundo
	 * @return string
	 */
	public String toString() {
		return getName();
	}
	
}
