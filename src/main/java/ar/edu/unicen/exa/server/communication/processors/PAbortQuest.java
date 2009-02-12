package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.messages.IMessage;
import common.messages.MsgPlainText;
import common.processors.IProcessor;

/**
 * Este procesador se encarga de aquellos mensajes que tienen por objetivo
 * pedir abandonar una quest.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public final class PAbortQuest extends ServerMsgProcessor {
	
	/**
	 * Construcotr por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PAbortQuest() { }
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PAbortQuest();
	}
	
	/**
	 * Toma la petición de abandonar una quest y la procesa. Toma
	 * contenido en el mensaje pasado como paramentro, e invoca el metodo
	 * {@link ModelAccess#abortQuest(String, String)}.
	 * 
	 * @param msg Contiene el id de la quest que se abandona.
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(final IMessage msg) {
		MsgPlainText msgAbortQuestRequest = (MsgPlainText) msg;

		String idPlayer = getPlayerAssociated().getIdEntity();

		String idQuest = msgAbortQuestRequest.getMsg();

		ModelAccess.getInstance().abortQuest(idPlayer, idQuest);
	}
}
