package ar.com.cubenet.client.leasson5;

import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

public class ClientExample implements SimpleClientListener {

	public static final String HOST_PROPERTY = "tutorial.host";
    public static final String DEFAULT_HOST = "localhost";
    public static final String PORT_PROPERTY = "tutorial.port";
    public static final String DEFAULT_PORT = "1119";
    
    protected SimpleClient simpleClient;
    
    public ClientExample() {
		super();
		this.simpleClient = new SimpleClient(this);
	}

	/**
	 * Para que funcione correctamente el mecanismo de login que provee 
	 * Darkstar se debe implementar este método que fue heredado de la
	 * interface SimpleClientListener. 
	 * */
    
    public PasswordAuthentication getPasswordAuthentication() {
		// TODO Auto-generated method stub
        String player = "pablo";
        String password = "pablo";
        return new PasswordAuthentication(player, password.toCharArray());
	}

	public void loggedIn() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Cuando del lado del servidor se realiza el chequeo de nombre y contraseña,
	 * se puede generar excepciones que hacen que se ejecute automaticamente este
	 * metodo debido a que no fue satisfactorio la informacion provista por el usuario.
	 * @param reason: en este string se puede indicar cual fue la causa del login
	 * fallido, si es por el nombre o por el password o por otras razones.
	 * */
	
	public void loginFailed(String reason) {
		// TODO Auto-generated method stub
		System.out.println("Login fallido: " + reason);		
	}

	public void disconnected(boolean graceful, String reason) {
		// TODO Auto-generated method stub
		
	}
	
	public ClientChannelListener joinedChannel(ClientChannel channel) {
		// TODO Auto-generated method stub
		return null;
	}

	public void receivedMessage(ByteBuffer message) {
		// TODO Auto-generated method stub
		
	}

	public void reconnected() {
		// TODO Auto-generated method stub
		
	}

	public void reconnecting() {
		// TODO Auto-generated method stub
		
	}
	
	protected void login() {
        String host = System.getProperty(HOST_PROPERTY, DEFAULT_HOST);
        String port = System.getProperty(PORT_PROPERTY, DEFAULT_PORT);

        try {
            Properties connectProps = new Properties();
            connectProps.put("host", host);
            connectProps.put("port", port);
            simpleClient.login(connectProps);
        } catch (Exception e) {
            e.printStackTrace();
            disconnected(false, e.getMessage());
        }
    }
	
	public static void main(String[] args) {
        new ClientExample().login();
        int i = 0;
        while(i != 9000000) i++;
    }
	
}
