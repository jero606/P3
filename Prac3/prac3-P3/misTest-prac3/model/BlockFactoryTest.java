package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.exceptions.WrongMaterialException;

public class BlockFactoryTest {
	
	@Test
	public void constructorTest() {
		
		try {
			SolidBlock s1 = new SolidBlock(Material.BEDROCK);
			Block b1 = BlockFactory.createBlock(Material.BEDROCK);
			assertEquals(s1, b1);
			
			LiquidBlock l1 = new LiquidBlock(Material.LAVA);
			Block b2 = BlockFactory.createBlock(Material.LAVA);
			assertEquals(l1,b2);
		} catch (WrongMaterialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected=WrongMaterialException.class)
	public void constructorTestException() throws WrongMaterialException {
		Block b1 = BlockFactory.createBlock(Material.APPLE);
	}
	
	
}
