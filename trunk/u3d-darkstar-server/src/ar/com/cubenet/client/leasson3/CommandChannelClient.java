package ar.com.cubenet.client.leasson3;

import java.io.IOException;
import java.nio.ByteBuffer;

import ar.com.cubenet.client.leasson3.listener.HelloChannelListener;
import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;

/**
 * Esta clase es similar a {@link SimpleChannelClient} con la
 * diferencia que implementa un simple sistema de comandos
 * que la instanciación del servidor 
 * {@link ar.com.cubenet.server.leasson3.ServerChannels} 
 * ( o mejor aún 
 * {@link ar.com.cubenet.server.leasson4.ServerChannels} )
 * sabrá interpretar.
 * 
 * @author Sebastián Perruolo
 *
 */
public class CommandChannelClient extends AbstractChannelChatClient {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -6208093334168870447L;
	/**
	 * Creates a new client UI.
	 */
	public CommandChannelClient() {
		super(CommandChannelClient.class.getSimpleName());
	}

	/** 
	 * Ejecuta el login y luego imprime un mensaje de
	 * ayuda para manejar los comandos que esta clase
	 * implementa.
	 * @see #login()
	 */
	private void loginAndHelp() {
		super.login();
		appendOutput("Type /help for help menu");
	}

	/**
	 * Evalúa si el texto a enviar es un comando. En caso
	 * de que no lo sea, simplemente envía el mensaje por
	 * el channel seleccionado. Si el texto es un comando
	 * realiza la acción necesaria.
	 * 
	 * @param text Comando a ejecutar o texto a enviar.
	 */
	protected final void send(final String text) {
		//Se sobreescribe este método para aceptar comandos que comiencen con /
		if (text.startsWith("/")) {
			if (text.startsWith("/help")) {
				appendOutput("Available commands:");
				appendOutput("\t /leave\t->\tleave the selected channel");
				appendOutput("\t /whoami\t->\t"
						+ "print the name of the current client");
				appendOutput("\t /who\t->\tlist clients");
				appendOutput("\t /exit\t->\tclose this client");
				appendOutput("\t /bye\t->\tdisconnect");
			} else 
				if (text.startsWith("/leave")) {
					leaveChannelCmd();
				} else 
					if (text.startsWith("/whoami")) {
						whoamiCmd();
					} else if (text.startsWith("/who")) {
						listClientsCmd();
					} else if (text.startsWith("/bye")) {
						getSimpleClient().logout(false);
					} else if (text.startsWith("/exit")) {
						System.exit(0);
					} else {
						appendError("Unknown command " + text);
					}
		} else {
			simpleSend(text);
		}
	}
	
	/**
	 * Envía el texto por el channel seleccionado, si
	 * el usuario seleccionó &lt;DIRECT> se envía
	 * el mensaje directamente al server.
	 * 
	 * @param text Texto a enviar
	 */
	protected final void simpleSend(final String text) {
		ClientChannel channel = getSelectedChannel();
		ByteBuffer message = Serializer.encodeString(text);
		
		try {
			if (channel == null) {
				getSimpleClient().send(message);
			} else {
				channel.send(Serializer.encodeString(text));
			}
		} catch (Exception e) {
			appendError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Este método se ejecuta cuando el cliente se une
	 * a un channel nuevo.
	 * @param channel Channel al que se acaba de unir el cliente.
	 * @return un listener del channel.
	 */
	public final ClientChannelListener joinedChannel(
			final ClientChannel channel) {
		ClientChannelListener result =  simpleJoinedChannel(channel);
		if (channel.getName().equals("clients")) {
			try {
				getSimpleClient().send(Serializer.encodeString("/login"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns a listener that formats and displays received channel
	 * messages in the output text pane.
	 */
	public final ClientChannelListener simpleJoinedChannel(
			final ClientChannel channel) {
		String channelName = channel.getName();
		getChannelsByName().put(channelName, channel);
		appendOutput("Joined to channel " + channelName);
		getChannelSelectorModel().addElement(channelName);
		return new HelloChannelListener(this);
	}
	
	/**
	 * Send command <b>/leave</b> to the Server. If the server implementation of
	 * <code>ChannelListener</code> for this channel does not listen commands 
	 * (<i>this</i> <b>/leave</b> command) then nothing will happens.
	 */
	protected final void leaveChannelCmd() {
		ClientChannel channel = getSelectedChannel();
		if (channel == null) {
			//must select a channel asshole
			appendError(
				"you must select a channel (<DIRECT> is not a channel)"
			);
			simpleSend("/leave");
		} else {
			try {
				channel.send(Serializer.encodeString("/leave"));
			} catch (IOException e) {
				appendError(e.getMessage());
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 
	 */
	protected final void listClientsCmd() {
		simpleSend("/who");
	}
	/**
	 * Send command <b>/leave</b> to the Server. If the server implementation of
	 * <code>ChannelListener</code> for this channel does not listen commands 
	 * (<i>this</i> <b>/leave</b> command) then nothing will happens.
	 */
	protected final void whoamiCmd() {
		ClientChannel channel = getSelectedChannel();
		if (channel == null) {
			simpleSend("/whoami");
		} else {
			//solo para molestar al usuario!
			appendError("you must use this command with <DIRECT> option");
		}		
	}
	/**
	 * Runs an instance of this client.
	 *
	 * @param args the command-line arguments (unused)
	 */
	public static void main(final String[] args) {
		new CommandChannelClient().loginAndHelp();
	}
}
