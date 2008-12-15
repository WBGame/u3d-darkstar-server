/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;

import common.messages.MsgTypes;
import common.processors.IProcessor;
import common.processors.MsgProcessorFactory;

// TODO: Auto-generated Javadoc
/**
 * La clase representa la base de la jerarquia de procesadores de mensajes en el
 * servidor Darkstar.<BR/>
 * Abstrae el comportamiento de cualquier procesador utilizado en el servidor,
 * al definir una {@link ManagedReference} (a un {@link Player}) y una
 * {@link ManagedReference} (a una {@link Cell}) que representan las
 * relacionadas a la recepsion y/o envio de los mensajes a procesar.
 * 
 * @author lito
 */
public abstract class ServerMsgProcessor implements IProcessor {

	/** El tipo de mensaje que se procesaran por la instancia del procesador. */
	private String						msgType;

	/** Referencia al player relacionado al mensaje a procesar. */
	private  ManagedReference<Player>	playerAsociete;

	/** Referencia a la celda relacionada al mensaje a procesar. */
	private  ManagedReference<Cell>	cellAsociete;

	/**
	 * Gets the msg type.
	 * 
	 * @return El tipo de mensaje que se procesaran por la instancia del
	 * procesador.
	 */
	public final String getMsgType() {
		return msgType;
	}

	/**
	 * Sets the msg type.
	 * 
	 * @param msgType El tipo de mensaje que se procesaran por la instancia del
	 * procesador.
	 */
	@Override
	public final void setMsgType(final String msgType) {
		this.msgType = msgType;
	}

	/**
	 * Gets the player asociete.
	 * 
	 * @return associated player
	 */
	public final Player getPlayerAsociete() {
		return playerAsociete.get();
	}

	/**
	 * Sets the player asociete.
	 * 
	 * @param associeted player relacionado al mensaje a procesar.
	 */
	public final void setPlayerAsociete(final Player associeted) {
		this.playerAsociete = AppContext.getDataManager().createReference(
				associeted
		);
	}

	/**
	 * Gets the cell asociete.
	 * 
	 * @return Referencia a la celda relacionada al mensaje a procesar.
	 */
	public final Cell getCellAsociete() {
		return cellAsociete.get();
	}

	/**
	 * Sets the cell asociete.
	 * 
	 * @param associeted celda relacionada al mensaje a procesar.
	 */
	public final void setCellAsociete(final Cell associeted) {
		this.cellAsociete = AppContext.getDataManager().createReference(
				associeted
		);
	}

	/**
	 * Con figura la fabrica de procesadores {@link MsgProcessorFactory}
	 * asignado para cada tipo de mensaje del framework que es recibido en el
	 * servidor, un instancia del procesador correspondiente.
	 */
	public static void configureMsgProcessorFactory() {

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_ABORT_QUEST_TYPE, new PAbortQuest());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_ADD_2D_GAME_SCORE_TYPE, new PAdd2DGameScore());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_BUY_GAME_TYPE, new PBuyGame());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_FINISH_QUEST_TYPE, new PFinishQuest());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_GET_2DGAME_PRICE_TYPE, new PGet2DGamePrice());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_GET_AVAILABLE_2DGAMES_TYPE,
				new PGetAvailable2DGames());

		MsgProcessorFactory.getInstance()
		.addProcessor(MsgTypes.MSG_GET_BUYABLE_2DGAMES_TYPE,
				new PGetBuyable2DGames());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_GET_PLAYER_TYPE, new PGetPlayer());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_GET_RANKING_TYPE, new PGetRanking());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_GET_TIMES_PLAYED_TYPE, new PGetTimesPlayed());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_START_QUEST_TYPE, new PStartQuest());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_GET_DYNAMIC_ENTITY_TYPE, new PTaskDelegator());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_ENTER_WORLD_TYPE, new PTaskDelegator());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_MOVE_SEND_TYPE, new PTaskDelegator());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_ROTATE_SEND_TYPE, new PTaskDelegator());

		MsgProcessorFactory.getInstance().addProcessor(
				MsgTypes.MSG_CHANGE_PLAYER_STATE_SEND_TYPE,
				new PTaskDelegator());

	}

}
