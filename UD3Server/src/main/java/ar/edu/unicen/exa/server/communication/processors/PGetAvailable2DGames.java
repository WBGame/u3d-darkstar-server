/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import java.util.List;
import java.util.Vector;

import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetAvailable2DGamesResponse;
import common.processors.IProcessor;

// TODO: Auto-generated Javadoc
/**
 * Este procesador es el encargado de realizar las acciones correspondientes a
 * mensajes que solicitan la lista de juegos 2D que están disponibles para el
 * player seteado en esta instancia de procesador.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public class PGetAvailable2DGames extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PGetAvailable2DGames() {
		
	}
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public final IProcessor factoryMethod() {
		return new PGetAvailable2DGames();
	}
	
	/**
	 * Se llama al método {@link ModelAccess#getAvailableGames(String)} para
	 * obtener la lista de juegos que disponibles para el player en cuestión.
	 * Construye un mensaje {@link MsgGetAvailable2DGamesResponse} y lo envia al
	 * player seteado en esta instancia de procesador.
	 * 
	 * @param msg the msg
	 * 
	 * @author Polo
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public final void process(final IMessage msg) {
		try {
			Player p = getPlayerAsociete();
			
			List<String> availableGames = new Vector<String>(ModelAccess
					.getInstance().getAvailableGames(p.getIdEntity()));
			
			MsgGetAvailable2DGamesResponse msgA2DGRs = 
				(MsgGetAvailable2DGamesResponse) MessageFactory
					.getInstance().createMessage(
							MsgTypes.MSG_GET_AVAILABLE_2DGAMES_RESPONSE_TYPE);
			
			msgA2DGRs.setIds2DGame(availableGames);

			// Envio el mensaje con la respuesta al Player que la solicitó
			p.send(msgA2DGRs);
		} catch (UnsopportedMessageException e) {
			// Esta excepcion no tendria porque ocurrir nunca.
			e.printStackTrace();
		}
	}
	
}
