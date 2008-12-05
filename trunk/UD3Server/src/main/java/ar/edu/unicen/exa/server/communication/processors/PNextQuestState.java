/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import common.messages.IMessage;
import common.processors.IProcessor;

/**
 * @author lito
 *
 */
public class PNextQuestState extends ServerMsgProcessor {
	
	/**
	 * Construcotr por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PNextQuestState() {
		
	}
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	@Override
	public IProcessor factoryMethod() {
		return new PNextQuestState();
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	@Override
	public void process(IMessage msg) {
		// TODO Auto-generated method stub
		
	}
	
}
