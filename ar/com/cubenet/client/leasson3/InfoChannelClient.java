package ar.com.cubenet.client.leasson3;

import java.io.IOException;

import org.unicen.u3d.common.tarea1.Serializer;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.tutorial.client.lesson2.HelloChannelClient;

public class InfoChannelClient extends HelloChannelClient {
	
    /** The version of the serialized form of this class. */
	private static final long serialVersionUID = -6208093334168870447L;
    /**
     * Creates a new client UI.
     */
    public InfoChannelClient() {
        super(InfoChannelClient.class.getSimpleName());
    }
	@Override
	protected void login() {
		super.login();
		appendOutput("Type /help for help menu");
	}

	@Override
	protected void send(String text) {
		//Se sobreescribe este método para aceptar comandos que comiencen con /
    	if(text.startsWith("/")){
    		if(text.startsWith("/help")){
    			appendOutput("Available commands:");
    			appendOutput("\t /leave\t->\tleave the selected channel");
    			appendOutput("\t /whoami\t->\tprint the name of the current client");
    			appendOutput("\t /who\t->\tlist clients");
    			appendOutput("\t /exit\t->\tclose this client");
    			appendOutput("\t /bye\t->\tdisconnect");
    		}else if(text.startsWith("/leave")){
    			leaveChannelCmd();
    		}else if(text.startsWith("/whoami")){
    			whoamiCmd();
    		}else if(text.startsWith("/who")){
    			listClientsCmd();
    		}else if(text.startsWith("/bye")){
    			simpleClient.logout(false);
    		}else if(text.startsWith("/exit")){
    			System.exit(0);
    		}else{
    			appendError("Unknown command " + text );
    		}
    	}else{
    		super.send(text);
    	}
	}
	
	@Override
	public ClientChannelListener joinedChannel(ClientChannel channel) {
		ClientChannelListener result =  super.joinedChannel(channel);
		if(channel.getName().equals("clients")){
			try{
				simpleClient.send(Serializer.encodeString("/login"));
			}catch(Exception e){
				
			}
		}
		return result;
	}
	/**
	 * Send command <b>/leave</b> to the Server. If the server implementation of
	 * <code>ChannelListener</code> for this channel does not listen commands 
	 * (<i>this</i> <b>/leave</b> command) then nothing will happens.
	 */
	protected void leaveChannelCmd(){
		ClientChannel channel = getSelectedChannel();
		if(channel==null){
			//must select a channel asshole
			appendError("you must select a channel (<DIRECT> is not a channel)");
			super.send("/leave");
		}else{
			try {
				channel.send(Serializer.encodeString("/leave"));
			} catch (IOException e) {
				appendError(e.getMessage());
				e.printStackTrace();
			}
		}		
	}
	protected void listClientsCmd(){
		super.send("/who");
	}
	/**
	 * Send command <b>/leave</b> to the Server. If the server implementation of
	 * <code>ChannelListener</code> for this channel does not listen commands 
	 * (<i>this</i> <b>/leave</b> command) then nothing will happens.
	 */
	protected void whoamiCmd(){
		ClientChannel channel = getSelectedChannel();
		if(channel==null){
			super.send("/whoami");
		}else{
			//solo para molestar al usuario!
			appendError("you must use this command with <DIRECT> option");
		}		
	}
	/**
     * Runs an instance of this client.
     *
     * @param args the command-line arguments (unused)
     */
    public static void main(String[] args) {
        new InfoChannelClient().login();
    }
}
