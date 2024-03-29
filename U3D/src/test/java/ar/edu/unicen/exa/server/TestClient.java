package ar.edu.unicen.exa.server;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;*/

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;
//import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;

//import ar.edu.unicen.exa.server.ClientMsgMove.ChannelListener;


import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgAbstract;
import common.messages.MsgEmpty;
import common.messages.MsgTypes;
//import common.messages.MsgEmpty;
import common.messages.MsgPlainText;

import common.messages.notify.MsgChangeWorld;
import common.messages.notify.MsgMove;


/**
 * Cliente simple para ser usado desde los test scripts. Contiene la funcionalidad
 * necesaria para testear login, movimiento, envio de mensajes etc.
 * Cualquier otra funcionalidad que requiera ser testeada, puede extender esta
 * clase.
 * 
 * @author Gerónimo Díaz <geronimod at gmail dot com>
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
	
	/** Variables para poder cumplir con checkstyle. */
	/** Posicion origen del jugador. */
	private int X1 = 10;
	/** Posicion origen del jugador. */
	private int Y1 = 15;
	/** Posicion origen del jugador. */
	private int Z1 = 20;
	/** Posicion destino del jugador. */
	private int X2 = 0;
	/** Posicion destino del jugador. */
	private int Y2 = 10;
	/** Posicion destino del jugador. */
	private int Z2 = 10;
	private IMessage mensaje;	
	
	public int[] availableWorlds;
	
	protected void initWorlds(){
		availableWorlds = new int[6];
		availableWorlds[0]= 510; //exterior
		availableWorlds[1]= 5;   //exa
		availableWorlds[2]= 2; 	 //buffet
		availableWorlds[3]= 513; //isistan
		availableWorlds[4]= 518; //econ
		availableWorlds[5]= 521; //ac1
	}
	
	public int getRandomWorld(){
		return availableWorlds[(int)((Math.random()* availableWorlds.length-1))+1];
	}
	
	/**
	 * Creamos un cliente {@link SimpleClient}.
	 */
	
	protected TestClient() {
		initWorlds();
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
			Thread.sleep(5000);
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

	public boolean isLoggedIn(){
		return simpleClient.isConnected();
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
		logger.info("Falló el Logeo de usuario, razón: " + reason);
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
	 * Llamado por el server luego del login, asocia un canal al usuario conectado.
	 * 
	 * @param channel el canal por el cual se envian los mensajes.
	 * 
	 * @return ClientChannelListener Capturador de eventos que se producen 
	 * en el canal channel.
	 */

	public ClientChannelListener joinedChannel(
			final ClientChannel ch) {
		this.channel = ch;
		String channelName = ch.getName();
		logger.info("Suscripto al canal " + channelName);
		return new ChannelListener();

	}
	
	public boolean isJoinedToChannel(){
		return (this.channel != null);
		
	}
	
	public ClientChannel channel(){
		return this.channel;
	}
	/**
	 * 
	 * Se informa que se ha recibido un mensaje que provino del servidor.
	 * 
	 * @param message mensaje que resive el cliente.   
	 *  
	 */
	public final void receivedMessage(final ByteBuffer message) {
		try {
			 mensaje = MessageFactory.getInstance().createMessage(message);
		} catch (MalformedMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsopportedMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("El servidor ha enviado el mensaje: " 
				+ mensaje.getType());
	}

	/**Retorna el mensaje recibido del servidor */
	public IMessage serverMsg(){
		return mensaje;
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
	
	public IMessage buildMessageEnterWorld(String idMundo) {
		return buildMessageChangeWorld(idMundo);
	}

	public IMessage buildMessageEnterWorld(final String idMundo,final float x,
			final float y,final float z) {
		return buildMessageChangeWorld(idMundo,x,y,z);
	}

	/**
	 * Codifica el texto y lo envía directamente al servidor.
	 * 
	 * @param message mensaje a enviar directamente al servidor
	 * @throws IOException 
	 */
	
	protected boolean sendMessage(final IMessage message){
		// Convierto Mensaje a ByteBuffer
		boolean retorno=false;
		ByteBuffer msg = message.toByteBuffer();
    	
      		try {
				simpleClient.send(msg);
				retorno=true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		/*los mensajes en el paquete common.messages.notify no extienden
    			MsgPlainText sino de MsgAbstract, por eso falla el casting*/
    			MsgAbstract iMsg = (MsgAbstract) message;

    			logger.info("Se ha enviado el tipo de mensaje " 
    					+ iMsg.getType() 
    					+ " al servidor ");
    			return retorno;
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

	public boolean recibido(){
		boolean retorno= false;
	   	   if(mensaje!=null)
	   		   retorno=true;
		   return retorno;
	   }
	
	/**Construye el mensaje para la solicitud de datos del jugador
	 * @param identificador del jugador
	 * */
	
	public IMessage buildMessageGetPlayer(){
		MsgPlainText msg = null;
		try {
			msg = (MsgPlainText) MessageFactory.getInstance()
			.createMessage(MsgTypes.MSG_GET_PLAYER_TYPE);
		} catch (UnsopportedMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.setMsg(getLogin());
		return msg;
	} 
		
	/**
	 * Contruye un mensaje para entrar a un mundo {@link MsgPlainText} 
	 * estableciendo el id del mundo al que se desea ingresar. Dicho mensaje
	 * se codifica en un {@link ByteBuffer} para ser enviado directamente al
	 * servidor.
	 * 
	 * Se realiza mediante un cambio de mundo pq semanticamente es lo mismo
	 * 
	 * @return ByteBuffer el movimiento codificado en un ByteBuffer.
	 * @param idMundo mundo al que se desa ingresar
	 */
	
	public IMessage buildMessageChangeWorld(String idMundo){
		MsgChangeWorld msg=null;
		try {

			msg = (MsgChangeWorld) MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_CHANGE_WORLD_TYPE);
            msg.setIdNewWorld(idMundo);
			msg.setSpownPosition(new Vector3f(1,1,1));

		} catch (UnsopportedMessageException e1) {
			e1.printStackTrace();
		}
		return msg;
	}
	
	public IMessage buildMessageChangeWorld(final String idMundo,final float x,
											final float y,final float z) {
		MsgChangeWorld msg=null;
		try {

			msg = (MsgChangeWorld) MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_CHANGE_WORLD_TYPE);
            Vector3f pos=new Vector3f();
            pos.set(x,y,z);
			msg.setIdNewWorld(idMundo);
			msg.setSpownPosition(pos);

		} catch (UnsopportedMessageException e1) {
			e1.printStackTrace();
		}
		return msg;
	}
	
	public IMessage buildMessageMove(Vector3f from, Vector3f to){
		X1 = (int) from.x;
		Y1 = (int) from.y;
		Z1 = (int) from.z;
		
		X2 = (int) to.x;
		Y2 = (int) to.y;
		Z2 = (int) to.z;
		
		return buildMessageMove();
	}
	
	public IMessage buildMessageMove() {
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
		origen.set(X1, Y1, Z1);
		// Seteo el origen
		iMsg.setPosOrigen(origen);
		// Creo Destino
		Vector3f destino =  new Vector3f();
		// 0, 10, 10
		destino.set(X2, Y2, Z2);
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
	 * @throws Exception 
	 */
	
	public void sendToChannel(final IMessage message) throws Exception {

		// Convierto Mensaje a ByteBuffer
		ByteBuffer msj = message.toByteBuffer();
			while(!isJoinedToChannel())
			   logger.info("No Joined!!");
			logger.info("Joined to:" + this.channel.getName());
			this.channel.send(msj);
			
			/*los mensajes en el paquete common.messages.notify no extienden
			MsgPlainText sino de MsgAbstract, por eso falla el casting*/
			MsgAbstract msg = (MsgAbstract) message;

			logger.info("Se ha enviado el tipo de mensaje " 
					+ msg.getType() 
					+ " con el mensaje " 
					+ msg //.getMsg() 
					+ " a travez del canal "  
					+ this.channel.getName());
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
			logger.info("El usuario fue quitado del canal "
					+ ch.getName());
		}

		/**
		 * Este método es invocado cada vez que el servidor envia un mensaje al
		 * canal channel.
		 * 
		 * @param ch el canal por el cual se recibió el mensaje.
		 * @param msg mensaje que se recibió.
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

			MsgAbstract iMsg = (MsgAbstract) iMessage;
			logger.info("Se ha recibido del canal " + ch.getName() 
					+ " el tipo de mensaje: " + iMsg.getType() 
					+ " con el mensaje id jugador: " + iMsg);
		}
	}
	
}
