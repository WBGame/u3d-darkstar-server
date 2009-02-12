/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.messages.IMessage;
import common.messages.MsgPlainText;
import common.processors.IProcessor;

// TODO: Auto-generated Javadoc
/**
 * Este procesador se encarga de aquellos mensajes que tienen por objetivo pedir
 * avanzar al siguiente estado en una quest.
 * 
 * @author Polo
 * @see #process(IMessage)
 */
public final class PNextQuestState extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PNextQuestState() {
		
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
		return new PNextQuestState();
	}
	
	/**
	 * Toma la petición de pasar al siguiente estado en una quest y la procesa.
	 * Toma contenido en el mensaje pasado como paramentro, e invoca el metodo
	 * {@link ModelAccess#nextQuestState(String, String, String)}.
	 * 
	 * @param msg Contiene el id de la quest en cuestión.
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(final IMessage msg) {
		MsgPlainText msgNextQuestState = (MsgPlainText) msg;
		
		String idPlayer = getPlayerAssociated().getIdEntity();
		
		String idQuest = msgNextQuestState.getMsg();
		// El model access se encargara de saber cual es el siguiente estado, al
		// pasarle null como parametro
		ModelAccess.getInstance().nextQuestState(idPlayer, idQuest, null);
	}
	
}
