/**
 * 
 */

package ar.edu.unicen.exa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

/**
 * Test user login.
 * 
 * @author Kopp Roberto <robertokopp at hotmail dot com/>
 * @encoding UTF-8
 * 
 * Esta clase es utilizada para testear el login. COntiene una implementacion 
 * basica de un cliente y solo tiene la funcionalidad para logearse por medio 
 * del nombre y password, y enviar mensajes.   
 */
public class TestLogin implements SimpleClientListener {

	/** Creamos un logger para esta clase. */
	private final Logger logger =
		Logger.getLogger(TestLogin.class.getName());
	
	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/** Instancia {@link SimpleClient} para este cliente. */
	private final SimpleClient simpleClient;
	
	/**
	 * El nombre que ingresa el usuario. 
	 */
	private String name;
	/**
	 * El password que ingresa el usuario.
	 */
	private String password;

	/**
	 * Corre el test del login.
	 *
	 * @param args los argumentos de la linea de comando.
	 */
	public static void main(final String[] args) {
		TestLogin ct = new TestLogin();
		
		try {
			
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader flujo = new BufferedReader(isr);
		
			System.out.print("Name: ");
			this.name = flujo.readLine();
			
			System.out.print("Password: ");
			this.password = flujo.readLine();
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}

		ct.login();
		
		try {
			
			System.out.print("Texto a enviar al servidor: ");
			String texto = flujo.readLine();
			ct.send(texto); 
		
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Creamos un cliente {@link SimpleClient}.
	 */
	protected TestLogin() {
		simpleClient = new SimpleClient(this);
	}

	/**
	 * Inicializa asincronicamente el login especificando las propiedades 
	 * host y port del cliente.
	 */
	protected final void login() {
		try {
			Properties connectProps = new Properties();
			connectProps.put("host", "localhost");
			connectProps.put("port", "1119");
			simpleClient.login(connectProps);
		} catch (Exception e) {
			e.printStackTrace();
			disconnected(false, e.getMessage());
		}
	}

	/**
	 * Codifica un {@code String} dentro de un {@link ByteBuffer}.
	 *
	 * @param s el string a codificar
	 * @return el {@code ByteBuffer} correspondiente a la codificacion del
	 *  string.
	 */
	protected static ByteBuffer encodeString(final String s) {
		try {
			return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET 
					+ " not found", e);
		}
	}

	/**
	 * Decodifica un {@link ByteBuffer} en un {@code String}.
	 *
	 * @param buf el {@code ByteBuffer} a decodificar
	 * @return el string decodificado
	 */
	protected static String decodeString(final ByteBuffer buf) {
		try {
			byte[] bytes = new byte[buf.remaining()];
			buf.get(bytes);
			return new String(bytes, MESSAGE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET 
					+ " not found", e);
		}
	}

	/**
	 * Se crea un autenticador para el cliente, el cual se utilizara para
	 * posteriormente chequear que el usuario y contraseña sean validos. 
	 * 
	 * @return PasswordAuthentication la autenticacion para el cliente con
	 * nombre de usuario name y contraseña password. 
	 */
	public final PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(name, password.toCharArray());
	}

	/**
	 * Informa al usuario que se logeo correctamente.
	 */
	public final void loggedIn() {
		logger.info("Usuario logeado.");
	}

	/**
	 * Informa al usuario que el name/password es incorrecto.
	 * 
	 * @param reason la razon por la cual el login fallo.
	 */
	public final void loginFailed(final String reason) {
		logger.info("Falló el Logeo del usuario, razón: " + reason);
	}

	/**
	 * Informa al usuario que se desconecto del servidor.
	 * 
	 * @param reason la razon por la cual el usuario se ha desconectado.
	 * @param graceful si {@code true}, el cliente se desconecta
	 *        correctamente.
	 */
	public final void disconnected(final boolean graceful, 
			final String reason) {
		logger.info("Desconectado: " + reason);
	}

	/**
	 * No tiene funcionalidad asociada debido a que no se hace uso de los 
	 * channels.
	 * 
	 * @param channel el canal por el cual se envian los mensajes.
	 * 
	 * @return ClientChannelListener Capturador de eventos que se producen 
	 * en el canal channel.
	 */
	public final ClientChannelListener joinedChannel(
			final ClientChannel channel) {
		return null;
	}
	/**
	 * 
	 * Se informa que se ha recibido un mensaje que provino del servidor.
	 * 
	 * @param message mensaje que resive el cliente.   
	 *  
	 */
	public final void receivedMessage(final ByteBuffer message) {
		logger.info("El servidor ha enviado el mensaje: " 
				+ decodeString(message));
	}

	/**
	 * Informa al cliente que se ha reconectado.
	 * 
	 */
	public final void reconnected() {
		logger.info("reconectado");
	}

	/**
	 * Informa al cliente que esta reconectando.
	 */
	public final void reconnecting() {
		logger.info("reconectando");
	}

	/**
	 * Codifica el texto y lo envia al servidor.
	 * 
	 * @param text el texto ha enviar
	 */
	protected final void send(final String text) {
		try {
			ByteBuffer message = encodeString(text);
			simpleClient.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Se utiliza la instancia de simpreClient para conocer el estado actual 
	 * de conexion correspondiente al usuario.
	 * 
	 * @return boolean Indica si el usuario esta conectado.
	 */
	
	public final boolean isConnected() {
		return simpleClient.isConnected();
	}
	
}
