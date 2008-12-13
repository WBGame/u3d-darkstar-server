package ar.edu.unicen.exa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;

/**
 * Test para mensajes de movimiento (MsgMove).
 * 
 * Esta clase es utilizada para testear el envío y recepcion de mensajes del
 * tipo MsgMove. Este mensaje es enviado al servidor el cual procesará el 
 * mismo mensaje enviandolo a los correspondientes clientes y mostrando el 
 * mensaje recivido por pantalla. 
 * 
 * @author Pablo Inchausti <pabloinchausti at hotmail dot com/>
 * @encoding UTF-8   
 */
public class ClientMsgMove implements SimpleClientListener {

	/** Creamos un logger para esta clase. */
	private static final  Logger logger =
		Logger.getLogger(ClientMsgMove.class.getName());
	
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
		ClientMsgMove cmm = new ClientMsgMove();

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader flujo = new BufferedReader(isr);		
		
		try {
			
			System.out.print("Name: ");
			cmm.setLogin(flujo.readLine());
			
			System.out.print("Password: ");
			cmm.setPassword(flujo.readLine());
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}

		cmm.login();
		
		String opcion = null;
		do {
			try {
				System.out.print("Enviar mensaje de movimiento? (s/n): ");
				opcion = flujo.readLine();
				IMessage message = cmm.buildMessage();
				cmm.printMessage(message);
				cmm.sendToChannel(message); 
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
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
	protected ClientMsgMove() {
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
	 * Se crea un autenticador para el cliente, el cual se utilizara para
	 * posteriormente chequear que el usuario y contraseña sean validos. 
	 * 
	 * @return PasswordAuthentication la autenticacion para el cliente con
	 * nombre de usuario name y contraseña password. 
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
		this.channel = channel;
		String channelName = channel.getName();
		logger.info("Suscripto al canal " + channelName);
		return new ChannelListener();
	}
	/**
	 * 
	 * Se informa que se ha recibido un mensaje que provino del servidor.
	 * 
	 * @param message mensaje que resive el cliente.   
	 *  
	 */
	public final void receivedMessage(final ByteBuffer message) {
		logger.info("El servidor ha enviado el mensaje: ");
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
	 * Codifica el texto y lo envía directamente al servidor.
	 * 
	 */
	
	protected final void send() {
		try {
			ByteBuffer message = null;
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

	/**
	 * 
	 * @return el nombre del usuario.
	 */
	
	public final String getLogin() {
		return login;
	}
	
	/**
	 * 
	 * @param login el nombre del usuario.
	 */
	
	public final void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * 
	 * @return el password del usuario.
	 */
	
	public final String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password contraseña del usuario.
	 */
	
	public final void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Contruye un mensaje de movimiento {@link MsgMove} simulando la 
	 * posicion actual y destino del jugador. Dicho mensaje se codifica
	 * en un {@link ByteBuffer} para ser enviado a travez del canal. 
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 */
	
	private IMessage buildMessage() {
		
		MsgMove iMsg = null;
		
		try {
			//	Creo un mensaje
			iMsg = (MsgMove) MessageFactory.getInstance()
			.createMessage(MsgTypes.MSG_MOVE_SEND_TYPE);

		} catch (UnsopportedMessageException e) {
			e.printStackTrace();
		}

		// Creo origen
		Vector3f origen =  new Vector3f();
		origen.set(10, 15, 20);
		
		// Seteo el origen
		iMsg.setPosOrigen(origen);
		
		// Creo Destino
		Vector3f destino =  new Vector3f();
		destino.set(0, 10, 10);
		
		// Seteo el destino
		iMsg.setPosDestino(destino);
		
		// Seteo el id de la Entidad
		iMsg.setIdDynamicEntity(login);
				
		return iMsg;
	}
	
    /**
     * Envia un mensaje a travez del canal suscripto al cliente.
     */
	
	public final void sendToChannel(IMessage message) {
    	
    	// Convierto Mensaje a ByteBuffer
		ByteBuffer msj = message.toByteBuffer();
    	
        try {
                this.channel.send(msj);
                logger.info("Se ha enviado el mensaje MsgMove al canal " 
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
	
	public final void printMessage(IMessage message){
		MsgMove iMsg = (MsgMove) message;
		logger.info("Tipo de Mensaje : " + message.getType()
		+ " Pos Origen: " + iMsg.getPosOrigen()
		+ " Pos Destino: " + iMsg.getPosDestino()
		+ " Id Dynamic Entity: " + iMsg.getIdDynamicEntity());
	}
	
	/**
	 * Clase que permite capturar los eventos que ocurren sobre un canal.
	 * Define los métodos para recibir mensajes y para para capturar el
	 * evento de desubscripcion del usuario. 
	 *
	 */
	public class ChannelListener implements ClientChannelListener {

	    /**
	     * Constructora de la clase.
	     */
	    
	    public ChannelListener() {

	    }
	    
	    /**
	     * Este método es invocado cuando el servidor desuscribe al usuario del
	     * canal channel al cual esta asociado. 
	     * 
	     * @param channel el canal que fue quitado el usuario.
	     */

	    public final void leftChannel(final ClientChannel channel) {
	    	logger.info("El usuario fue quitado del canal "
	    			+ channel.getName());
	    }
	    
	    /**
	     * Este método es invocado cada vez que el servidor envia un mensaje al
	     * canal channel.
	     * 
	     * @param channel el canal por el cual se recivió el mensaje.
	     * @param message mensaje que se recivió.
	     */
	    
	    public final void receivedMessage(
	    		final ClientChannel channel, final ByteBuffer message) {
	    	logger.info("Se ha recivido el mensaje del canal "
	    			+ channel.getName());
	    	IMessage iMessage = null;
			try {
				iMessage = MessageFactory.getInstance().createMessage(message);
			} catch (MalformedMessageException e) {
				e.printStackTrace();
			} catch (UnsopportedMessageException e) {
				e.printStackTrace();
			}
			
	    	printMessage(iMessage);
	    }
	}
}
