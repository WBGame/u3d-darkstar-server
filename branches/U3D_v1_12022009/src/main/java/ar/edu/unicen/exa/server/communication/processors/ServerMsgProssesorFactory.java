package ar.edu.unicen.exa.server.communication.processors;

import common.messages.MsgTypes;
import common.processors.MsgProcessorFactory;

/**
 * Intermediario para acceder al Factory de los procesadores.
 * 
 * @author Sebastian Perruolo &lt;sebastian dot perruolo at gmail dot com&gt;
 * @encoding UTF-8.
 * 
 */
public final class ServerMsgProssesorFactory {
	
	/**
	 * Instancia de objeto a retornar.
	 */
	private static MsgProcessorFactory instance = null;
	
	/** Constructor. */
	private ServerMsgProssesorFactory() { }

	/**
	 * Singleton para obtener la instancia de la clase 
	 * {@link MsgProcessorFactory}.
	 * 
	 * @return Factory para los procesadores.
	 */
	public static MsgProcessorFactory getInstance() {
		if (instance == null) {
			configureMsgProcessorFactory();
			instance = MsgProcessorFactory.getInstance();
		}
		return instance;
	}
	
	/**
	 * Configura el factory de procesadores {@link MsgProcessorFactory}
	 * asignado para cada tipo de mensaje del framework que es recibido en el
	 * servidor, un instancia del procesador correspondiente.
	 */
	private static void configureMsgProcessorFactory() {

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

