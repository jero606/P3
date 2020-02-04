/**
 * 
 */
package mains;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.SortedSet;


import model.BlockWorld;
import model.Location;
import model.World;
import model.entities.Creature;
import model.entities.LivingEntity;
import model.entities.Player;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.score.EmptyRankingException;
import model.score.CollectedItemsScore;
import model.score.MiningScore;
import model.score.PlayerMovementScore;
import model.score.Ranking;
import model.score.XPScore;

/** Este programa juega varias partidas aleatorias con los parámetros proporcionados por el usuario.
 * Al final de sus ejecución muestra el ranking de puntuaciones de todas las partidas jugadas.
 * 
 * @author pierre
 *
 */
public final class Main4_ranking {
	
	/**
	 * Juega una partida aleatoria con los argumentos proporcionados
	 * @param seed semilla para crear el mundo
	 * @param size tamaño del mundo
	 * @param worldName nombre del mundo
	 * @param playerName nombre del jugador
	 * @param numMovs número de movimientos a realizar
	 * @return el mundo después de haber jugado la partida
	 */
	private static World playRandomWalk(int seed, int size, String worldName, String playerName, int numMovs) {
		BlockWorld game = BlockWorld.getInstance();	
		System.out.println("-> game.createWorld("+seed+","+size+","+worldName+","+playerName+")");
		World world = game.createWorld(seed, size, worldName, playerName);
		Random rng = new Random(world.getSeed());

		Player player = world.getPlayer();
		System.out.println(game.showPlayerInfo(player));
		int x,y,z;
		for (int i=0; i<numMovs; i++) // intentamos realizar numMovs movimientos
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
		
		return world;

	}
	
	/**
	 * Programa principal
	 * @param args argumentos de línea de órdenes
	 */
	public static void main(String args[]) {
		Ranking<CollectedItemsScore> itemsRanking = new Ranking<>();
		Ranking<MiningScore> miningRanking = new Ranking<>();
		Ranking<PlayerMovementScore> movementRanking = new Ranking<>();
		Ranking<XPScore> xpRanking = new Ranking<>();
		
		System.out.println(">>>>>>>>>>>> Competición BLOCKWORLD <<<<<<<<<<<<<");
		System.out.println(">> Bienvenido a la competición.");
		System.out.println(">> Introduce órdenes para jugar partidas en una línea con el formato");
		System.out.println(">>     <semilla> <tamaño del mundo> <nombre del jugador> <nombre del mundo>");
		System.out.println(">> donde <semilla> y <tamaño del mundo> son números enteros positivos.");
		System.out.println(">> Tras cada órden el programa jugará automáticamente la partida.");
		System.out.println(">> Para indicar que no hay más partidas, introduce cualquier palabra que no sea un número.");
		System.out.println(">> Al final de las partidas, el programa mostrará los rankings y los ganadores.");
		
		Scanner input = new Scanner(System.in);
		
		while (input.hasNextInt()) {
			// assume next line contains world creation instruction
			int seed = input.nextInt();
			int size = input.nextInt();
			String playerName = input.next();
			String worldName = input.nextLine();
			World world = playRandomWalk(seed, size, worldName, playerName, 1000);
			itemsRanking.addScore(BlockWorld.getInstance().getItemsScore());
			miningRanking.addScore(BlockWorld.getInstance().getMiningScore());
			movementRanking.addScore(BlockWorld.getInstance().getMovementScore());
			XPScore xp = new XPScore(world.getPlayer());
			xp.addScore(BlockWorld.getInstance().getItemsScore());
			xp.addScore(BlockWorld.getInstance().getMiningScore());
			xpRanking.addScore(xp);
		}
		
		input.close();
		
		// End of play, output Rankings
		
		SortedSet<CollectedItemsScore> itemScores =  itemsRanking.getSortedRanking();
		SortedSet<MiningScore> miningScores = miningRanking.getSortedRanking();
		SortedSet<PlayerMovementScore> movementScores = movementRanking.getSortedRanking();
		SortedSet<XPScore> xpScores = xpRanking.getSortedRanking();
		System.out.println("Collected items ranking: "+itemScores);
		System.out.println("Mining ranking: "+miningScores);
		System.out.println("Movement ranking: "+movementScores);
		System.out.println("Experience ranking: "+ xpScores);
		
		try {
			System.out.println("Collected items winner: "+itemsRanking.getWinner());
			System.out.println("Mining winner: "+miningRanking.getWinner());
			System.out.println("Best mover: "+movementRanking.getWinner());
			System.out.println("Most experienced player: "+xpRanking.getWinner());
		} catch (EmptyRankingException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
}
