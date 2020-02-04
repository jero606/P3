package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import model.exceptions.WrongMaterialException;

public class BlockFactory_PreP3Test {


	/* Comprueba con todos los materiales de bloque sólidos, la correcta creación 
	 * de bloques sólidos */
	@Test
	public void testSolidBlockFactory() {
		Material [] m = Material.values();
		Block b=null;
		for (int i =0; i<=7; i++)
			try {
				b=BlockFactory.createBlock(m[i]);
				assertNotNull(b);
				assertTrue (b instanceof SolidBlock);
			} catch (Exception e) {
				fail("Error: excepción "+e.getClass().toString()+" inesperada");
			}	
	}
	
	/* Comprueba con todos los materiales de bloques líquidos, la correcta 
	 * creación de bloques líquidos */
	
	@Test
	public void testLiquidBlockFactory() {
		Material [] m = Material.values();
		Block b=null;
		for (int i =16; i<=17; i++)
			try {
				b=BlockFactory.createBlock(m[i]);
				assertNotNull(b);
				assertTrue (b instanceof LiquidBlock);
			} catch (Exception e) {
				fail("Error: excepción "+e.getClass().toString()+" inesperada");
			}	
	}
	
	/* Con todos los materiales que no son de bloque, se intenta crear bloques 
	 * saltando en todos los casos la excepción WrongMaterialException
	 */
	
	@Test
	public void testBlockFactoryException() {
		Material [] m = Material.values();
		Block b = null;
		for (int i=8; i<=17; i++) {
			try {
				b = BlockFactory.createBlock(m[i]);
				//System.out.println(b.toString());
			} catch (WrongMaterialException e) {
				assertFalse(b instanceof SolidBlock);
				System.out.println(e.getMessage());
			}
		}
	}
}
