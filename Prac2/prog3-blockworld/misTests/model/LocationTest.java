package model;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import model.exceptions.BadLocationException;
import model.exceptions.StackSizeException;

public class LocationTest {
	
	World w1, w2, w3;
	Location l1, l2, l3,l4;
	
	
	@Before
	public void setUp() throws StackSizeException, BadLocationException {
		w1 = new World(0,50,"Marte");
		w2 = new World(0,51,"Saturno");
		w3 = new World(0,50,"Urano");
		l1 = new Location(w1,20,15,25);
		l2 = new Location(w2,-30,5,10);
		l3 = new Location(w3,21,0,10);
		l4 = new Location(w3,21,255,10);
	}
	
	@Test
	public void testCheck() {
		assertTrue(Location.check(l1));
		assertFalse(Location.check(l2));
	}
	
	@Test
	public void testAbove() throws BadLocationException{
		Location l3 = l1.above();
		assertEquals(l1.getY()+1, l3.getY(),0.01);
	}
	
	@Test
	public void testBelow() throws BadLocationException {
		Location l4 = l2.below();
		assertEquals(l2.getY()-1, l4.getY(),0.01);
	}
	
	@Test(expected=BadLocationException.class)
	public void testBelowError() throws BadLocationException {
		Location b = l3.below();
	}
	
	@Test(expected=BadLocationException.class)
	public void testAboveError() throws BadLocationException{
		Location b = l4.above();
	}
	
	@Test
	public void testGetNeighborhood() throws StackSizeException, BadLocationException {
		Set<Location> set = l1.getNeighborhood();
		assertEquals(17,set.size());
		
		World w = new World(0,100,"Hola");
		Location l = new Location(w,-49,2,50);
		Set<Location> set1 = l.getNeighborhood();
		assertEquals(11,set1.size());
	}
}
