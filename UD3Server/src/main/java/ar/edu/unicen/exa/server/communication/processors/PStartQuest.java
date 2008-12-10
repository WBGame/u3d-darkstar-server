/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.messages.IMessage;
import common.messages.MsgPlainText;
import common.processors.IProcessor;

/**
 * Este procesador se encarga de aquellos mensajes que tienen por objetivo pedir
 * comenzar una quest.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public class PStartQuest extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PStartQuest() {
		
	}
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PStartQuest();
	}
	
	/**
	 * Toma la petición de comenzar una quest y la procesa. Toma contenido en el
	 * mensaje pasado como paramentro, e invoca el metodo
	 * {@link ModelAccess#startQuest(String, String)}.
	 * 
	 * @param msg Contiene el id de la quest que se quiere iniciar.
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(IMessage msg) {
		MsgPlainText msgStartQuestRequest = (MsgPlainText) msg;
		
		String idPlayer = getPlayerAsociete().get().getIdEntity();
		
		String idQuest = msgStartQuestRequest.getMsg();
		
		ModelAccess.getInstance().startQuest(idPlayer, idQuest);
	}
	
}
