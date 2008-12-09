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
 * @author Kopp Roberto <robertokopp at hotmail dot com>
 * @encoding UTF-8
 * 
 * TODO describir mejor que hace este caso de test. 
 */
public class TestLogin implements SimpleClientListener {

	/** Creamos un logger para esta clase. */
	private static final Logger logger =
		Logger.getLogger(TestLogin.class.getName());
	
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/** The {@link SimpleClient} instance for this client. */
	protected final SimpleClient simpleClient;

	/**
	 * Runs an instance of this client.
	 *
	 * @param args the command-line arguments (unused)
	 */
	public static void main(String[] args) {
		TestLogin ct = new TestLogin();
		ct.login();

		// busy wait for connection.
		while (!ct.isConnected());
		
		try{
			System.out.print( "Ingrese el texto a enviar al servidor: " );
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader Flujo = new BufferedReader (isr);
			String texto = Flujo.readLine();
			ct.send(texto);
		}
		catch(IOException e){
			logger.log( Level.SEVERE , e.getMessage() );
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new client UI with the given window title.
	 *
	 * @param title the title for the client's window
	 */
	protected TestLogin() {
		simpleClient = new SimpleClient(this);
	}

	/**
	 * Initiates asynchronous login to the SGS server specified by
	 * the host and port properties.
	 */
	protected void login() {
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
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 *
	 * @param s the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
	protected static ByteBuffer encodeString(String s) {
		try {
			return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET +
					" not found", e);
		}
	}

	/**
	 * Decodes a {@link ByteBuffer} into a {@code String}.
	 *
	 * @param buf the {@code ByteBuffer} to decode
	 * @return the decoded string
	 */
	protected static String decodeString(ByteBuffer buf) {
		try {
			byte[] bytes = new byte[buf.remaining()];
			buf.get(bytes);
			return new String(bytes, MESSAGE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET +
					" not found", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns dummy credentials where user is "guest-&lt;random&gt;"
	 * and the password is "guest."  Real-world clients are likely
	 * to pop up a login dialog to get these fields from the player.
	 */
	public PasswordAuthentication getPasswordAuthentication() {
		String player = "pablo";
		String password = "pablo";
		return new PasswordAuthentication(player, password.toCharArray());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Enables input and updates the status message on successful login.
	 */
	public void loggedIn() {
		System.out.println("Player Logged In.");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Updates the status message on failed login.
	 */
	public void loginFailed(String reason) {
		logger.info( "Login failed: " + reason );
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Disables input and updates the status message on disconnect.
	 */
	public void disconnected(boolean graceful, String reason) {
		System.out.println("Disconnected: " + reason);
	}

	/**
	 *  {@inheritDoc}
	 */
	public ClientChannelListener joinedChannel(ClientChannel channel) {
		return null;
	}
	/**
	 * {@inheritDoc}
	 * <p>
	 * Decodes the message data and adds it to the display.
	 */
	public void receivedMessage(ByteBuffer message) {
		System.out.println("Server sent: " + decodeString(message));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Updates the status message on successful reconnect.
	 */
	public void reconnected() {
		System.out.println("reconnected");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Updates the status message when reconnection is attempted.
	 */
	public void reconnecting() {
		System.out.println("reconnecting");
	}

	/**
	 * Encodes the given text and sends it to the server.
	 * 
	 * @param text the text to send.
	 */
	protected void send(String text) {
		try {
			ByteBuffer message = encodeString(text);
			simpleClient.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected(){
		return simpleClient.isConnected();
	}
}
