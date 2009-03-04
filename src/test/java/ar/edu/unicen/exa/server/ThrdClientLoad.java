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
		LOGGER.info("ThrdClient nº:" + t.getName() + " Logged");
		IMessage message = c.buildMessageEnterWorld("201");
		c.sendMessage(message);
		message = c.buildMessageMove();
		c.sendToChannel(message);
	}
	
	
}