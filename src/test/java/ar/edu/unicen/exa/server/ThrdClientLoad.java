package ar.edu.unicen.exa.server;

import java.util.logging.Logger;

import common.messages.IMessage;

import ar.edu.unicen.exa.server.TestClient;

/**
 * Client liviando que corre en un thread interno, logandose al server y 
 * cambiandose entre mundos.
 * 
 * @author Gerónimo Díaz <geronimod at gmail dot com>
 * @encoding UTF-8
 */

public class ThrdClientLoad implements Runnable {
	private static final Logger LOGGER = Logger
		.getLogger(TestNGClientLoad.class.getName());

	private TestClient c;
	private Thread t;
	
	public ThrdClientLoad(){
		c = new TestClient();
		t = new Thread(this);
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
		String woldId = "5";//exa
		for (int i = 0; i < 2; i++) {
			LOGGER.info("ThrdClient nº:" + t.getName() + " cambiando a mundo: " + woldId);
			IMessage message = c.buildMessageEnterWorld(woldId);
			c.sendMessage(message);
			message = c.buildMessageMove();
			c.sendToChannel(message);
			woldId = woldId == "513" ? "5" : "513"; //isistan
		}
		c.logout();
		
	}
	
	
}