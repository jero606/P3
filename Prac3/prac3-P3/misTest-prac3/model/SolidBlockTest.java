package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

public class SolidBlockTest {
	
	//No es tipo bloque
	@Test(expected=WrongMaterialException.class)
	public void testConstructor() throws WrongMaterialException {
		SolidBlock b1 = new SolidBlock(Material.APPLE);
	}
	
	//Es bloque liquido
	@Test(expected=WrongMaterialException.class)
	public void testConstructor2() throws WrongMaterialException {
		SolidBlock b1 = new SolidBlock(Material.LAVA);
	}
	
	//Prueba el constructor de SolidBlock
	@Test
	public void testConstructor3() {
		try {
			SolidBlock b1 = new SolidBlock(Material.CHEST);
			assertEquals(Material.CHEST, b1.getType());
			assertNull(b1.getDrops());
			b1.setDrops(Material.WATER, 4);
			ItemStack i1 = b1.getDrops();
			assertTrue(i1.getType()==Material.WATER);
			assertTrue(i1.getAmount()==4);
		} catch (WrongMaterialException | StackSizeException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConstructorCopia() {
		SolidBlock block;
		try {
			block = new SolidBlock(Material.BEDROCK);
			block.setDrops(Material.BEDROCK, 1);
		    SolidBlock auxblock = new SolidBlock(block);
		    assertEquals(Material.BEDROCK,auxblock.getType());
		    assertEquals(block.getDrops(),auxblock.getDrops());
		} catch (Exception e) {
			fail("Error: excepción "+e.getClass().toString()+" inesperada");
		}
	}
	
	@Test
	public void testBreaks() {
		try {
			SolidBlock block = new SolidBlock(Material.BEDROCK);
			assertTrue(block.breaks(3));
			SolidBlock b1 = new SolidBlock(Material.OBSIDIAN);
			assertFalse(b1.breaks(1));
			
		} catch (WrongMaterialException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testClone() {
		try {
			SolidBlock b1 = new SolidBlock(Material.GRASS);
			SolidBlock b2 = (SolidBlock)b1.clone();
			assertEquals(b1,b2);
		} catch (WrongMaterialException e) {
			e.printStackTrace();
		}
	}
}
