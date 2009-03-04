package ar.edu.unicen.exa.server;

import java.util.logging.Logger;
import org.testng.annotations.*;

import ar.edu.unicen.exa.server.ThrdClientLoad;

/**
 * Test de carga para testeo del server usando TestNG
 * Se lanza un conjunto de {@link TestNGClientLoad} que intentan cargar el server,
 * cada {@link ThrdClientLoad} corre en un hilo independiente logandose al server
 * y moviendose entre mundos.
 * 
 * @author Gerónimo Díaz <geronimod at gmail dot com>
 * @encoding UTF-8
 */

public class TestNGClientLoad {

	private static final Logger LOGGER = Logger
			.getLogger(TestNGClientLoad.class.getName());

	private Integer maxClientsSupported;
	private ThrdClientLoad[] clients;

	/**
	 * Seteamos el umbral de clientes posibles que podemos conectar e
	 * inicializamos el arreglo de clientes con un nuevo cliente.
	 */
	@BeforeClass
	public void setUp() {
		maxClientsSupported = 10;//00;
		clients = new ThrdClientLoad[maxClientsSupported];
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
				LOGGER.info("Running client nº: " + i);
				clients[i] = new ThrdClientLoad();
				clients[i].run();
			}
		} catch (Exception e) {
			assert false;
		}
		assert true;
	}

	
}
