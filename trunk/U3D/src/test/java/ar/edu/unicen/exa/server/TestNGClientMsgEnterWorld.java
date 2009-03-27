package ar.edu.unicen.exa.server;


import java.util.logging.Logger;
import org.testng.annotations.*;


import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.notify.MsgChangeWorld;

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
	
	@AfterClass
	 void desconectar(){
		simpleClient.logout();
	}
	
  	@Test(groups = {"success"})
	public void TestChangeWorld() {
  		/** Mundos: 
	 		buffet = 2
			exa = 5
			exterior = 510
			isistan = 513
			econ = 518
			ac1 = 521
		*/
  		assert changeWorld("2");
	}
	
  	/**
  	 * NOTA: Tenemos los mundos 100 y 201, pero cualquier otro tambien funciona.
  	 * 
  	 */
  	@Test(groups = {"failure"})
	public void TestChangeUnknownWorld() {
		assert !changeWorld("000");
	}
  	
  	/** Recibe un id de mundo e intenta enviar un mensaje de cambio de mundo
  	 * por el canal suscripto.
  	 * @return boolean
  	 */

  	protected final boolean changeWorld(String worldId) {
  		LOGGER.info("Cambiando al mundo: '" + worldId + "' ...");
  		try {
			MsgChangeWorld cwMsg = (MsgChangeWorld) simpleClient.buildMessageEnterWorld(worldId);
			simpleClient.sendMessage(cwMsg);
			//bussy wait debido a los tiempos de retardo desde el server
			//debido a la asincronia en la conexion y envio de mensajes
			
			while (!simpleClient.isConnected())
				LOGGER.info("Not connected !!");
				
			// validamos que realmente haya cambiado de mundo
			return cwMsg.getIdNewWorld() == worldId;
  		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
  		//return true;
	}
	
}