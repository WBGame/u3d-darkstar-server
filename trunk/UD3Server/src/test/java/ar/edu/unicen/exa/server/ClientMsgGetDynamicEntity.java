package ar.edu.unicen.exa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetDynamicEntityResponse;

/**
 * Test para mensajes de pedido de información al servidor.
 * 
 * Esta clase es utilizada para testear el envío y recepción de mensajes del
 * tipo MSG_GET_DYNAMIC_ENTITY_TYPE. Un jugador envia la solicitud de la 
 * informacion asociada a una entidad dinamica, el servidor procesará dicha 
 * solicitud y enviará el mensaje de respuesta al jugador que realizó el 
 * pedido.
 * 
 * Cabe aclarar que la solicitud se envia atravez de un canal y la respuesta 
 * llega al jugador no por un canal sino directamente desde el servidor. 
 * 
 * @author Pablo Inchausti <pabloinchausti at hotmail dot com/>
 * @encoding UTF-8   
 */
public final class ClientMsgGetDynamicEntity implements SimpleClientListener {

	/** Creamos un logger para esta clase. */
	private static final  Logger LOGGER =
		Logger.getLogger(ClientMsgGetDynamicEntity.class.getName());
	
	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/** Instancia {@link SimpleClient} para este cliente. */
	private final SimpleClient simpleClient;
	
	/** Login del usuario. */
	private String login;
	
	/** Password del usuario. */
	private String password;
	
	/**
	 * El canal al que esta subscrito el jugador.
	 */
	private ClientChannel channel;
	
	/**
	 * Corre el test para un mesaje de movimiento.
	 *
	 * @param args los argumentos de la linea de comando.
	 */
	public static void main(final String[] args) {
		ClientMsgGetDynamicEntity cmm = new ClientMsgGetDynamicEntity();

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader flujo = new BufferedReader(isr);		
		
		try {
			
			System.out.print("Name: ");
			cmm.setLogin(flujo.readLine());
			
			System.out.print("Password: ");
			cmm.setPassword(flujo.readLine());
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}

		cmm.login();
		
		try {
			System.out.print("Id del mundo al que desea ingresar: ");
			String idMundo = flujo.readLine();
			IMessage message = cmm.buildMessageEnterWorld(idMundo);
			cmm.send(message);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		
		String opcion = null;
		do {
			System.out.print("Enter para obtener la entidad dinamica: " 
					+ cmm.getLogin());
			try {
				flujo.readLine();
				IMessage message = 
					cmm.buildMessageGetDynamicEntity(cmm.getLogin());
				cmm.sendToChannel(message);
				opcion = flujo.readLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while("s".equals(opcion));

		cmm.logout();
	}

	/** 
	 * Logout from server.
	 */
	private void logout() {
		this.simpleClient.logout(false);
	}

	/**
	 * Creamos un cliente {@link SimpleClient}.
	 */
	protected ClientMsgGetDynamicEntity() {
		simpleClient = new SimpleClient(this);
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
	 * Se informa que se ha recibido un mensaje que provino directamente del
	 * servidor.
	 * 
	 * @param message mensaje que resive el cliente.   
	 *  
	 */
	public void receivedMessage(final ByteBuffer message) {
		
    	IMessage iMessage = null;
		try {
			iMessage = MessageFactory.getInstance().createMessage(message);
		} catch (MalformedMessageException e) {
			e.printStackTrace();
		} catch (UnsopportedMessageException e) {
			e.printStackTrace();
		}
		
		MsgGetDynamicEntityResponse iMsg = 
			(MsgGetDynamicEntityResponse) iMessage;
	
		LOGGER.info("Se ha recibido el mensaje directo del servidor " 
				+ " Tipo de Mensaje : " + iMsg.getType()
				+ " Mundo Actual: " + iMsg.getActualWorld()
				+ " Angulo: " + iMsg.getAngle()
				+ " Posición: " + iMsg.getPosition()
				+ " Id Dynamic Entity: " + iMsg.getIdDynamicEntity()
				+ " Skin: " + iMsg.getSkin()
		);
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
	 * 
	 * @param message mensaje que se envia directamente al servidor
	 */
	
	protected void send(final IMessage message) {
		// Convierto Mensaje a ByteBuffer
		ByteBuffer msg = message.toByteBuffer();
    	
        try {
        		simpleClient.send(msg);
                MsgPlainText iMsg = (MsgPlainText) message;

                LOGGER.info("Se ha enviado el tipo de mensaje " 
                		+ iMsg.getType() 
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
	 * Contruye un mensaje para entrar a un mundo {@link MsgPlainText} 
	 * estableciendo el id del mundo al que se desea ingresar. Dicho mensaje
	 * se codifica en un {@link ByteBuffer} para ser enviado a travez del canal 
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 * @param idMundo mundo al que se desa ingresar
	 */
	private IMessage buildMessageEnterWorld(final String idMundo) {
		
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
	 * Contruye un mensaje para obtener la informacion de una entidad dinamica
	 * {@link MsgPlainText}. Dicho mensaje se codifica en un {@link ByteBuffer}
	 * para ser enviado a travez del canal. 
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 * @param idDynamicEntity identificador de la entidad que se desea obtener
	 */
	private IMessage buildMessageGetDynamicEntity(
			final String idDynamicEntity) {
		
		MsgPlainText msg = null;
		try {
			msg = (MsgPlainText) MessageFactory.getInstance()
					.createMessage(MsgTypes.MSG_GET_DYNAMIC_ENTITY_TYPE);
			//seteo el mensaje con el id de la entidad
			msg.setMsg(idDynamicEntity);
			
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
	 * Clase que permite capturar los eventos que ocurren sobre un canal.
	 * Define los métodos para recibir mensajes y para para capturar el
	 * evento de desubscripcion del usuario. 
	 *
	 */
	public final class ChannelListener implements ClientChannelListener {

	    /**
	     * Constructora de la clase.
	     */
	    public ChannelListener() { }
	    
	    /**
	     * Este método es invocado cuando el servidor desuscribe al usuario del
	     * canal channel al cual esta asociado. 
	     * 
	     * @param ch el canal que fue quitado el usuario.
	     */
	    public void leftChannel(final ClientChannel ch) {
	    	LOGGER.info("El usuario fue quitado del canal "
	    			+ ch.getName());
	    }
	    
	    /**
	     * Este método es invocado cada vez que el servidor envia un mensaje al
	     * canal channel.
	     * 
	     * @param ch el canal por el cual se recivió el mensaje.
	     * @param msg mensaje que se recivió.
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
			MsgPlainText msgPlainText = (MsgPlainText) iMessage;
			LOGGER.info("Se ha recibido del canal "
					+ ch.getName() + " el tipo de mensaje: " 
					+ msgPlainText.getType() 
					+ " con el mensaje id jugador: "
					+ msgPlainText.getMsg());
	    }
	}
}
