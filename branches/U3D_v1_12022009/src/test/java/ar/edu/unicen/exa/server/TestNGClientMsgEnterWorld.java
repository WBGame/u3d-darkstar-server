package ar.edu.unicen.exa.server;

import java.util.logging.Logger;
import org.testng.annotations.*;
import ar.edu.unicen.exa.server.TestClient;

/**
 * Reescritura de ClientMsgEnterWorld utilizando TestNG 
 * Test para mensajes de entrada y cambio de mundo.
 * 
 * Esta clase es utilizada para testear el envío y recepcion de mensajes del
 * tipo MsgEnterWorld y MsgChangeWorld. Estos mensajes son enviados al servidor
 * el cual los procesará enviando los mensajes de respuesta correspondientes a
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
	}
	
  	@Test(groups = {"success"})
	public void TestChangeWorld() {
		assert changeWorld("1");
	}
	
  	@Test(groups = {"failure"})
	public void TestChangeUnknownWorld() {
		assert !changeWorld("inexistente");
	}
  	
  	/** Debido a que no se esta uniendo a ningun canal, el metodo sendToChannel
  	 * esta fallando con lo cual ambos test fallan, la misma version del test
  	 * aparentemente esta funcionando con otro common, por lo tanto estos test
  	 * deberian funcionar con el commmon correcto.
  	 * @return boolean
  	 */
  	protected final boolean changeWorld(String worldId) {
  		simpleClient.login();
  		LOGGER.info("Cambiando al mundo: '" + worldId + "' ...");
  		try {
  			simpleClient.sendToChannel(
  					simpleClient.buildMessageChangeWorld(worldId));
  		} catch (Exception e) {
			e.printStackTrace();
			simpleClient.logout();
			return false;
  		}
  		simpleClient.logout();
  		return true;
	}
	
}