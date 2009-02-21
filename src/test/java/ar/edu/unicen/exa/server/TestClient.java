package ar.edu.unicen.exa.server;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;*/
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;
//import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgEmpty;
import common.messages.MsgTypes;
//import common.messages.MsgEmpty;
import common.messages.MsgPlainText;


/**
 * Cliente simple para ser usado desde los test scripts
 * 
 * @author GerÛnimo DÌaz <geronimod at gmail dot com>
 * @encoding UTF-8   
 */

public class TestClient implements SimpleClientListener {

	/** Creamos un logger para esta clase. */
	private final static Logger logger =
		Logger.getLogger(TestLogin.class.getName());
	
	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/** Instancia {@link SimpleClient} para este cliente. */
	private final SimpleClient simpleClient;
	
	/** Login del usuario */
	private String login;
	/** Password del usuario */
	private String password;
	
	/**
	 * El canal al que esta subscrito el simpleClient.
	 */
	private ClientChannel channel;
	
	
	/**
	 * Creamos un cliente {@link SimpleClient}.
	 */
	protected TestClient() {
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

	/** Logout from server */
	protected final void logout() {
		this.simpleClient.logout(false);
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
	 * posteriormente chequear que el usuario y contrase√±a sean validos. 
	 * 
	 * @return PasswordAuthentication la autenticacion para el cliente con
	 * nombre de usuario name y contrase√±a password. 
	 */
	public final PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication( 
				getLogin(),
				getPassword().toCharArray() 
		);
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
		logger.info("FallÛ el Logeo de usuario, razÛn: " + reason);
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public IMessage buildMessageChangeWorld(final String idMundo) {

		MsgPlainText msg = null;
		try {
			msg = (MsgPlainText) MessageFactory.getInstance()
			.createMessage(MsgTypes.MSG_CHANGE_WORLD_TYPE);

			msg.setMsg(idMundo);

		} catch (UnsopportedMessageException e1) {
			e1.printStackTrace();
		}

		return msg;
	}
	
	/**
	 * Envia un mensaje a travez del canal suscripto al cliente.
	 * 
	 * @param message mensaje a enviar.
	 */
	public void sendToChannel(final IMessage message) {

		// Convierto Mensaje a ByteBuffer
		ByteBuffer msj = message.toByteBuffer();

		try {
			this.channel.send(msj);
			MsgPlainText iMsg = (MsgPlainText) message;

			logger.info("Se ha enviado el tipo de mensaje " 
					+ iMsg.getType() 
					+ " con el mensaje " 
					+ iMsg.getMsg() 
					+ " a travez del canal "  
					+ this.channel.getName());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Imprime el mensaje correspondiente al movimiento del jugador.
	 * 
	 * @param message el mensaje ha imprimir.
	 */

	public void printMessage(final IMessage message) {
		MsgEmpty iMsg = (MsgEmpty) message;
		logger.info("Tipo de Mensaje : " + iMsg.getType()
				+ " Mundo: " /*+ iMsg.getMsg()*/);
	}
	
	/**
	 * Clase que permite capturar los eventos que ocurren sobre un canal.
	 * Define los m√©todos para recibir mensajes y para para capturar el
	 * evento de desubscripcion del usuario. 
	 *
	 */
	public final class ChannelListener implements ClientChannelListener {

		/**
		 * Constructora de la clase.
		 */
		public ChannelListener() { }

		/**
		 * Este m√©todo es invocado cuando el servidor desuscribe al usuario del
		 * canal channel al cual esta asociado. 
		 * 
		 * @param ch el canal que fue quitado el usuario.
		 */
		public void leftChannel(final ClientChannel ch) {
			logger.info("El usuario fue quitado del canal "
					+ ch.getName());
		}

		/**
		 * Este m√©todo es invocado cada vez que el servidor envia un mensaje al
		 * canal channel.
		 * 
		 * @param ch el canal por el cual se recivi√≥ el mensaje.
		 * @param msg mensaje que se recivi√≥.
		 */
		public void receivedMessage(final ClientChannel ch, 
				final ByteBuffer msg) {
			IMessage iMessage = null;
			try {
				iMessage = MessageFactory.getInstance().createMessage(msg);
			} catch (MalformedMessageException e) {
				e.printStackTrace();
			} catch (UnsopportedMessageException e) {
				e.printStackTrace();
			}

			MsgPlainText iMsg = (MsgPlainText) iMessage;
			logger.info("Se ha recivido del canal " + ch.getName() 
					+ " el tipo de mensaje: " + iMsg.getType() 
					+ " con el mensaje id jugador: " + iMsg.getMsg());
		}
	}
	
}
