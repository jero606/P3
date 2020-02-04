/**
 * 
 */
package mains;

import model.BlockWorld;
import model.Player;
import model.World;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;

/**
 * Juega una partida de BlockWorld
 * 
 * @author pierre
 *
 */
public final class Main2_partida1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BlockWorld game = BlockWorld.getInstance();

		try {

			World world = game.createWorld(10, 100, "test");
			Player player = world.getPlayer();
			try {
				System.out.println("-> game.createWorld(10, 100, \"test\")");
				System.out.println(game.showPlayerInfo(player));

				System.out.println("-> game.movePlayer(player, 1, 0, 1)");
				game.movePlayer(player, 1, 0, 1);
				System.out.println(game.showPlayerInfo(player));

				System.out.println("-> game.movePlayer(player, -1, 0, 1)");
				game.movePlayer(player, -1, 0, 1);
				System.out.println(game.showPlayerInfo(player));

				System.out.println("-> game.movePlayer(player, 0, 0, -1)");
				game.movePlayer(player, 0, 0, -1);
				System.out.println(game.showPlayerInfo(player));

				System.out.println("-> game.movePlayer(player, -1, 0, 1)");
				game.movePlayer(player, -1, 0, 1);
				System.out.println(game.showPlayerInfo(player));

			} catch (BadLocationException | EntityIsDeadException e) {
				e.printStackTrace();
			}

		} catch (BadLocationException | StackSizeException e) {
			e.printStackTrace();
		}
	}

}
