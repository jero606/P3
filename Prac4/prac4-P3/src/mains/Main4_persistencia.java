/**
 * 
 */
package mains;

import model.BlockWorld;
import model.World;
import model.persistence.IWorld;
import model.persistence.MinetestSerializer;
import model.persistence.WorldAdapter;

/** Este programa crea un mundo BlockWorld (línea 25) y lo exporta como un mundo de Minetest.
 * El usuario debe indicar como argumento de entrada en la línea de órdenes la ruta del directorio
 * donde se debe exportar el mundo.
 * 
 * @author pierre
 *
 */
public final class Main4_persistencia {

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			throw new IllegalArgumentException("Debes proporcionar la ruta donde guardar la partida");
		BlockWorld bw = BlockWorld.getInstance();
		World w = bw.createWorld(100, 100, "BlockWorld test", "singleplayer");
		IWorld world = new WorldAdapter(w);
		MinetestSerializer serializer = new MinetestSerializer(world);

		if  (serializer.serialize(args[0]))
			System.out.println("World "+w.getName()+" saved to disk.");
		
	}

}
