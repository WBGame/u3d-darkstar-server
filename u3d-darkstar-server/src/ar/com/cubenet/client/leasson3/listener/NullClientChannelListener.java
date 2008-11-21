package ar.com.cubenet.client.leasson3.listener;

import java.nio.ByteBuffer;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;

/**
 *  A ClientChannelListener that does nothing at all (this basic
 *  client does not support channels).
 */
public class NullClientChannelListener implements ClientChannelListener {

	/** {@inheritDoc} */
	public final void leftChannel(final ClientChannel channel) { 
		System.out.println("Unexepected call to leftChannel");
	}
	/** {@inheritDoc} */

	public final void receivedMessage(final ClientChannel channel, 
			final ByteBuffer message) {
		System.out.println("Unexepected call to receivedMessage");
	}

}
