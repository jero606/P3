package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;

public class PlayerTest {
	
	World w1;
	Player p1;
	
	@Before
	public void setUp() throws StackSizeException, BadLocationException {
		w1 = new World(1,77,"Marte");
		p1 = new Player("Rafa",w1);
	}
	
	@Test
	public void gettersTest() {
		assertEquals("Rafa",p1.getName());
		assertEquals(Player.MAX_HEALTH,p1.getHealth(),0.01);
		assertEquals(Player.MAX_FOODLEVEL,p1.getFoodLevel(),0.01);
		//Location locationEsperada = new Location(w1, 0,72,0);
		//assertEquals("Location inicial", locationEsperada, p1.getLocation());
	}
	
	@Test
	public void isDeadTest() {
		assertFalse(p1.isDead());
		p1.setHealth(0);
		assertTrue(p1.isDead());
	}
	
	//Prueba añadiendo objetos y comprobando el tamaño del inventario.
	@Test
	public void addItemsToInventoryTest() throws StackSizeException {
		assertEquals(0,p1.getInventorySize());
		ItemStack i1 = new ItemStack(Material.APPLE,3);
		ItemStack i2 = new ItemStack(Material.APPLE,1);
		p1.addItemsToInventory(i1);
		p1.addItemsToInventory(i2);
		assertEquals(2,p1.getInventorySize());
	}
	@Test(expected=BadInventoryPositionException.class)
	public void selectItemTest() throws BadInventoryPositionException {
		p1.selectItem(-3);
	}
	@Test(expected=BadInventoryPositionException.class)
	public void selectItemTest2() throws BadInventoryPositionException {
		p1.selectItem(75);
	}
	@Test(expected=BadInventoryPositionException.class)
	public void selectItemTest3() throws BadInventoryPositionException {
		p1.selectItem(1);
	}
	@Test(expected=BadInventoryPositionException.class)
	public void selectItemTest6() throws BadInventoryPositionException {
		p1.selectItem(0);
	}
	@Test(expected=BadInventoryPositionException.class)
	public void selectItemTest7() throws BadInventoryPositionException, StackSizeException {
		ItemStack i1 = new ItemStack(Material.APPLE,3);
		ItemStack i2 = new ItemStack(Material.APPLE,1);
		p1.addItemsToInventory(i1);
		p1.addItemsToInventory(i2);
		p1.selectItem(2);
	}
	
	//Prueba cuando tengo objeto en la mano
	@Test
	public void selectItemTest4() throws StackSizeException, BadInventoryPositionException {
		ItemStack i1 = new ItemStack(Material.APPLE,3);
		ItemStack i2 = new ItemStack(Material.BEEF,2);
		p1.addItemsToInventory(i1);
		p1.addItemsToInventory(i2);
		
		p1.selectItem(0);
		assertEquals(i1,p1.getInventory().getItemInHand());
		
		ItemStack espada = new ItemStack(Material.WOOD_SWORD, 1);
		assertEquals(espada,p1.getInventory().getItem(0));
		assertEquals(i2,p1.getInventory().getItem(1));
		
	}
	
	//Prueba cuando no tengo nada en la mano
	@Test
	public void selectItemTest5() throws StackSizeException, BadInventoryPositionException {
		p1.getInventory().clear();
		
		ItemStack i1 = new ItemStack(Material.APPLE,3);
		ItemStack i2 = new ItemStack(Material.BEEF,2);
		p1.addItemsToInventory(i1);
		p1.addItemsToInventory(i2);
		
		p1.selectItem(0);
		assertEquals(i1,p1.getInventory().getItemInHand());
		assertEquals(i2,p1.getInventory().getItem(0));
	}
	
	@Test(expected=EntityIsDeadException.class)
	public void useItemInHandTest() throws EntityIsDeadException, StackSizeException {
		p1.setHealth(0);
		p1.useItemInHand(2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void useItemInHandTest2() throws EntityIsDeadException, StackSizeException {
		p1.useItemInHand(0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void useItemInHandTest3() throws EntityIsDeadException, StackSizeException {
		p1.useItemInHand(-3);
	}
	
	//Tengo la espada en la mano
	@Test
	public void useItemInHandTest4() throws EntityIsDeadException, StackSizeException {
		p1.useItemInHand(3);
		assertEquals(Player.MAX_HEALTH, p1.getHealth(),0.01);
		assertEquals(Player.MAX_FOODLEVEL-0.3, p1.getFoodLevel(),0.01);
	}
	
	//No tengo nada en la mano
	@Test
	public void useItemInHandTest5() {
		p1.getInventory().setItemInHand(null);
		assertEquals(Player.MAX_HEALTH, p1.getHealth(),0.01);
		assertEquals(Player.MAX_FOODLEVEL, p1.getFoodLevel(),0.01);
	}
	
	//Uso algo que no es comida
	@Test
	public void useItemInHandTest6() throws EntityIsDeadException, StackSizeException {
		p1.useItemInHand(300);
		assertEquals(10, p1.getHealth(),0.01);
		assertEquals(0, p1.getFoodLevel(),0.01);
		
		p1.useItemInHand(50);
		assertEquals(5, p1.getHealth(),0.01);
		assertEquals(0, p1.getFoodLevel(),0.01);
	}
	
	//Me como 3 manzana de 3 teniendo la salud y alimento a tope
	@Test
	public void useItemInHandTest7() throws StackSizeException, EntityIsDeadException {
		ItemStack i1 = new ItemStack(Material.APPLE,3);
		p1.getInventory().setItemInHand(i1);
		//Primera manzana
		p1.useItemInHand(1);
		assertEquals(Player.MAX_FOODLEVEL, p1.getFoodLevel(),0.01);
		assertEquals(Player.MAX_HEALTH, p1.getHealth(),0.01);
		assertEquals(2,p1.getInventory().getItemInHand().getAmount());
		//Segunda manzana
		p1.useItemInHand(1);
		assertEquals(Player.MAX_FOODLEVEL, p1.getFoodLevel(),0.01);
		assertEquals(Player.MAX_HEALTH, p1.getHealth(),0.01);
		assertEquals(1,p1.getInventory().getItemInHand().getAmount());
		//Tercera manzana
		p1.useItemInHand(1);
		assertEquals(Player.MAX_FOODLEVEL, p1.getFoodLevel(),0.01);
		assertEquals(Player.MAX_HEALTH, p1.getHealth(),0.01);
		assertEquals(null,p1.getInventory().getItemInHand());
	}
	
	@Test
	public void useItemInHandTest8() throws StackSizeException, EntityIsDeadException {
		p1.setFoodLevel(15);
		p1.setHealth(15);
		
		ItemStack i1 = new ItemStack(Material.APPLE,5);
		p1.getInventory().setItemInHand(i1);
		
		p1.useItemInHand(1);
		assertEquals(19,p1.getFoodLevel(),0.01);
		assertEquals(15,p1.getHealth(),0.01);
		
		p1.useItemInHand(1);
		assertEquals(20,p1.getFoodLevel(),0.01);
		assertEquals(18,p1.getHealth(),0.01);
		
		p1.useItemInHand(2);
		assertEquals(Player.MAX_FOODLEVEL, p1.getFoodLevel(),0.01);
		assertEquals(Player.MAX_HEALTH, p1.getHealth(),0.01);
		assertEquals(1,p1.getInventory().getItemInHand().getAmount(),0.01);
	}
	
	@Test
	public void toStringTest() throws StackSizeException {
		assertEquals("Name=Rafa\n"
				+ "Location{world=Marte,x=0.0,y=72.0,z=0.0}\n"
				+ "Health=20.0\n"
				+ "Food level=20.0\n"
				+ "Inventory=(inHand=(WOOD_SWORD,1),[])",p1.toString());
		
		ItemStack i1 = new ItemStack(Material.APPLE,5);
		p1.addItemsToInventory(i1);
		
		assertEquals("Name=Rafa\n"
				+ "Location{world=Marte,x=0.0,y=72.0,z=0.0}\n"
				+ "Health=20.0\n"
				+ "Food level=20.0\n"
				+ "Inventory=(inHand=(WOOD_SWORD,1),[(APPLE,5)])",p1.toString());
		
		ItemStack i2 = new ItemStack(Material.BEEF,2);
		p1.addItemsToInventory(i2);
		p1.setHealth(15);
		p1.setFoodLevel(3.5);
		assertEquals("Name=Rafa\n"
				+ "Location{world=Marte,x=0.0,y=72.0,z=0.0}\n"
				+ "Health=15.0\n"
				+ "Food level=3.5\n"
				+ "Inventory=(inHand=(WOOD_SWORD,1),[(APPLE,5), (BEEF,2)])",p1.toString());
	}
		
}
