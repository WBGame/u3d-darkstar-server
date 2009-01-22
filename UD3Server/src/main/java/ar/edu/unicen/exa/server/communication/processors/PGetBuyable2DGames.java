package ar.edu.unicen.exa.server.communication.processors;

import java.util.List;
import java.util.Vector;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetBuyable2DGamesResponse;
import common.processors.IProcessor;

/**
 * Este procesador es el encargado de realizar las acciones correspondientes a
 * mensajes que solicitan la lista de juegos 2D que se pueden comprar.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public final class PGetBuyable2DGames extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PGetBuyable2DGames() { }
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PGetBuyable2DGames();
	}
	
	/**
	 * Se llama al método {@link ModelAccess#getBuyables2DGames()} para obtener
	 * la lista de juegos que pueden tienen un costo asociado. Construye un
	 * mensaje {@link MsgGetBuyable2DGamesResponse} y lo envia al player seteado
	 * en esta instancia de procesador.
	 * 
	 * @param msg the msg
	 * 
	 * @author Polo
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(final IMessage msg) {
		try {
			List<String> buyable2DGames = new Vector<String>(ModelAccess
					.getInstance().getBuyables2DGames());
			
			MsgGetBuyable2DGamesResponse msgGB2DGRs = 
				(MsgGetBuyable2DGamesResponse) MessageFactory
					.getInstance().createMessage(
							MsgTypes.MSG_GET_BUYABLE_2DGAMES_RESPONSE_TYPE);
			
			msgGB2DGRs.setIds2DGame(buyable2DGames);
			
			// Envio el mensaje con la respuesta al Player que la solicitó
			getPlayerAssociated().send(msgGB2DGRs);
		} catch (UnsopportedMessageException e) {
			// Esta excepcion no tendria porque ocurrir nunca.
			e.printStackTrace();
		}
	}
	
}
