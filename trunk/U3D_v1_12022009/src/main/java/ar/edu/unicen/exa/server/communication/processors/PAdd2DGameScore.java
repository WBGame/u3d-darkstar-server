package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;

import common.datatypes.D2GameScore;
import common.messages.IMessage;
import common.messages.requests.MsgAdd2DGameScore;
import common.processors.IProcessor;

/**
 * Este procesador se encarga de aquellos mensajes que tienen por objetivo
 * agregar un puntaje a un minijuego.
 * 
 * @author fede
 * @encoding UTF-8.
 * @see #process(IMessage)
 */
public final class PAdd2DGameScore extends ServerMsgProcessor {
	
	/**
	 * Construcotr por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PAdd2DGameScore() { }
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor.
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	@Override
	public IProcessor factoryMethod() {
		return new PAdd2DGameScore();
	}
	
	/**
	 * Toma la petición de agregar un puntaje a un minijuego y la procesa. Toma
	 * contenido en el mensaje pasado como paramentro, e invocando el metodo
	 * {@link ModelAccess#add2DGameScore(D2GameScore)} agrega el puntaje al
	 * modelo.
	 * 
	 * @param msg Contiene el Score a agregar al modelo.
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	@Override
	public void process(final IMessage msg) {
		// Casteo el mensaje paraobtener el scrore.
		MsgAdd2DGameScore msgAddScore = (MsgAdd2DGameScore) msg;
		
		// Obtengo el score.
		D2GameScore score = msgAddScore.getScore();
		
		// Se agrega el score a la modelo.
		ModelAccess.getInstance().add2DGameScore(score);
	}	
}
