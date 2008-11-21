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

import java.awt.BorderLayout;
import java.nio.ByteBuffer;

import javax.swing.JPanel;

import ar.com.cubenet.client.leasson3.listener.NullClientChannelListener;
import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;

/**
 * A simple GUI client that interacts with an SGS server-side app.
 * It presents a basic chat interface with an output area and input
 * field.
 * <p>
 * The client understands the following properties:
 * <ul>
 * <li><code>{@value #HOST_PROPERTY}</code> <br>
 *     <i>Default:</i> {@value #DEFAULT_HOST} <br>
 *     The hostname of the server.<p>
 *
 * <li><code>{@value #PORT_PROPERTY}</code> <br>
 *     <i>Default:</i> {@value #DEFAULT_PORT} <br>
 *     The port that the server is listening on.<p>
 *
 * </ul>
 */
public class HelloUserClient extends AbstractChatClient {

	/**
	 * The version of the serialized form of this class.
	 */
	private static final long serialVersionUID = -2757877361587901591L;

	/**
	 * Runs an instance of this client.
	 *
	 * @param args the command-line arguments (unused)
	 */
	public static void main(final String[] args) {
		new HelloUserClient().login();
	}

	/**
	 * Creates a new client UI.
	 */
	public HelloUserClient() {
		super(HelloUserClient.class.getSimpleName());
	}

	/**
	 *  {@inheritDoc}
	 */
	public final ClientChannelListener joinedChannel(
			final ClientChannel channel) {

		return new NullClientChannelListener();
	}
	
	/**
	 * Encodes the given text and sends it to the server.
	 * 
	 * @param text the text to send.
	 */
	protected final void send(final String text) {
		try {
			ByteBuffer message = Serializer.encodeString(text);
			getSimpleClient().send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Allows subclasses to populate the input panel with
	 * additional UI elements.  The base implementation
	 * simply adds the input text field to the center of the panel.
	 *
	 * @param panel the panel to populate
	 */
	public final void populateInputPanel(final JPanel panel) {
		panel.add(getInputField(), BorderLayout.CENTER);
	}
	
}
