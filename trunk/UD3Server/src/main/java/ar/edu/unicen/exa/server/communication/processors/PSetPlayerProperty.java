/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.player.Player;

import common.datatypes.IPlayerProperty;
import common.messages.IMessage;
import common.messages.requests.MsgSetPlayerProperty;
import common.processors.IProcessor;

// TODO: Auto-generated Javadoc
/**
 * Este procesador es el encargado de realizar las acciones correspondientes a
 * mensajes de tipo {@link MsgSetPlayerProperty}.
 * 
 * @author lito
 * @see #process(IMessage)
 */
public class PSetPlayerProperty extends ServerMsgProcessor {
	
	/**
	 * Construcotr por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PSetPlayerProperty() {
		
	}
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	@Override
	public final IProcessor factoryMethod() {
		return new PSetPlayerProperty();
	}
	
	/**
	 * Obtiene la propiedad a setear desde el mensaje, y modifica la entidad
	 * {@link Player} correspondiente que se tenga referenciada desde este
	 * procesador.<BR/>
	 * <BR/>
	 * NOTA: Todas las propiedades que se desen setear mediante este procesador,
	 * deben ser propiedades exlusiva del Obejto Player, no deben ser ninguna
	 * que sea del {@link ModelAccess}.
	 * 
	 * @param msg Un mensaje que sera casteado a {@link MsgSetPlayerProperty}
	 * para poder obtener le identificador de la propiedad y el valor a
	 * setear.
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	@Override
	public final void process(final IMessage msg) {
		try {
			MsgSetPlayerProperty msgSPP = (MsgSetPlayerProperty) msg;
			
			Player player = getPlayerAsociete(); 
			
			IPlayerProperty prop = msgSPP.getPlaerProperty();
			// tirara la excepcion al querer seterale el valor a una propiedad
			// que no posea el player.
			player.getProperty(prop.getId()).setValue(prop.getValue());
		} catch (NullPointerException e) {
			// Si la propiedad no existe en el player, no se hace nada.
			System.out.println("");
		}
	}
	
}
