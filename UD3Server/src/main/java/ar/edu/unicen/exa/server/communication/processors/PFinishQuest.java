package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.messages.IMessage;
import common.messages.MsgPlainText;
import common.processors.IProcessor;

/**
 * Este procesador se encarga de aquellos mensajes que tienen por objetivo pedir
 * finalizar una quest.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public final class PFinishQuest extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PFinishQuest() { }
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PFinishQuest();
	}
	
	/**
	 * Toma la petición de finalizar una quest y la procesa. Toma contenido en
	 * el mensaje pasado como paramentro, e invoca el metodo
	 * {@link ModelAccess#finishQuest(String, String)}.
	 * 
	 * @param msg Contiene el id de la quest que se finaliza.
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(final IMessage msg) {
		MsgPlainText msgFinishQuestReq = (MsgPlainText) msg;
		
		String idQuest = msgFinishQuestReq.getMsg();
		
		String idPlayer = getPlayerAsociete().getIdEntity();
		
		ModelAccess.getInstance().finishQuest(idPlayer, idQuest);
	}	
}
