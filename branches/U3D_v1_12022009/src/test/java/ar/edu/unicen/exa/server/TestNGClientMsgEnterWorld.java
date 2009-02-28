package ar.edu.unicen.exa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.*;

import common.messages.IMessage;

import ar.edu.unicen.exa.server.TestClient;

/**
 * Reescritura de ClientMsgEnterWorld utilizando TestNG 
 * Test para mensajes de entrada y cambio de mundo.
 * 
 * Esta clase es utilizada para testear el envío y recepcion de mensajes del
 * tipo MsgEnterWorld y MsgChangeWorld. Estos mensajes son enviados al servidor
 * el cual los procesar enviando los mensajes de respuesta correspondientes a
 * los restantes clientes. 
 
 * @author Gerónimo Díaz <geronimod at gmail dot com>
 * @encoding UTF-8   
 */

public class TestNGClientMsgEnterWorld {

	/** Creamos un logger para esta clase. */
	private static final  Logger LOGGER =
		Logger.getLogger(TestNGClientMsgEnterWorld.class.getName());

	private TestClient simpleClient;

	
	@BeforeClass
	public void setUp() {
		simpleClient = new TestClient();
  		simpleClient.setLogin("TestPlayer");
  		simpleClient.setPassword("TestPlayer");
  		simpleClient.login();
  	}
	
  	@Test(groups = {"success"})
	public void TestChangeWorld() {
		assert changeWorld("1");
	}
	
  	/**
  	 * Activar este test cuando sepamos que mundos hay disponibles.
  	 * 
  	 */
  	/*@Test(groups = {"failure"})
	public void TestChangeUnknownWorld() {
		assert !changeWorld(null);
	}*/
  	
  	/** Recibe un id de mundo e intenta enviar un mensaje de cambio de mundo
  	 * por el canal suscripto.
  	 * @return boolean
  	 */
  	protected final boolean changeWorld(String worldId) {
  		LOGGER.info("Cambiando al mundo: '" + worldId + "' ...");
  		try {
			IMessage message = simpleClient.buildMessageChangeWorld(worldId);
			simpleClient.sendToChannel(message);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
  		return true;
		
	}
	
}