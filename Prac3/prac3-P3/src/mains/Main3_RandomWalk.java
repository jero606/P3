/**
 * 
 */
package mains;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import model.BlockWorld;
import model.Location;
import model.World;
import model.entities.Creature;
import model.entities.LivingEntity;
import model.entities.Player;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;

/**
 * @author pierre
 *
 */
public class Main3_RandomWalk {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BlockWorld game = BlockWorld.getInstance();	
		
		System.out.println("-> game.createWorld(123, 100, \"test\")");
		World world = game.createWorld(123, 100, "test");
		Random rng = new Random(world.getSeed());

		Player player = world.getPlayer();
		System.out.println(game.showPlayerInfo(player));
		int x,y,z;
		for (int i=0; i<1000; i++) // intentamos realizar 450 movimientos
		try {
			if (player.isDead()) {
				System.out.flush(); System.err.flush();
				System.err.println(player.getName()+" has died!!!!!"); System.err.flush();
				break;
			}
			x = rng.nextInt(3) - 1; // genera -1, 0 o 1 aleatoriamente
			y = rng.nextInt(3) - 1; 
			z = rng.nextInt(3) - 1;

			// Para que no se vaya 'por las nubes', si sale y==1, lo cambiamos el 50% de las veces por 0 o -1.
			// controlamos que  x, z no sean cero a la vez, para no generar demasadas veces (0,0,0)
			if (y==1 && ((x!=0) || (z!=0))) if (rng.nextDouble() > 0.5) y = - rng.nextInt(2);

			System.out.println("("+i+") -> game.movePlayer(player, "+x+", "+y+", "+z+")"); System.out.flush();
			game.movePlayer(player, x,y,z);
			
			if (rng.nextDouble() < 0.33) {// intentar orientar al jugador y usar el item de la mano un tercio de las veces.
				// Preferentemente, lo orientamos hacia una entidad, si la hay
				Collection<Creature> entities = world.getNearbyCreatures(player.getLocation());
				try  {
					if (entities.size() > 0) {
						Iterator<Creature> iterator = entities.iterator();
						int idx = rng.nextInt(entities.size());
						LivingEntity entity = null;
						while (idx-- >= 0) entity = iterator.next();
						Location entity_loc = entity.getLocation();
						entity_loc.substract(player.getLocation()); // hacerla relativa a la pos. del jugador
						x = (int)entity_loc.getX();
						y = (int)entity_loc.getY();
						z = (int)entity_loc.getZ();
						System.out.println("("+i+") -> game.orientatePlayer(player, "+x+", "+y+", "+z+")"); System.out.flush();
						game.orientatePlayer(player, x,y,z);
					} else { // orientarlo a una posición adyacente aleatoria.
						x = rng.nextInt(3) - 1; // genera -1, 0 o 1 aleatoriamente
						y = rng.nextInt(3) - 1;
						z = rng.nextInt(3) - 1;
						// Sólo un tercio de las veces lo dejamos que se oriente hacia arriba o hacia abajo
						if (Math.abs(y)==1 && rng.nextDouble() > 0.33)
							y = 0;
						System.out.println("("+i+") -> game.orientatePlayer(player, "+x+", "+y+", "+z+")"); System.out.flush();
						game.orientatePlayer(player,x, y, z);
					}
				} catch(BadLocationException e) { 
					// de orientatePlayer(). La capturamos aquí para que no se salte el siguiente if() en caso
					// de que la orientación no sea correcta
					System.err.println(e.getMessage()); System.err.flush();
				}
				int times = rng.nextInt(5)+1;
				System.out.println("("+i+") -> game.useItem(player, "+times+")"); System.out.flush();
				game.useItem(player, times); // usamos el item de 1 a 5 veces
				System.out.println(game.showPlayerInfo(player)); System.out.flush();
			}
			if (i % 20 == 0) { // cada 20 movimientos 
				if (player.getInventorySize() > 0) {
					int pos = rng.nextInt(player.getInventorySize());
					System.out.println("("+i+") -> player.selectItem("+pos+")"); System.out.flush();
					player.selectItem(pos);
				}
				System.out.println(game.showPlayerInfo(player)); System.out.flush();
			}
		} catch (BadLocationException | EntityIsDeadException | BadInventoryPositionException  e) {
			System.err.println(e.getMessage()); System.err.flush();
			System.out.println(game.showPlayerInfo(player)); System.out.flush();
		}
		System.out.println(game.showPlayerInfo(player));


	}

}
