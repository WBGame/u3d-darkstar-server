package ar.edu.unicen.exa.server;

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
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.notify.MsgChangeWorld;
import common.messages.notify.MsgMove;

/**
 * Test para mensajes de movimiento en celdas adyacentes (MsgMove).
 * 
 * Esta clase es utilizada para testear el envío y recepcion de mensajes del
 * tipo MsgMove. Este mensaje es enviado al servidor el cual procesará el 
 * mismo mensaje enviandolo a los correspondientes clientes y mostrando el 
 * mensaje recivido por pantalla. Además, se deberán recibir mensajes
 * indicando 
 * 
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com/&gt;
 * @encoding UTF-8   
 */
public final class ClientMsgMoveAdj implements SimpleClientListener {

	/** Creamos un logger para esta clase. */
	private static final  Logger LOGGER =
		Logger.getLogger(ClientMsgMoveAdj.class.getName());
	
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
	
	
	/** Variables para poder cumplir con checkstyle. Se le van a sumar 10 para
	 *  dirección de destino 
	 */
	/** Posicion origen del jugador VECINO. */
	private static final int VECINO_X1 = 110;
	/** Posicion origen del jugador VECINO. */
	private static final int VECINO_Y1 = 60;
	/** Posicion origen del jugador VECINO. */
	private static final int VECINO_Z1 = 20;
	/** Posicion destino del jugador PLAYER. */
	private static final int PLAYER_X1 = 60;
	/** Posicion destino del jugador PLAYER. */
	private static final int PLAYER_Y1 = 60;
	/** Posicion destino del jugador PLAYER. */
	private static final int PLAYER_Z1 = 60;
	
	/**
	 * Corre el test para un mesaje de movimiento.
	 *
	 * @param args los argumentos de la linea de comando.
	 */
	public static void main(final String[] args) {
		ClientMsgMoveAdj cmmPlayer = new ClientMsgMoveAdj();
		ClientMsgMoveAdj cmmVecino = new ClientMsgMoveAdj();
		LOGGER.info("====Login de los players====");
		cmmVecino.setLogin("vecino"); cmmVecino.setPassword("vecino"); cmmVecino.login();
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		cmmPlayer.setLogin("player"); cmmPlayer.setPassword("player"); cmmPlayer.login();
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		
		LOGGER.info("====Ingreso al mundo====");
		String idMundo = "100";
		cmmVecino.send(cmmVecino.buildMessageEnterWorld(idMundo));
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		cmmPlayer.send(cmmPlayer.buildMessageEnterWorld(idMundo));
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		
		LOGGER.info("====Posiciono al vecino en la celda de al lado====");
		IMessage messageMove = cmmVecino.buildMessageMoveVecino();
		cmmVecino.sendToChannel(messageMove);
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		LOGGER.info("====Posiciono al player en la celda de al lado del vecino====");
		cmmPlayer.sendToChannel(cmmPlayer.buildMessageMovePlayerInicial());
		
		
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		LOGGER.info("====Deslogueo====");
		cmmPlayer.logout();
		cmmVecino.logout();
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
	protected ClientMsgMoveAdj() {
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
		LOGGER.info(this.getLogin() + "->Suscripto al canal " + channelName);
		return new ChannelListener(this.getLogin());
	}
	/**
	 * 
	 * Se informa que se ha recibido un mensaje que provino del servidor.
	 * 
	 * @param message mensaje que resive el cliente.   
	 *  
	 */
	public void receivedMessage(final ByteBuffer message) {
		IMessage imessage = null;
		try {
			imessage = MessageFactory.getInstance().createMessage(message);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
			return;
		}
		LOGGER.info("El servidor ha enviado el mensaje: " + imessage.getType() 
				+ " al player " + this.getLogin());
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
	 * @param message mensaje a enviar directamente al servidor
	 */
	
	protected void send(final IMessage message) {
		// Convierto Mensaje a ByteBuffer
		ByteBuffer msg = message.toByteBuffer();
    	
        try {
        		simpleClient.send(msg);
        		MsgChangeWorld iMsg = (MsgChangeWorld) message;

                LOGGER.info("Se ha enviado el tipo de mensaje " 
                		+ iMsg.getType() 
                		+ " con el mensaje mundo: " + iMsg.getIdNewWorld());
                
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
	 * se codifica en un {@link ByteBuffer} para ser enviado directamente al
	 * servidor.
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 * @param idMundo mundo al que se desa ingresar
	 */
	private IMessage buildMessageEnterWorld(final String idMundo) {
		MsgChangeWorld msg = null;
		try {
			msg = (MsgChangeWorld) MessageFactory.getInstance()
					.createMessage(MsgTypes.MSG_CHANGE_WORLD_TYPE);
			msg.setIdNewWorld(idMundo);
			msg.setSpownPosition(new Vector3f(1, 1, 1));
		} catch (UnsopportedMessageException e1) {
			e1.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * Contruye un mensaje de movimiento {@link MsgMove} simulando la 
	 * posicion actual y destino del jugador. Dicho mensaje se codifica
	 * en un {@link ByteBuffer} para ser enviado a travez del canal. 
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 */
	
	private IMessage buildMessageMoveVecino() {
		
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
		
		// 10, 15, 20
		origen.set(VECINO_X1, VECINO_Y1, VECINO_Z1);


				
		// Seteo el origen
		iMsg.setPosOrigen(origen);
		
		// Creo Destino
		Vector3f destino =  new Vector3f();
		// 0, 10, 10
		destino.set(VECINO_X1+10, VECINO_Y1 +10, VECINO_Z1+10);

		
		// Seteo el destino
		iMsg.setPosDestino(destino);
		
		// Seteo el id de la Entidad
		iMsg.setIdDynamicEntity(login);
				
		return iMsg;
	}
	
	/**
	 * Contruye un mensaje de movimiento {@link MsgMove} simulando la 
	 * posicion actual y destino del jugador. Dicho mensaje se codifica
	 * en un {@link ByteBuffer} para ser enviado a travez del canal. 
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 */
	private IMessage buildMessageMovePlayerInicial() {
		
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
		
		// 10, 15, 20
		origen.set(PLAYER_X1, PLAYER_Y1, PLAYER_Z1);


				
		// Seteo el origen
		iMsg.setPosOrigen(origen);
		
		// Creo Destino
		Vector3f destino =  new Vector3f();
		// 0, 10, 10
		destino.set(PLAYER_X1+10, PLAYER_Y1+10, PLAYER_Z1+10);

		
		// Seteo el destino
		iMsg.setPosDestino(destino);
		
		// Seteo el id de la Entidad
		iMsg.setIdDynamicEntity(login);
				
		return iMsg;
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
    			MsgMove iMsg = (MsgMove) message;
    			
    			LOGGER.info("Se ha enviado el mensaje por el canal " 
    			+ channel.getName()
    			+ " Tipo de Mensaje : " + iMsg.getType()
    			+ " Pos Origen: " + iMsg.getPosOrigen()
    			+ " Pos Destino: " + iMsg.getPosDestino()
    			+ " Id Dynamic Entity: " + iMsg.getIdDynamicEntity());
                
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
		MsgMove iMsg = (MsgMove) message;
		LOGGER.info("Tipo de Mensaje : " + message.getType()
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
	public final class ChannelListener implements ClientChannelListener {
		private String userLogin;
	    /**
	     * Constructora de la clase.
	     */
	    public ChannelListener(String login) { 
	    	userLogin = login;
	    }
	    
	    /**
	     * Este método es invocado cuando el servidor desuscribe al usuario del
	     * canal channel al cual esta asociado. 
	     * 
	     * @param ch el canal que fue quitado el usuario.
	     */
	    public void leftChannel(final ClientChannel ch) {
	    	LOGGER.info("El usuario " + userLogin + " fue quitado del canal "
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
			
			if (iMessage.getType().equals(MsgTypes.MSG_ARRIVED_TYPE)) {
			
				MsgPlainText msgPlainText = (MsgPlainText) iMessage;
				LOGGER.info(userLogin + "->Se ha recibido del canal "
						+ ch.getName() + " el tipo de mensaje: " 
						+ msgPlainText.getType() 
						+ " con el mensaje id jugador: "
						+ msgPlainText.getMsg());
			
			} else if (iMessage instanceof MsgMove) {
				MsgMove iMsg = (MsgMove) iMessage;
				
				LOGGER.info(userLogin + "->Se ha recibido el mensaje del canal " 
						+ ch.getName()
						+ " Tipo de Mensaje : " + iMsg.getType()
						+ "Diciendo que '" + iMsg.getIdDynamicEntity() 
						+ "' se movio");
			} else if (iMessage instanceof MsgPlainText) {
				MsgPlainText iMsg = (MsgPlainText) iMessage;
				
				LOGGER.info(userLogin + "->Se ha recibido el mensaje del canal " 
						+ ch.getName()
						+ " Tipo de Mensaje : " + iMsg.getType()
						+ " Diciendo: " + iMsg.getMsg());
			} else {
		
				LOGGER.info(userLogin + "->Se ha recibido el mensaje del canal " 
						+ ch.getName()
						+ " Tipo de Mensaje : " + iMessage.getType());
			}
	    }
	}
}
