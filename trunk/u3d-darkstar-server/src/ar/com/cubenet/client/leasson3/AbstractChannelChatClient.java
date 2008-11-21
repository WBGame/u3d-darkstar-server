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
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

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
public abstract class AbstractChannelChatClient extends AbstractChatClient {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	/** Map that associates a channel name with a {@link ClientChannel}. */
	private final Map<String, ClientChannel> channelsByName =
			new HashMap<String, ClientChannel>();

	/** The UI selector among direct messaging and different channels. */
	private JComboBox channelSelector;

	/** The data model for the channel selector. */
	private DefaultComboBoxModel channelSelectorModel;


	// HelloChannelClient methods

	/**
	 * Creates a new client UI.
	 */
	public AbstractChannelChatClient() {
		this(AbstractChannelChatClient.class.getSimpleName());
	}

	/**
	 * Creates a new client UI with the given window title.
	 *
	 * @param title the title for the client's window
	 */
	protected AbstractChannelChatClient(final String title) {
		super(title);
	}
	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation adds a channel selector component next
	 * to the input text field to allow users to choose between
	 * direct-to-server messages and channel broadcasts.
	 */
	public final void populateInputPanel(final JPanel panel) {
		panel.add(getInputField(), BorderLayout.CENTER);

		channelSelectorModel = new DefaultComboBoxModel();
		channelSelectorModel.addElement("<DIRECT>");
		channelSelector = new JComboBox(channelSelectorModel);
		channelSelector.setFocusable(false);
		panel.add(channelSelector, BorderLayout.WEST);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns a listener that formats and displays received channel
	 * messages in the output text pane.
	 */
	public abstract ClientChannelListener joinedChannel(
			final ClientChannel channel);

	/**
	 * Obtiene el channel seleccionado del ComboBox.
	 * @return .
	 */
	protected final ClientChannel getSelectedChannel() {
		String channelName = (String) channelSelector.getSelectedItem();
		ClientChannel channel = channelsByName.get(channelName);
		return channel;
	}

	/**
	 * Envía un String al servidor.
	 * @param text .
	 */
	protected abstract void send(final String text);

	/** 
	 * Retorna Map de Channels donde el key del Map es el
	 * nombre del channel.
	 * 
	 * @return un Map donde el key es el nombre del channel que
	 * 		se encuentra en el value del Map 
	 */
	public final Map<String, ClientChannel> getChannelsByName() {
		return channelsByName;
	}
	
	/** 
	 * Retorna el modelo de datos para el selector de channels.
	 * @return . 
	 */
	public final DefaultComboBoxModel getChannelSelectorModel() {
		return channelSelectorModel;
	}

}
