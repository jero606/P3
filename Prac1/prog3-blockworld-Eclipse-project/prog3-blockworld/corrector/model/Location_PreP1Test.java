package model;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

/**
 * Tests previos a la entrega de la práctica 1 Cada método @Test corresponde con
 * una prueba unitaria (unit test) Algunos de ellos están sin completar.
 * Contienen una instrucción 'fail' que hace que siempre fallen. Busca el
 * comentario '// TODO' para localizarlos y completalos correctamente. Usa los
 * que ya están hechos como ejemplo. Estos test (incluyendo los que faltan por
 * completar) serán los que se usen para corregir la práctica.
 * 
 * @author paco
 *
 */

public class Location_PreP1Test {

	World earth, mars;
	Location le, lm;

	/*
	 * Este metodo se ejecuta antes de cada Test, de manera que los bjetos 'earth',
	 * 'mars', 'le' y 'lm' son distintos en cada test
	 */
	@Before
	public void setUp() {
		earth = new World("Earth");
		mars = new World("Mars");

		le = new Location(earth, 1.1, 2.2, 3.3);
		lm = new Location(mars, 10.01, 20.02, 30.03);
	}

	/**
	 * Comprueba los getters
	 */
	@Test
	public final void testGetters() {
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 1.1, le.getX(), 0.01); // 0.01 is the maximum delta when comparing two doubles
		assertEquals("getY", 2.2, le.getY(), 0.01);
		assertEquals("getZ", 3.3, le.getZ(), 0.01);
	}

	/**
	 * Comprueba los getters
	 */
	@Test
	public final void testGetters_2() {
		// fail("add similar asserts for 'lm'");
		assertSame("getWorld", mars, lm.getWorld());
		assertEquals("getX", 10.01, lm.getX(), 0.01);
		assertEquals("getY", 20.02, lm.getY(), 0.01);
		assertEquals("getZ", 30.03, lm.getZ(), 0.01);
	}

	/**
	 * Comprueba los setters
	 */
	@Test
	public final void testSetters() {

		le.setWorld(mars);
		assertSame("setWorld", mars, le.getWorld());

		le.setX(1.5);
		assertEquals("setX", 1.5, le.getX(), 0.01);

		le.setY(2.6);
		assertEquals("setY", 2.6, le.getY(), 0.01);

		le.setZ(3.7);
		assertEquals("setZ", 3.7, le.getZ(), 0.01);
	}

	/**
	 * Comprueba los setters
	 */
	@Test
	public final void testSetters_2() {
		// fail("add similar asserts for 'lm'");
		lm.setWorld(earth);
		assertSame("setWorld", earth, lm.getWorld());

		lm.setX(5);
		assertEquals("setX", 5, lm.getX(), 0.01);

		lm.setY(1);
		assertEquals("setY", 1, lm.getY(), 0.01);

		lm.setZ(4);
		assertEquals("setZ", 4, lm.getZ(), 0.01);
	}

	/**
	 * Comprueba que un valor 'y' fuera de rango es acotado por el setter
	 */
	@Test
	public final void testSettersIncorrectY() {

		le.setY(-0.06);
		assertEquals("setY", 0.0, le.getY(), 0.01);

	}

	/**
	 * Comprueba que un valor de 'y' superior a UPPER_Y_VALUE es acotado por el
	 * setter
	 */
	@Test
	public final void testSettersIncorrectY_2() {
		// fail("test setting 'y' coordinate beyond UPPER_Y_VALUE");

		lm.setY(300);
		assertEquals("setY", 255, lm.getY(), 0.01);
	}

	/**
	 * Comprueba el constructor sobrecargado, con valor 'y' fuera de rango
	 */
	@Test
	public final void testConstructor() {
		Location ly;

		ly = new Location(earth, -15.7, -16.8, -18.9);
		assertSame("getWorld", earth, ly.getWorld());
		assertEquals("getX", -15.7, ly.getX(), 0.01);
		assertEquals("getY", 0.0, ly.getY(), 0.01);
		assertEquals("getZ", -18.9, ly.getZ(), 0.01);
	}

	/**
	 * Comprueba el constructor cuando el valor de 'y' es mayor que el de
	 * UPPER_Y_VALUE
	 */
	@Test
	public final void testConstructor_2() {
		// fail("test calling the overloaded constructor with 'y' value beyond
		// UPPER_Y_VALUE");
		Location ly = new Location(mars, 2, 500, 30);
		assertSame("getWorld", mars, ly.getWorld());
		assertEquals("getX", 2, ly.getX(), 0.01);
		assertEquals("getY", 255, ly.getY(), 0.01);
		assertEquals("getZ", 30, ly.getZ(), 0.01);
	}

	/**
	 * Comprueba el constructor de copia
	 */
	@Test
	public final void testCopyConstructor() {
		Location ly = new Location(le);
		assertEquals("Copia", ly, le);
	}

	/**
	 * Comprueba la operación add() con posiciones del mismo mundo
	 */
	@Test
	public final void testAddSameWorlds() {
		Location lie = new Location(earth, 3.3, 2.2, 1.1);
		lie.add(le);
		assertSame("getWorld", earth, lie.getWorld());
		assertEquals("getX", 4.4, lie.getX(), 0.01);
		assertEquals("getY", 4.4, lie.getY(), 0.01);
		assertEquals("getZ", 4.4, lie.getZ(), 0.01);
	}

	/**
	 * Comprueba la operación add() con posiciones en diferentes mundos
	 */
	@Test
	public final void testAddDifferentWorlds() {

		le.add(lm); // error
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 1.1, le.getX(), 0.01);
		assertEquals("getY", 2.2, le.getY(), 0.01);
		assertEquals("getZ", 3.3, le.getZ(), 0.01);
	}

	/**
	 * Comprueba la operación add() con el valor de 'y' fuera de rango
	 */
	@Test
	public final void testAdd_Y_OutOfRange() {
		// fail("Test adding 'le' to location [3.3,Location.UPPER_Y_VALUE,1.1] in the
		// same world");
		//PREGUNTAR si hay que crear la localizacion de aqui abajo
		Location lo = new Location(earth, 3.3, Location.UPPER_Y_VALUE, 1.1);

		lo.add(le);
		assertSame("getWorld", lo.getWorld(), le.getWorld());
		assertEquals("getX", 4.4, lo.getX(), 0.01);
		assertEquals("getY", Location.UPPER_Y_VALUE, lo.getY(), 0.01);
		assertEquals("getZ", 4.4, lo.getZ(), 0.01);
	}

	/**
	 * Comprueba el método que calcula la distancia entre dos posiciones
	 */
	@Test
	public final void testDistance() {
		assertEquals("distance (different worlds)", le.distance(lm), -1.0, 0.01);

		Location ld = new Location(le.getWorld(), le.getX() + 1, le.getY() + 2, le.getZ() + 2);
		assertEquals("distance", 3.0, le.distance(ld), 0.01);

		Location ldd = new Location(le.getWorld(), le.getX() + 3, le.getY() + 4, le.getZ() + 0);
		assertEquals("distance", 5.0, ldd.distance(le), 0.01);
	}

	/**
	 * Comprueba el método length()
	 */
	@Test
	public final void testLength() {
		assertEquals("lenght", 4.1158, le.length(), 0.01);

		Location ld = new Location(mars, 1, 2, 2);
		assertEquals("lenght", 3.0, ld.length(), 0.01);
	}

	/**
	 * Comprueba el método multiply()
	 */
	@Test
	public final void testMultiply() {

		le.multiply(2.0);
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 2.2, le.getX(), 0.01); // 0.01 is the maximum delta when comparing two doubles
		assertEquals("getY", 4.4, le.getY(), 0.01);
		assertEquals("getZ", 6.6, le.getZ(), 0.01);
	}

	/**
	 * Comprueba el método multiply cuando Y está fuera de rango
	 */
	@Test
	public final void testMultiply_Y_OutOfRange() {
		// fail("Test multiplying object 'le' by UPPER_Y_VALUE");

		le.multiply(Location.UPPER_Y_VALUE);
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 1.1 * Location.UPPER_Y_VALUE, le.getX(), 0.01);
		assertEquals("getY", Location.UPPER_Y_VALUE, le.getY(), 0.01);
		assertEquals("getZ", 3.3 * Location.UPPER_Y_VALUE, le.getZ(), 0.01);
	}

	/**
	 * Comprueba la resta entre localizaciones de mundos diferentes
	 */
	@Test
	public final void testSubstractDifferentWorlds() {

		le.substract(lm); // mundos diferentes
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 1.1, le.getX(), 0.01);
		assertEquals("getY", 2.2, le.getY(), 0.01);
		assertEquals("getZ", 3.3, le.getZ(), 0.01);
	}

	/**
	 * Comprueba la resta entre localizaciones del mismo mundo
	 */
	@Test
	public final void testSubstractSameWorlds() {

		Location lec = new Location(earth, 1, 3, 3);
		le.substract(lec);
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 0.1, le.getX(), 0.01);
		assertEquals("getY", 0.0, le.getY(), 0.01);
		assertEquals("getZ", 0.3, le.getZ(), 0.01);
	}

	/**
	 * Comprueba la resta entre la misma localización del mismo mundo
	 */
	@Test
	public final void testSubstractSameLocation() {
		// fail("test: le - le == (0,0,0)");

		le.substract(le);
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 0, le.getX(), 0.01);
		assertEquals("getY", 0, le.getY(), 0.01);
		assertEquals("getZ", 0, le.getZ(), 0.01);
	}

	/**
	 * Comprueba el método zero()
	 */
	@Test
	public final void testZero() {
		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 1.1, le.getX(), 0.01);
		assertEquals("getY", 2.2, le.getY(), 0.01);
		assertEquals("getZ", 3.3, le.getZ(), 0.01);

		le.zero();

		assertSame("getWorld", earth, le.getWorld());
		assertEquals("getX", 0.0, le.getX(), 0.01);
		assertEquals("getY", 0.0, le.getY(), 0.01);
		assertEquals("getZ", 0.0, le.getZ(), 0.01);
	}

	/**
	 * Comprueba toString()
	 */
	@Test
	public final void testToString() {
		assertEquals("toString", "Location{world=Earth,x=1.1,y=2.2,z=3.3}", le.toString());
		assertEquals("toString", "Location{world=Mars,x=10.01,y=20.02,z=30.03}", lm.toString());
	}

	/**
	 * Comprueba toString() con world=null
	 */
	@Test
	public final void testToStringNullWorld() {
		// fail("test the toString() method when applied to a null world location");
		// PREGUNTAR lo de si hay que establecer el mundo a null
		le.setWorld(null);
		assertEquals("toString", "Location{world=null,x=1.1,y=2.2,z=3.3}", le.toString());
	}

	/**
	 * Comprueba los equals
	 */
	@Test
	public final void testEquals() {
		Location le2 = new Location(earth, le.getX(), le.getY(), le.getZ());
		Location lce = new Location(le);
		lce.setWorld(mars);
		assertFalse(le.equals(null));
		assertTrue(le.equals(le));
		assertTrue(le.equals(le2));
		assertFalse(le.equals(lm));
		assertFalse(lm.equals(le));
		assertFalse(le.equals(lce));
		assertFalse(lm.equals(lce));
		assertFalse(lm.equals(mars));
	}
}
