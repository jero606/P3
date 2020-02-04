/**
 * 
 */
package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import model.exceptions.StackSizeException;

/**
 * @author jero
 *
 */
public class ItemStackTest {
	
	//Prueba del constructor y de los get
	@Test
	public void testConstructor_Getters() throws StackSizeException {
		ItemStack item = new ItemStack(Material.GRASS,2);
		ItemStack item2 = new ItemStack(Material.WOOD_SWORD,1);
		
		assertSame(Material.GRASS, item.getType());
		assertEquals(2, item.getAmount());
		
		assertSame(Material.WOOD_SWORD, item2.getType());
		assertEquals(1, item2.getAmount());
	}
	
	@Test
	public void testConstructorCopia() throws StackSizeException {
		ItemStack item3 = new ItemStack(Material.GRASS,2);
		ItemStack item4 = new ItemStack(item3);
		
		assertEquals(item3.getType(),item4.getType());
		assertEquals(item3.getAmount(), item4.getAmount());
	}
	
	@Test
	public void testSetAmount() throws StackSizeException{
		ItemStack item5 = new ItemStack(Material.GRANITE,4);
		item5.setAmount(10);
		assertEquals(10, item5.getAmount());
	}
	
	@Test
	public void testToString() throws StackSizeException{
		ItemStack item6 = new ItemStack(Material.APPLE,3);
		assertEquals("(APPLE,3)", item6.toString());
		
	}
	
	@Test(expected=StackSizeException.class)
	public void testSetAmountException() throws StackSizeException{
		ItemStack item7 = new ItemStack(Material.APPLE, 2);
		item7.setAmount(100);
	}
	
	@Test(expected=StackSizeException.class)
	public void testSetAmountException2() throws StackSizeException{
		ItemStack item7 = new ItemStack(Material.IRON_PICKAXE, 1);
		item7.setAmount(3);
	}
	
	@Test(expected=StackSizeException.class)
	public void testConstructorException() throws StackSizeException{
		ItemStack item8 = new ItemStack(Material.WOOD_SWORD,3);
	}
	
	@Test(expected=StackSizeException.class)
	public void testConstructorException2() throws StackSizeException{
		ItemStack item8 = new ItemStack(Material.GRASS,-30);
	}
}
