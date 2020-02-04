package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

public class BlockTest {

	@Test
	public void testConstructor() throws WrongMaterialException {
		Block b1 = new Block(Material.BEDROCK);
		assertSame(Material.BEDROCK, b1.getType());
	}
	
	@Test(expected=WrongMaterialException.class)
	public void testConstructorException() throws WrongMaterialException{
		Block b2 = new Block(Material.APPLE);
	}
	
	@Test
	public void testConstructorCopia() throws WrongMaterialException{
		Block b3 = new Block(Material.BEDROCK);
		Block b4 = new Block(b3);
		assertEquals(b3,b4);
	}
	
	@Test
	public void testToString() throws WrongMaterialException{
		Block b5 = new Block(Material.GRANITE);
		assertEquals("[GRANITE]", b5.toString());
	}
	
	@Test
	public void testSetDrops() throws WrongMaterialException, StackSizeException{
		Block b6 = new Block(Material.GRANITE);
		ItemStack i1 = new ItemStack(Material.CHEST,5);
		
		b6.setDrops(Material.CHEST, 5);
		assertEquals(i1,b6.getDrops());
	}
	
	@Test(expected=StackSizeException.class)
	public void testSetDropsException() throws StackSizeException, WrongMaterialException{
		Block b7 = new Block(Material.GRANITE);
		b7.setDrops(Material.SAND, 3);
	}
}
