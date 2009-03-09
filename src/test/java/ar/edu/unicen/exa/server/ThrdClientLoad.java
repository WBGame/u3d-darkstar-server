package ar.edu.unicen.exa.server;

import java.util.logging.Logger;

import com.jme.math.Vector3f;

import common.messages.IMessage;
import common.messages.notify.MsgMove;
import common.messages.notify.MsgChangeWorld;

import ar.edu.unicen.exa.server.TestClient;

/**
 * Client liviando que corre en un thread interno, logandose al server y 
 * cambiandose entre mundos.
 * Se utiliza un estado interno para llevar registro si el cliente ha hecho
 * los movimientos y cambios de mundo correctamente, esta informacion se
 * utiliza desde el test script de carga para determinar si el test pasa.
 * 
 * @author Gerónimo Díaz <geronimod at gmail dot com>
 * @encoding UTF-8
 */

public class ThrdClientLoad implements Runnable {
	private static final Logger LOGGER = Logger
		.getLogger(TestNGClientLoad.class.getName());

	private TestClient c;
	private Thread t;
	private Boolean valid;
	
	public ThrdClientLoad(){
		c = new TestClient();
		t = new Thread(this);
		valid = false;
	}
	
	@Override
	public void run() {
		c.setLogin("ThrdClient" + t.getName());
		c.setPassword("ThrdClient" + t.getName());
		c.login();
		if (c.isLoggedIn())
			LOGGER.info("ThrdClient nº:" + t.getName() + " Logged");
		else
			LOGGER.info("ThrdClient nº:" + t.getName() + " NOT LOGGED!!");
		
		/** Mundos: utilizar los nombres fallo debido a que espera un entero
		 	buffet = 2
 			exa = 5
 			exterior = 510
 			isistan = 513
 			econ = 518
 			ac1 = 521
		   
		   vamos switcheando entre ambos mundos durante N intentos
		 * en cada switcheo enviamos un movimiento
		 * luego nos deslogamos
		 */
		String worldId = "5";//exa
		for (int i = 0; i < 2; i++) {
			LOGGER.info("ThrdClient nº:" + t.getName() + " cambiando a mundo: " + worldId);
			
			MsgChangeWorld cwMsg = (MsgChangeWorld) c.buildMessageEnterWorld(worldId);
			c.sendMessage(cwMsg);
			// validamos que realmente haya cambiado de mundo
			valid = cwMsg.getIdNewWorld() == worldId;
			
			Vector3f from = new Vector3f(0,0,0);
			Vector3f to   = new Vector3f(10,0,10);
			MsgMove mMsg = (MsgMove) c.buildMessageMove(from, to);
			c.sendToChannel(mMsg);
			// validamos que realmente esta en la posicion que se le indica
			valid = mMsg.getPosDestino().equals(to);
			
			worldId = worldId == "513" ? "5" : "513"; //isistan
		}
		c.logout();
		
	}
	
	/**
	 * Retorna el estado interno del thread, el estado interno se usa como validacion
	 * para tener nocion de si los movimientos y cambios de mundo se han hecho
	 * correctametne, desde el test script de carga se utiliza esta informacion
	 * para determinar si el test pasa o no.
	 * @return valid
	 */
	public boolean isValid(){
		return valid;		
	}
	
	
}