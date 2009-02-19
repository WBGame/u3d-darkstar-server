package ar.edu.unicen.exa.server;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;

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
import common.messages.MsgPlainText;
import common.messages.MsgTypes;

import org.testng.annotations.*;

/*
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


public class TestNGClientMsgEnterWorld implements SimpleClientListener {

	/** Creamos un logger para esta clase. */
	private static final  Logger LOGGER =
		Logger.getLogger(TestNGClientMsgEnterWorld.class.getName());

	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/** Instancia {@link SimpleClient} para este cliente. */
	private SimpleClient simpleClient;

	/** Login del usuario. */
	private String login;

	/** Password del usuario. */
	private String password;

	/**
	 * El canal al que esta subscrito el jugador.
	 */
	private ClientChannel channel;


	@BeforeGroups(groups = {"success", "failure"})
	public void setUp() {
  		simpleClient = new SimpleClient(this);
  		setLogin("TestPlayer");
		setPassword("TestPlayer");
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
  		login();
  		System.out.print("Cambiando al mundo: '" + worldId + "' ...");
  		try {
  			sendToChannel(buildMessageChangeWorld(worldId));
  		} catch (Exception e) {
			e.printStackTrace();
			logout();
			return false;
  		}
		logout();
  		return true;
	}
  	
  	
	/** 
	 * Logout from server.
	 */
	private void logout() {
		this.simpleClient.logout(false);
	}

	/**
	 * Inicializa asincronicamente el login especificando las propiedades 
	 * host y port del cliente.
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
	 * Se crea un autenticador para el cliente, el cual se utilizara para
	 * posteriormente chequear que el usuario y contraseña sean validos. 
	 * 
	 * @return PasswordAuthentication la autenticacion para el cliente con
	 * nombre de usuario name y contraseña password. 
	 */
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(
				getLogin(),
				getPassword().toCharArray() 
		);
	}

	/**
	 * Informa al usuario que se logeo correctamente.
	 */
	public void loggedIn() {
		LOGGER.info("Usuario logeado.");
	}

	/**
	 * Informa al usuario que el name/password es incorrecto.
	 * 
	 * @param reason la razon por la cual el login fallo.
	 */
	public void loginFailed(final String reason) {
		LOGGER.info("Falló el Logeo del usuario, razón: " + reason);
	}

	/**
	 * Informa al usuario que se desconecto del servidor.
	 * 
	 * @param reason la razon por la cual el usuario se ha desconectado.
	 * @param graceful si {@code true}, el cliente se desconecta
	 *        correctamente.
	 */
	public void disconnected(final boolean graceful, 
			final String reason) {
		LOGGER.info("Desconectado: " + reason);
	}

	/**
	 * No tiene funcionalidad asociada debido a que no se hace uso de los 
	 * channels.
	 * 
	 * @param ch el canal por el cual se envian los mensajes.
	 * 
	 * @return ClientChannelListener Capturador de eventos que se producen 
	 * en el canal channel.
	 */
	public ClientChannelListener joinedChannel(
			final ClientChannel ch) {
		this.channel = ch;
		String channelName = ch.getName();
		LOGGER.info("Suscripto al canal " + channelName);
		return new ChannelListener();
	}
	/**
	 * 
	 * Se informa que se ha recibido un mensaje que provino del servidor.
	 * 
	 * @param message mensaje que resive el cliente.   
	 *  
	 */
	public void receivedMessage(final ByteBuffer message) {
		LOGGER.info("El servidor ha enviado el mensaje: ");
	}

	/**
	 * Informa al cliente que se ha reconectado.
	 * 
	 */
	public void reconnected() {
		LOGGER.info("reconectado");
	}

	/**
	 * Informa al cliente que esta reconectando.
	 */
	public void reconnecting() {
		LOGGER.info("reconectando");
	}

	/**
	 * Codifica el texto y lo envía directamente al servidor.
	 * @param message el mensaje.
	 * 
	 */

	protected void send(final IMessage message) {
		// Convierto Mensaje a ByteBuffer
		ByteBuffer msg = message.toByteBuffer();

		try {
			simpleClient.send(msg);
			MsgPlainText iMsg = (MsgPlainText) message;

			LOGGER.info("Se ha enviado el tipo de mensaje " + iMsg.getType() 
					+ " con el mensaje mundo: " + iMsg.getMsg());

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
	public boolean isConnected() {
		return simpleClient.isConnected();
	}

	/**
	 * Getter.
	 * 
	 * @return el nombre del usuario.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Setter.
	 * 
	 * @param name el nombre del usuario.
	 */
	public void setLogin(final String name) {
		this.login = name;
	}

	/**
	 * Getter.
	 * 
	 * @return el password del usuario.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter.
	 * 
	 * @param pass contraseña del usuario.
	 */

	public void setPassword(final String pass) {
		this.password = pass;
	}

	/**
	 * Contruye un mensaje para cambiar de mundo {@link MsgChangeWorld} 
	 * Dicho mensaje se codifica en un {@link ByteBuffer} para ser enviado
	 * a travez del canal. 
	 * 
	 * @param idMundo identificador del mundo
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 */
	private IMessage buildMessageChangeWorld(final String idMundo) {

		MsgPlainText msg = null;
		try {
			msg = (MsgPlainText) MessageFactory.getInstance()
			.createMessage(MsgTypes.MSG_ENTER_WORLD_TYPE);

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

			LOGGER.info("Se ha enviado el tipo de mensaje " 
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
		LOGGER.info("Tipo de Mensaje : " + iMsg.getType()
				+ " Mundo: " /*+ iMsg.getMsg()*/);
	}

	
	/**
	 * Clase que permite capturar los eventos que ocurren sobre un canal.
	 * Define los mÃ©todos para recibir mensajes y para para capturar el
	 * evento de desubscripcion del usuario. 
	 *
	 */
	public final class ChannelListener implements ClientChannelListener {

		/**
		 * Constructora de la clase.
		 */
		public ChannelListener() { }

		/**
		 * Este mÃ©todo es invocado cuando el servidor desuscribe al usuario del
		 * canal channel al cual esta asociado. 
		 * 
		 * @param ch el canal que fue quitado el usuario.
		 */
		public void leftChannel(final ClientChannel ch) {
			LOGGER.info("El usuario fue quitado del canal "
					+ ch.getName());
		}

		/**
		 * Este mÃ©todo es invocado cada vez que el servidor envia un mensaje al
		 * canal channel.
		 * 
		 * @param ch el canal por el cual se reciviÃ³ el mensaje.
		 * @param msg mensaje que se reciviÃ³.
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
			LOGGER.info("Se ha recivido del canal " + ch.getName() 
					+ " el tipo de mensaje: " + iMsg.getType() 
					+ " con el mensaje id jugador: " + iMsg.getMsg());
		}
	}
}