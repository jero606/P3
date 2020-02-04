package model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.exceptions.BadInventoryPositionException;
import model.exceptions.StackSizeException;

public class InventoryTest {
	
	Inventory i;
	ItemStack item, item2, item3, item4;
	@Before
	public void setUp() throws StackSizeException {
		i = new Inventory();
		item = new ItemStack(Material.APPLE,3);
		item2 = new ItemStack(Material.IRON_PICKAXE,1);
		item3 = new ItemStack(Material.BEEF,4);
		item4 = new ItemStack(Material.APPLE,4);
		
		i.addItem(item);
		i.addItem(item2);
		i.setItemInHand(item3);
	}

	@Test
	public void constructorTest(){
		Inventory i2 = new Inventory();
		assertEquals(0, i2.getSize());
	}
	
	@Test
	public void addItemTest() throws StackSizeException {
		assertEquals(3,i.addItem(item),0.01);
		assertEquals(1,i.addItem(item2),0.01);
	}
	
	@Test
	public void clearTest() throws StackSizeException {
		i.clear();
		assertEquals(null,i.getItemInHand());
		assertEquals(0,i.getSize());
	}
	
	@Test
	public void clearSlotTest() throws BadInventoryPositionException {
		assertEquals(2,i.getSize());
		i.clear(1);
		assertEquals(1,i.getSize());
		i.clear(0);
		assertEquals(0,i.getSize());
	}
	
	@Test
	public void firstTest() {
		assertEquals(0,i.first(Material.APPLE));
		assertEquals(1,i.first(Material.IRON_PICKAXE));
		
	}
	
	@Test
	public void getItemTest() {
		assertEquals(item, i.getItem(0));
		assertEquals(item2,i.getItem(1));
	}
	
	@Test
	public void getItemInHandTest() {
		assertEquals(item3,i.getItemInHand());
	}
	
	@Test
	public void setItem() throws StackSizeException, BadInventoryPositionException {
		assertEquals(item,i.getItem(0));
		assertEquals(item2,i.getItem(1));
		ItemStack i2 = new ItemStack(Material.BREAD,2);
		i.setItem(0,i2);
		assertEquals(i2, i.getItem(0));
		assertEquals(item2,i.getItem(1));
	}
	
	@Test
	public void toStringTest() {
		assertEquals("(inHand=(BEEF,4),[(APPLE,3), (IRON_PICKAXE,1)])", i.toString());
	}
	@Test
	public void toStringTest2() throws BadInventoryPositionException {
		i.clear(1);
		i.clear(0);
		assertEquals("(inHand=(BEEF,4),[])", i.toString());
	}
	
	@Test(expected=BadInventoryPositionException.class)
	public void clearSlotException() throws BadInventoryPositionException {
		i.clear(5);
	}
	@Test(expected=BadInventoryPositionException.class)
	public void clearSlotException2() throws BadInventoryPositionException {
		i.clear(70);
	}
	@Test(expected=BadInventoryPositionException.class)
	public void clearSlotException3() throws BadInventoryPositionException {
		i.clear(-5);
	}
	
	@Test(expected=BadInventoryPositionException.class)
	public void setItemException() throws BadInventoryPositionException, StackSizeException {
		ItemStack is = new ItemStack(Material.GRANITE,2);
		i.setItem(5, is);
	}
	
	@Test(expected=BadInventoryPositionException.class)
	public void setItemException2() throws BadInventoryPositionException, StackSizeException {
		ItemStack is = new ItemStack(Material.GRANITE,2);
		i.setItem(75, is);
	}
	
	@Test(expected=BadInventoryPositionException.class)
	public void setItemException3() throws BadInventoryPositionException, StackSizeException {
		ItemStack is = new ItemStack(Material.GRANITE,2);
		i.setItem(-6, is);
	}
	
	@Test
	public void setItemInHandTest() {
		i.setItemInHand(null);
		assertEquals(null,i.getItemInHand());
	}
	
	
}
