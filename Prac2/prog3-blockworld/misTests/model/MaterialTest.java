package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author jero
 *
 */
public class MaterialTest {
	
	Material m1, m2, m3, m4;

    /* Este metodo se ejecuta antes de cada Test,
     * de manera que m1, m2, m3, m4 son distintos en cada test */
    @Before
    public void setUp() {
        m1 = Material.GRASS;
        m2 = Material.APPLE;
        m3 = Material.IRON_SHOVEL;
        m4 = Material.WOOD_SWORD;
        
    }
    
    /**
     * Comprueba los getters
     */
    @Test
    public void testGetters() {
        assertSame("geSymbol", 'g', m1.getSymbol());
        assertEquals("getValue", 0.6, m1.getValue(),0.01);  // 0.01 is the maximum delta when comparing two doubles
        assertSame("geSymbol", '>', m3.getSymbol());
        assertEquals("getValue", 0.2, m3.getValue(),0.01);
    }
    
    @Test
    public void testIs() {
    	assertTrue(m1.isBlock());
    	assertTrue(m2.isEdible());
    	assertTrue(m3.isTool());
    	assertTrue(m4.isWeapon());
    	
    	assertFalse(m2.isBlock());
    	assertFalse(m1.isEdible());
    	assertFalse(m4.isTool());
    	assertFalse(m3.isWeapon());
    }
    
    @Test
    public void testRandom() {
    	Material m1 = Material.APPLE;
    	Material m2 = Material.getRandomItem(0, 10);
    	
    	while(m2 != m1) {
    		m2 = Material.getRandomItem(0, 10);
    	}
    	
    	assertEquals(m2,m1);
    }
    
    @Test
    public void testRandom2() {
    	Material m1 = Material.DIRT;
    	Material m2 = Material.getRandomItem(3, 3);
    	assertEquals(m1,m2);
    }
}
