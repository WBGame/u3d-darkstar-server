/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import java.util.HashMap;

import com.sun.sgs.app.AppContext;

import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.datatypes.IPlayerProperty;
import common.datatypes.PlayerProperty;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetPlayerResponse;
import common.processors.IProcessor;

// TODO: Auto-generated Javadoc
/**
 * Este procesador es el encargado de realizar las acciones correspondientes a
 * mensajes que solicitan la devolucion de los datos correspondientes a un
 * jugador.
 * 
 * @author lito
 * @see #process(IMessage)
 */
public final class PGetPlayer extends ServerMsgProcessor {
	
	/**
	 * Construcotr por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PGetPlayer() {
		
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
		return new PGetPlayer();
	}
	
	/**
	 * Obtiene el identificador del player a retornar del mensaje pasado como
	 * parametro, luego solicita al {@link DataManager} que devuelva el objeto
	 * con el biding igual a dicho identificador, por ultimo contruye un mensaje
	 * {@link MsgGetPlayerResponse} y lo envia a travez de la referencia al
	 * player de la instancia (el que envio la solicitud).
	 * 
	 * @param msg Es casteado a {@link MsgPlainText} y se interpreta que el
	 * texto representa el identificador del player que se quiere
	 * retornar.
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(final IMessage msg) {
		try {
			// Puede arrojar UnsopportedMessageException
			MsgGetPlayerResponse msgGPR = (MsgGetPlayerResponse) MessageFactory
					.getInstance().createMessage(
							MsgTypes.MSG_GET_PLAYER_RESPONSE_TYPE);
			
			Player playerToReturn = (Player) AppContext.getDataManager()
					.getBinding(((MsgPlainText) msg).getMsg());
			
			msgGPR.setActualWorld(playerToReturn.getActualWorld());
			msgGPR.setAngle(playerToReturn.getAngle());
			msgGPR.setPlayerState(playerToReturn.getState());
			msgGPR.setPosition(playerToReturn.getPosition());
			
			// tabla de hash auxiliar para poner las propieddades presentes en
			// el objeto Player, y las del ModelAccess.
			HashMap<String, IPlayerProperty> properties = 
				new HashMap<String, IPlayerProperty>();
			
			// Coloco las propiedades del manged object.
			for (IPlayerProperty prop : playerToReturn.getProperties()
					.values()) {
				properties.put(prop.getId(), prop);
			}
			// Coloco las propiedades del ModelAccess.
			// Primero coloco los stats
			for (IPlayerProperty prop : ModelAccess.getInstance()
					.getPlayerStats(playerToReturn.getIdEntity())) {
				properties.put(prop.getId(), prop);
			}
			// ahora coloco los puntos globales y el dinero.
			PlayerProperty globalScore = new PlayerProperty();
			globalScore.setId(PlayerProperty.PP_GLOBAL_SCORE);
			globalScore.setValue(ModelAccess.getInstance().getGlobalScore(
					playerToReturn.getIdEntity()));
			
			PlayerProperty money = new PlayerProperty();
			money.setId(PlayerProperty.PP_MONEY);
			money.setValue(ModelAccess.getInstance().getMoney(
					playerToReturn.getIdEntity()));
			
			// seteo las propiedades
			msgGPR.setProperties(properties);
			
			msgGPR.setSkin(ModelAccess.getInstance().getSkin(
					playerToReturn.getIdEntity()));
			
			// Envio el mensaje con la respuesta. al Player que solicito
			getPlayerAsociete().send(msgGPR);
			
		} catch (UnsopportedMessageException e) {
			// Esta excepcion no tendria porque ocurrir nunca.
			e.printStackTrace();
		}
	}
	
}
