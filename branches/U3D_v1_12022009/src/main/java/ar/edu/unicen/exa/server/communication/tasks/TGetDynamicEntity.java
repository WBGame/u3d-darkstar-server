package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.entity.DynamicEntity;
import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ObjectNotFoundException;

import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.responses.MsgGetDynamicEntityResponse;


/**
 * La tarea se ejecutará al recibir un mensaje ({@link MsgGetDynamicEntity})
 * directo desde un cliente, que desea la infomación completa de una entidad
 * dinámica cuyo identificador esta presente en el mensaje. <br/> 
 * Los pasos a seguir son: <br/>
 * * Obtener la entidad dinámica a partir del DataManager. <br/>
 * * Construir un mensaje del tipo {@link MsgGetDynamicEntityResponse} y
 * setearle la entidad dynamica. <br/>
 * * Enviarle el mensaje construido de vuelta al jugador que solicitó la
 * información.
 * 
 */
public final class TGetDynamicEntity extends TaskCommunication {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8571194487175360055L;
	/**
	 * The Constructor.
	 * 
	 * @param msg the msg
	 */
	public TGetDynamicEntity(final IMessage msg) {
		super(msg);
	}
	
	/**
	 * Crear y devuelve una instancia de la clase.
	 * 
	 * @param msg El mensaje con el que trabajará la tarea internamente.
	 * @return Una instancia de esta clase
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TGetDynamicEntity(msg);
	}
	
	/**
	 * Este metodo obtiene una entidad dinamica a partir de su identificador 
	 * contenido en el mensaje recibido. Ademas crea un mensaje de respuesta
	 * con la informacion de dicha entidad para luego ser enviada al jugador
	 * que la solicito.
	 * 
	 * @encoding UTF-8
	 */
	public void run() {
				
		//castear al mensage plano
		MsgPlainText msg = (MsgPlainText) getMessage();

		//obtener el id de la entidad dinamica
		String idDynamicEntity = msg.getMsg();
		
		DynamicEntity dinamicEntity = null;
		try {
		//Obtener la entidad dinámica a partir del DataManager
		dinamicEntity = (DynamicEntity) AppContext.getDataManager()
			.getBinding(idDynamicEntity);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		
		//crear el mensaje de respuesta
		MsgGetDynamicEntityResponse msgGDER = null;
		try {
			msgGDER = (MsgGetDynamicEntityResponse)
				MessageFactory.getInstance().createMessage(
				MsgTypes.MSG_GET_DYNAMIC_ENTITY_RESPONSE_TYPE);
			
			//seteo el mensaje con la informacion correspondiente
			msgGDER.setActualWorld(dinamicEntity.getActualWorld());
			msgGDER.setAngle(dinamicEntity.getAngle());
			msgGDER.setIdDynamicEntity(dinamicEntity.getIdEntity());
			msgGDER.setPosition(dinamicEntity.getPosition());
			msgGDER.setSkin(ModelAccess.getInstance().getSkin(
					dinamicEntity.getIdEntity()));
			
		} catch (UnsopportedMessageException e) {
			e.printStackTrace();
		}
		
		//enviar el mensaje con la respuesta al Player que solicito
		getPlayerAssociated().send(msgGDER);
	}
	
}
