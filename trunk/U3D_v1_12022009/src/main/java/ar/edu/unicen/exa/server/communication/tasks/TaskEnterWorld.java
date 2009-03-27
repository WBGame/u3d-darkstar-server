package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.exception.U3DMustRetryTask;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;

import com.jme.math.Vector3f;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;

/**
 * Task responsible of handle the process of subscribe a {@link Player} into
 * the given {@link IGridStructure} at a specific position.
 * 
 * @author Cabrera Emilio Facundo &lt;cabrerafacundo at gmail dot com&gt;
 * @encoding UTF-8
 * 
 * @see TEnterWorld
 * 
 * TODO use it in place of method {@link AppListenerImpl#enterWorld(Player)}.
 * TODO need testing.
 * TODO add more exception treatment.
 * TODO develop task leave world.
 */
public final class TaskEnterWorld extends TaskCommunication {
	/** 
	 * Default serialVersionUID. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            Message associated with enter world task
	 */
	public TaskEnterWorld(final IMessage msg) {
		super(msg);
	}

	/**
	 * Factory method for abstract creation of task.
	 * 
	 * @param msg
	 *            Message associated with enter world task
	 * 
	 * @return Task Abstraction.
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TaskEnterWorld(msg);
	}

	/**
	 * Enter world logic.
	 * 
	 * @throws Exception some unexpected behavior notification.
	 */
	@Override
	public void run() throws Exception {
		// Create message arrived for notify players that it new player is 
		// entering in this world. 
		IMessage msgArrived = null;
		try {
			msgArrived = MessageFactory.getInstance()
			.createMessage(MsgTypes.MSG_ARRIVED_TYPE);
			((MsgPlainText) msgArrived).setMsg(getPlayerAssociated()
					.getIdEntity());
		} catch (UnsopportedMessageException e) {
			throw new U3DMustRetryTask();
		}		
		// retrieve default structure
		IGridStructure defaultWorld = GridManager.getInstance()
		.getDefaultStructure();
		// update user world 
		getPlayerAssociated().setActualWorld(defaultWorld.getIdWorld());
		// update user angle
		getPlayerAssociated().setAngle(new Vector3f(1, 1, 1));
		// update user position
		getPlayerAssociated().setPosition(defaultWorld.getSpawnPosition());
		// retrieve default cell
		Cell cell = defaultWorld.getSpawnCell();
		// notify enter world in default cell.
		cell.send(msgArrived, null);
		// get adjacent cell
		Cell[] adyacentes = defaultWorld
		.getAdjacents(cell, getPlayerAssociated().getPosition());
		// notify enter world in adjacent cells
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msgArrived, null);
			}
		}
		// subscribe the new player into cell channel 
		cell.joinToChannel(getPlayerAssociated().getSession());
	}

}
