/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import java.util.List;
import java.util.Vector;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetAvailable2DGamesResponse;
import common.processors.IProcessor;

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
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PGetAvailable2DGames();
	}
	
	/**
	 * Se llama al método {@link ModelAccess#getAvailableGames(String)} para
	 * obtener la lista de juegos que disponibles para el player en cuestión.
	 * Construye un mensaje {@link MsgGetAvailable2DGamesResponse} y lo envia al
	 * player seteado en esta instancia de procesador.
	 * 
	 * @param msg
	 * 
	 * @author Polo
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(IMessage msg) {
		try {
			String idPlayer = playerAsociete.get().getIdEntity();
			
			List<String> availableGames = new Vector<String>(ModelAccess
					.getInstance().getAvailableGames(idPlayer));
			
			MsgGetAvailable2DGamesResponse msgA2DGRs = (MsgGetAvailable2DGamesResponse) MessageFactory
					.getInstance().createMessage(
							MsgTypes.MSG_GET_AVAILABLE_2DGAMES_RESPONSE_TYPE);
			
			msgA2DGRs.setIds2DGame(availableGames);

			// Envio el mensaje con la respuesta al Player que la solicitó
			playerAsociete.get().send(msgA2DGRs);
			
		} catch (UnsopportedMessageException e) {
			// Esta excepcion no tendria porque ocurrir nunca.
			e.printStackTrace();
		}
	}
	
}