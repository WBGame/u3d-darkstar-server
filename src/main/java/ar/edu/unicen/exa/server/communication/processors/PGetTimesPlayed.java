/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetTimesPlayedResponse;
import common.processors.IProcessor;

// TODO: Auto-generated Javadoc
/**
 * Este procesador es el encargado de realizar las acciones correspondientes a
 * mensajes que solicitan el número de veces que se ha jugado a un juego 2D.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public final class PGetTimesPlayed extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PGetTimesPlayed() {
		
	}
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PGetTimesPlayed();
	}
	
	/**
	 * Castea el mensaje recibido a {@link MsgPlainText}, y obtiene de este el
	 * identificador del minijuego para el que se pretende obtener la cantidad
	 * de veces que ha sido jugado por el player que lo solicita, llamando al
	 * método {@link ModelAccess#getPlayedTimes(String, String)}. Construye un
	 * mensaje {@link MsgGetTimesPlayedResponse} y lo envia al player seteado en
	 * esta instancia de procesador.
	 * 
	 * @param msg Es casteado a {@link MsgPlainText} y se interpreta que el
	 * texto representa el identificador del minijuego del cual se quiere
	 * saber cuantas veces ha sido jugado.
	 * 
	 * @author Polo
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(final IMessage msg) {
		try {
			MsgPlainText msgTimePlayedRq = (MsgPlainText) msg;
			
			String id2DGame = msgTimePlayedRq.getMsg();
			Player p = getPlayerAssociated();
			
			int timesPlayed = ModelAccess.getInstance().getPlayedTimes(
					id2DGame, p.getIdEntity());
			
			MsgGetTimesPlayedResponse msgGTPR = 
				(MsgGetTimesPlayedResponse) MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_GET_TIMES_PLAYED_RESPONSE_TYPE);
			
			msgGTPR.setTimesPlayed(timesPlayed);
			msgGTPR.setId2DGame(id2DGame);
			
			// Envio el mensaje con la respuesta al Player que la solicitó
			p.send(msgGTPR);
		} catch (UnsopportedMessageException e) {
			// Esta excepcion no tendria porque ocurrir nunca.
			e.printStackTrace();
		}
	}
	
}
