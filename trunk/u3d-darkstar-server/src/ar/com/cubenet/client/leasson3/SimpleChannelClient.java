/*
 * Copyright (c) 2007-2008, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ar.com.cubenet.client.leasson3;

import java.nio.ByteBuffer;

import ar.com.cubenet.client.leasson3.listener.HelloChannelListener;
import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;

/**
 * A simple GUI client that interacts with an SGS server-side app using
 * both direct messaging and channel broadcasts.
 * <p>
 * It presents a basic chat interface with an output area and input
 * field, and adds a channel selector to allow the user to choose which
 * method is used for sending data.
 *
 * @see HelloUserClient for a description of the properties understood
 *      by this client.
 */
public class SimpleChannelClient extends AbstractChannelChatClient {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	// Main

	/**
	 * Runs an instance of this client.
	 *
	 * @param args the command-line arguments (unused)
	 */
	public static void main(final String[] args) {
		new SimpleChannelClient().login();
	}

	// HelloChannelClient methods

	/**
	 * Creates a new client UI.
	 */
	public SimpleChannelClient() {
		this(SimpleChannelClient.class.getSimpleName());
	}

	/**
	 * Creates a new client UI with the given window title.
	 *
	 * @param title the title for the client's window
	 */
	protected SimpleChannelClient(final String title) {
		super(title);
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
				getSimpleClient().send(message);
			} else {
				channel.send(Serializer.encodeString(text));
			}
		} catch (Exception e) {
			appendError(e.getMessage());
			e.printStackTrace();
		}
	}

}
