package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.messages.IMessage;
import common.messages.MsgPlainText;
import common.processors.IProcessor;

/**
 * Este procesador se encarga de aquellos mensajes que tienen por objetivo pedir
 * comprar un juego.
 * 
 * @author Polo
 * @encoding UTF-8.
 * @see #process(IMessage)
 */
public final class PBuyGame extends ServerMsgProcessor {
	
	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PBuyGame() { }
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor.
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PBuyGame();
	}
	
	/**
	 * Toma la petición de comprar un juego y la procesa. Toma contenido en el
	 * mensaje pasado como paramentro, e invocanda el metodo
	 * {@link ModelAccess#buy2DGame(String, String)}.
	 * 
	 * @param msg Contiene el id del juego 2D que se quiere comprar.
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	public void process(final IMessage msg) {
		MsgPlainText msgBuyGame = (MsgPlainText) msg;
		
		String id2DGame = msgBuyGame.getMsg();
		
		String idPlayer = getPlayerAssociated().getIdEntity();
		
		ModelAccess.getInstance().buy2DGame(id2DGame, idPlayer);
	}	
}
