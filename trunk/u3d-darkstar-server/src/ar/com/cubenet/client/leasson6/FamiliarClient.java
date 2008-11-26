package ar.com.cubenet.client.leasson6;

import java.nio.ByteBuffer;

import ar.com.cubenet.client.leasson3.AbstractChannelChatClient;
import ar.com.cubenet.client.leasson3.listener.HelloChannelListener;
import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;

/**
 * This client just show a message info about the leasson6
 * server's implementation.
 * @author Sebastián Perruolo
 *
 */
public class FamiliarClient extends AbstractChannelChatClient {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -8764914447253980626L;

	/** 
	 * Ejecuta el login y luego imprime un mensaje de
	 * ayuda para manejar los comandos que esta clase
	 * implementa.
	 * @see #login()
	 */
	private void loginAndHelp() {
		super.login();
		appendOutput(getHelpInfo());
	}
	
	/**
	 * Returns usage help text.
	 * @return usage help text.
	 */
	private String getHelpInfo() {
		StringBuffer sb = new StringBuffer("\nAbout this client:\n");
		sb.append("Just send a message and the server will echo\n");
		sb.append("that message several times using a complex task\n");
		sb.append("to create child task which echo the same message\n");
		sb.append("you have sent.\n");
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns a listener that formats and displays received channel
	 * messages in the output text pane.
	 */
	public final ClientChannelListener joinedChannel(
			final ClientChannel channel) {
		String channelName = channel.getName();
		getChannelsByName().put(channelName, channel);
		appendOutput("Joined to channel " + channelName);
		getChannelSelectorModel().addElement(channelName);
		return new HelloChannelListener(this);
	}

	/**
	 * Envía el texto por el channel seleccionado, si
	 * el usuario seleccionó &lt;DIRECT> se envía
	 * el mensaje directamente al server.
	 * @param text Texto a enviar
	 */
	protected final void send(final String text) {
		ClientChannel channel = getSelectedChannel();
		ByteBuffer message = Serializer.encodeString(text);
		
		try {
			if (channel == null) {
				appendOutput("Sending DIRECT message");
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
	 * Runs an instance of this client.
	 *
	 * @param args the command-line arguments (unused)
	 */
	public static void main(final String[] args) {
		new FamiliarClient().loginAndHelp();
	}
}
