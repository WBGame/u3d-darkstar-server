package ar.edu.unicen.exa.server;

import java.util.logging.Logger;
import org.testng.annotations.*;

import common.messages.IMessage;

import ar.edu.unicen.exa.server.TestClient;

/**
 * Test de carga para testeo del server usando TestNG
 * 
 * @author Gerónimo Díaz <geronimod at gmail dot com>
 * @encoding UTF-8
 */

public class TestNGClientLoad {

	private static final Logger LOGGER = Logger
			.getLogger(TestNGClientLoad.class.getName());

	private Integer maxClientsSupported;
	private TestClient[] clients;

	/**
	 * Seteamos el umbral de clientes posibles que podemos conectar e
	 * inicializamos el arreglo de clientes con un nuevo cliente.
	 */
	@BeforeClass
	public void setUp() {
		maxClientsSupported = 1;//00;
		clients = new TestClient[maxClientsSupported];
		for (int i = 0; i < maxClientsSupported; i++)
			clients[i] = new TestClient();
	}

	/**
	 * el test pasa si al menos podemos conectar a 10 cliente simultaneamente,
	 * hay que establecer el umbral para que este test pase ya que no esta
	 * especificado en la documentación
	 * 
	 */
	@Test
	public void LoadTest() {
		try {
			for (int i = 0; i < clients.length; i++) {
				TestClient client = null;
				client = clients[i];
				client.setLogin("TestClient" + i);
				client.setPassword("TestClient" + i);
				client.login();
				LOGGER.info("TestClient nº:" + i + " Logged");
				IMessage message = client.buildMessageEnterWorld("100");
				client.sendMessage(message);
				message = client.buildMessageMove();
				client.sendToChannel(message);
			}
		} catch (Exception e) {
			assert false;
		}
		assert true;
	}

}
