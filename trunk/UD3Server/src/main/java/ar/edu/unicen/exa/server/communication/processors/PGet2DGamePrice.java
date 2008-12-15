/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.responses.MsgGet2DGamePriceResponse;
import common.processors.IProcessor;


/**
 * Este procesador es el encargado de realizar las acciones correspondientes a
 * mensajes que solicitan la devolución de los datos correspondientes al precio
 * de un juego 2D.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public class PGet2DGamePrice extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PGet2DGamePrice() {
		
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
		return new PGet2DGamePrice();
	}
	
	/**
	 * Castea el mensaje recibido a {@link MsgPlainText}, y obtiene de este el
	 * identificador del minijuego para el que se pretende obtener el precio,
	 * Construye un mensaje {@link MsgGet2DGamePriceResponse} y lo envia al
	 * player seteado en esta instancia de procesador.
	 * 
	 * @param msg Es casteado a {@link MsgPlainText} y se interpreta que el
	 * texto representa el identificador del minijuego del cual se quiere
	 * retornar su precio.
	 * 
	 * @author Polo
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public final void process(final IMessage msg) {
		try {
			// Este debería ser el id del juego del cual se debe obtener el
			// precio
			MsgPlainText msgGamePrice = (MsgPlainText) msg;
			
			String id2DGame = msgGamePrice.getMsg();
			// obtener el precio del juego
			float price = ModelAccess.getInstance().get2DGamePrice(id2DGame);
			
			// Creo el mensaje a devolver.
			MsgGet2DGamePriceResponse msgG2DGPR = 
						(MsgGet2DGamePriceResponse) MessageFactory
						.getInstance().createMessage(
						MsgTypes.MSG_GET_2DGAMES_PRICE_RESPONSE_TYPE);
			
			msgG2DGPR.setPrice(price);
			msgG2DGPR.setId2DGame(id2DGame);
			
			// Envio el mensaje con la respuesta al Player que solicito
			playerAsociete.get().send(msgG2DGPR);
			
		} catch (UnsopportedMessageException e) {
			// Esta excepcion no tendria porque ocurrir nunca.
			e.printStackTrace();
		}
		
	}
	
}
