package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.player.Player;
import common.processors.IProcessor;

/**
 * La clase representa la base de la jerarquia de procesadores de mensajes en el
 * servidor Darkstar.<BR/>
 * Abstrae el comportamiento de cualquier procesador utilizado en el servidor,
 * al definir un {@linkDynamicEntity} y una {@link Cell}) que representan las
 * relacionadas a la recepsion y/o envio de los mensajes a procesar.
 * 
 * @author lito
 * @encoding UTF-8.
 */

public abstract class ServerMsgProcessor implements IProcessor {

	/** El tipo de mensaje que se procesaran por la instancia del procesador. */
	private String msgType;

	//XXX las referencias no son necesarias ya que los procesadores no 
	//implementan Serializable ni MnanagedObject

	/** Entidad dinamica relacionada al mensaje a procesar. */
	private Player playerAssociated = null;

	/** Celda relacionada al mensaje a procesar. */
	private Cell cellAssociated;

	/**
	 * Obtiene el tipo de mensaje.
	 * 
	 * @return El tipo de mensaje que se procesaran por la instancia del
	 * procesador.
	 */
	public final String getMsgType() {
		return msgType;
	}

	/**
	 * Establece el tipo de mensaje.
	 * 
	 * @param type tipo de mensaje que se procesará por la instancia del
	 * procesador.
	 */
	@Override
	public final void setMsgType(final String type) {
		this.msgType = type;
	}

	/**
	 * Devuelve el {@link Player} asociado.
	 * 
	 * @return associated {@link Player}.
	 */
	public final Player getPlayerAssociated() {
		return playerAssociated;
	}

	/**
	 * Setea el {@link player} asociado.
	 * 
	 * @param associated {@link player} relacionado al mensaje a procesar.
	 */
	public final void setPlayerAssociated(final Player associated) {
		this.playerAssociated = associated;
	}

	/**
	 * Obtiene la celda({@Cell}) asociada.
	 * 
	 * @return Referencia a la celda relacionada al mensaje a procesar.
	 */
	public final Cell getCellAssociated() {
		return cellAssociated;
	}

	/**
	 * Establese la celda({@Cell}) asociada.
	 * 
	 * @param associated celda relacionada al mensaje a procesar.
	 */
	public final void setCellAssociated(final Cell associated) {
		cellAssociated = associated;
	}
}
