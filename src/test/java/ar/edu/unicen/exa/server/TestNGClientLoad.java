package ar.edu.unicen.exa.server;

import java.util.logging.Logger;
import org.testng.annotations.*;
import ar.edu.unicen.exa.server.TestClient;

/**
 * Test de carga para testeo del server usando TestNG 
 * 
 * @author Gerónimo Díaz <geronimod at gmail dot com>
 * @encoding UTF-8   
 */

public class TestNGClientLoad {

	private static final  Logger LOGGER =
		Logger.getLogger(TestNGClientLoad.class.getName());

	private Integer maxClientsSupported;
	private TestClient[] clients;

	
	/**
	 * Seteamos el umbral de clientes posibles que podemos conectar
	 * e inicializamos el arreglo de clientes con un nuevo cliente.
	 */
	@BeforeClass
	public void setUp(){
		maxClientsSupported = 100;
		clients = new TestClient[maxClientsSupported];
		for(int i = 0; i < maxClientsSupported; i++)
			clients[i] = new TestClient();			
	}
	
	/**
	 * el test pasa si al menos podemos conectar a 10 cliente 
	 * simultaneamente, hay que establecer el umbral para que 
	 * este test pase ya que no esta especificado en la documentación
	 *  
	 */
	@Test
	public void LoadTest() {
		try {
			for(int i = 0; i < clients.length; i++) {
				clients[i].setLogin("TestClient" + i);
				clients[i].setPassword("TestClient" + i);
				clients[i].login();
				LOGGER.info("TestClient nº:" + i + " Logged");
			}			
		} catch (Exception e) {
			assert false;
		}
		assert true;
	}
	
}
  	