/**
 * 
 */
package server.communication.processors;

import server.serverLogic.ModelAccess;

import common.datatypes.Ranking;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetRankingResponse;
import common.processors.IProcessor;

/**
 * Este procesador es el encargado de realizar las acciones correspondientes a
 * mensajes que solicitan la devolución de los datos correspondientes a un
 * ranking de un juego 2D.
 * 
 * @author fede
 * @see #process(IMessage)
 */
public class PGetRanking extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PGetRanking() {
		
	}
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	@Override
	public IProcessor factoryMethod() {
		return new PGetRanking();
	}
	
	/**
	 * Castea el mensaje recibido a {@link MsgPlainText}, y obtiene de este el
	 * identificador del minijuego para el que se pretende obtener el ranking,
	 * Posteriormente solicita mediane el metodo
	 * {@link ModelAccess#getRanking(String)} el ranking a devolver, construye
	 * un mensaje {@link MsgGetRankingResponse} y lo envia al player seteado en
	 * esta instancia de procesador.
	 * 
	 * @param msg Es casteado a {@link MsgPlainText} y se interpreta que el
	 *        texto representa el identificador del minijuego del cual se quiere
	 *        retornar su ranking.
	 * 
	 * @author fede
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	@Override
	public void process(IMessage msg) {
		try {
			// Puede arrojar UnsopportedMessageException
			MsgGetRankingResponse msgGRR = (MsgGetRankingResponse) MessageFactory
					.getInstance().createMessage(
							MsgTypes.MSG_GET_RANKING_RESPONSE_TYPE);
			
			// El mensaje de solicitud de ranking, para obtener el id del
			// minijuego para el que se quiere el ranking.
			MsgPlainText msgRankingRequest = (MsgPlainText) msg;
			
			Ranking rankingToReturn = ModelAccess.getInstance().getRanking(
					msgRankingRequest.getMsg());
			
			msgGRR.setRanking(rankingToReturn);
			
			// Envio el mensaje con la respuesta. al Player que solicito
			this.playerAsociete.get().send(msgGRR);
			
		} catch (UnsopportedMessageException e) {
			// Esta excepcion no tendria porque ocurrir nunca.
			e.printStackTrace();
		}
		
	}
	
}
