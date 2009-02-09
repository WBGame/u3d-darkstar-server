package ar.edu.unicen.exa.server.communication.processors;

import com.jme.math.Vector3f;
import com.sun.sgs.app.ClientSession;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.MsgPlainText;
import common.processors.IProcessor;

/**
 * Este procesador se ejecutará al recibir un mensaje directo desde un cliente,
 * el cual informa que entrará al mundo indicado en el mensaje. Las 
 * acciones a tomar son las siguientes:
 * <ol>
 * 	<li>Agregar el jugador al mundo deseado.</li>
 * 	<li>Suscribirlo a la celda por defecto del mundo y a las adyacentes según
 * corresponda.</li>
 * 	<li>Enviar los mensajes correspondientes {@link MsgArrived} a las celdas 
 * del mundo.</li>
 * </ol>
 * 
 */
public final class PEnterWorld extends ServerMsgProcessor {

	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PEnterWorld() { }
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PEnterWorld();
	}

	/**
	 * Este procesador contiene la funcionalidad asociada al ingreso de un 
	 * jugador a un mundo. Se obtiene el mundo por medio del mensaje recivido 
	 * el mundo al que desea ingresar, actualizando el mundo del jugador como
	 * asi tambien la celda y posicion inicial, suscribirlo al canal y 
	 * finalmente enviar el mensaje {@link MsgArrived} a las celdas 
	 * correspondientes. 
	 *  
	 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
	 * 
	 * @param msg mensaje a procesar
	 * 
	 * TODO verificar correcta implementación y acomodar JAVADOC en caso de 
	 *      aprovación del cambio.
	 */
	public void process(final IMessage msg) {
		// instancia del jugador
		Player player = getPlayerAssociated();
		// obtener la sesion del jugador
		ClientSession session = player.getSession();		
		
		if (session == null) {
			throw new NullPointerException(
					"Player inexistente. Accion Abortada" 
			);
		}		
		
		// está suscripto a algún mundo => necesito salir del mismo. 
		if (player.getActualWorld() != null) {
			// recuperar la celda actual
			Cell actualCell = getCellAssociated();
			
			// crear el mensaje de partida del jugador
			IMessage msgLeft = null;
			try {
				msgLeft = MessageFactory.getInstance().createMessage(
						MsgTypes.MSG_LEFT_TYPE);
				// seteo el id del jugador como mensaje del mismo
				((MsgPlainText) msgLeft).setMsg(player.getIdEntity());
			} catch (UnsopportedMessageException e1) {
				e1.printStackTrace();
			}
			
			// notificar a la celda actual que la entidad dinamica ha salido
			actualCell.send(msgLeft, session);

			// desuscribir al jugador de la celda del viejo mundo
			actualCell.leaveFromChannel(session);			
		}
		
		// A partir de aca el player no pertenece a ningún mundo.
		
		// casting al tipo de mensaje correspondiente
		MsgPlainText imsg = (MsgPlainText) msg;
		// recuperar el id del nuevo mundo desde el mensaje recibido
		String newWorldID = imsg.getMsg();
		
		//TODO FIX si falla algo de lo de mas abajo esto queda inconsisten no?
		//     hay que buscar metodos de rollback de las acciones para asegurar
		//     el correcto estado del player.
		
		// actualizar el jugador con el id del nuevo mundo
		player.setActualWorld(newWorldID);
		
		//TODO FIX este angulo tiene que ser definido con aprobación de la gente
		// del cliente.
		player.setAngle(new Vector3f(1, 1, 1));

		// obtener la estructura del nuevo mundo
		IGridStructure world = GridManager.getInstance()
				.getStructure(newWorldID);
		
		// establecer la posicion inicial del jugador dentro del mundo
		player.setPosition(world.getSpawnPosition());
		
		// obtener la celda por defecto a partir del nuevo mundo
		Cell cell = world.getSpawnCell();
		
		// suscribir al jugador a la nueva celda
		cell.joinToChannel(session);
		
		// crear el mensaje de llegada del jugador al nuevo mundo
		IMessage msgArrived = null;
		try {
			msgArrived = MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_ARRIVED_TYPE);
			//seteo el mensaje con el id del jugador
			((MsgPlainText) msgArrived).setMsg(player.getIdEntity());			
		} catch (UnsopportedMessageException e) {
			e.printStackTrace();
		}
		// notificar a la celda por defecto que ingresó el jugador
		cell.send(msgArrived, session);
		
		// obtener los adyacentes de la nueva celda
		Cell[] adyacentes = world.getAdjacents(cell, player.getPosition());
		
		// notificar a las celdas adyacentes que ingresó el jugador
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msgArrived, session);
			}
		}
	}
}
