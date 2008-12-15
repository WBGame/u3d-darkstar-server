package ar.edu.unicen.exa.server.serverLogic;

import java.util.Set;

import common.datatypes.Skin;
import common.datatypes.Quest;
import common.datatypes.D2GameScore;
import common.datatypes.Ranking;
import common.datatypes.PlayerStat;

/**
 * La clase implementa la interface de acceso al modelo de logica del juego.
 * Este modelo debera proveer soporte para el almacenamiento percistente y
 * recuperacion de datos relevantes para la mecanica del juego, que abarca
 * puntos tales como almacenamiento de Players, 2DGames, Quest, estado de
 * quests, puntuacion de stats, etc.<BR/>
 * 
 * La clase sigue el patron de diseño <I>Sinleton</I>.
 *
 * @author Cabrea Emilio Facundo &lt;cabrerafacundo at gmail dot com&gt;
 * @encoding UTF-8
 * 
 * TODO Implementar.
 * TODO Extraer una interface a partir de esto. Interface Name IModel.
 */
public final class ModelAccess {
	
	/**
	 * Instancia de la clase.
	 */
	private static ModelAccess instance = new ModelAccess();
	
	/**
	 * @return La instancia singleton de la clase.
	 */
	public static ModelAccess getInstance() {
		return instance;
	}
	
	/**
	 * Constructor por defecto de la clase, inicializa el estado interno de la
	 * misma.
	 */
	private ModelAccess() {
		// begin-user-code
		// TODO Apéndice de constructor generado automáticamente
		// end-user-code
	}
	
	/**
	 * Retorna el Skin relacionado al identificador de jugador pasado como
	 * parametro.
	 * 
	 * @param idPlayer El identificador del jugador para el que se desea obtener
	 *        el skin.
	 * @return El Skin relacionado al identificador del jugador.
	 */
	public Skin getSkin(final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * Retorna el conjunto de identificadores de juegos 2D que estan disponibles
	 * para el identificador del jugador pasado como parametro.
	 * 
	 * @param idPlayer El identificador del jugador para el cual se desea
	 *        conocer los juegos disponibles.
	 * @return Un conjunto con los identificadores de juegos 2D disponibles para
	 *         el jugador.
	 */
	public Set<String> getAvailableGames(final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * Realiza las acciones correspondientes para marcar como "comprado" el
	 * juego para el jugador.
	 * 
	 * @param id2DGame El identificador del juego a comprar.
	 * @param idPlayer El identificador del jugado que compra el juego.
	 */
	public void buy2DGame(final String id2DGame, final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		// end-user-code
	}
	
	/**
	 * Construye una instancia de la clase en base a la informacion almacenada
	 * par el identificador de Quest pasado.
	 * 
	 * @param idQuest El identificador de la quest que se desea obtener.
	 * @return una instancia de la clase en base a la informacion almacenada par
	 *         el identificador de Quest pasado.
	 */
	public Quest getQuest(final String idQuest) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * Obtiene el conjunto de Quests disponibles par ael jugador pasado.
	 * 
	 * @param idPlayer El identificador del jugador.
	 * @return El conjunto de Quests disponibles par ael jugador pasado.
	 */
	public Set<Quest> getAvailableQuests(final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * 
	 * @return Un conjunto con los identificadores de los juegos 2D de todos
	 *         aquellos juegos que se puedan comprar.
	 */
	public Set<String> getBuyables2DGames() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * 
	 * @param id2DGame Identificador del juego 2D
	 * @param idPlayer Identificador del juegador.
	 * @return La cantidad de veces que el juegador ah jugado el juego 2D.
	 */
	public int getPlayedTimes(final String id2DGame, final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return 0;
		// end-user-code
	}
	
	/**
	 * @param idPlayer Identificador del jugador.
	 * @return La puntuacion global del jugador pasado como parametro.
	 */
	public int getGlobalScore(final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return 0;
		// end-user-code
	}
	
	/**
	 * @param idPlayer Identificador del jugador
	 * @return Conjunto de quests que esta haciendo actualmente el jugador.
	 */
	public Set<Quest> getCurrentQuests(final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * Dummy implementation. Check always if login == password.  
	 * 
	 * @param password La contraseña.
	 * @param idPlayer El identificador del jugador
	 * 
	 * @return {@code true} si la contraseña suministrada para el usuario es la
	 *         que se tiene almancenada. {@code false} en caso contrario.
	 */
	public boolean checkPlayer(final String password, final String idPlayer) {
		return password.equalsIgnoreCase(idPlayer);
	}
	
	/**
	 * Agrega el puntaje pasado como parametro al modelo, realizando todas las
	 * acciones corresponidentes.<BR/>
	 * Estas acciones consiten en actualizar los stats para el jugador 
	 * dueño del puntaje, asi como tmb el historial de puntajes para el
	 * minijuego asociado
	 * y chequear si es el puntaje mas alto.
	 * 
	 * @param score El puntaje a agregar al modelo.
	 */
	public void add2DGameScore(final D2GameScore score) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * Cambia el puntaje global asociado al jugador por el pasado como
	 * parametro.
	 * 
	 * @param idPlayer Identificador del jugador.
	 * @param globalScore el puntaje global.
	 */
	public void setGlobalScore(final String idPlayer, final int globalScore) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * @param id2DGame Identificador del juego2D
	 * @return El ranking para el juego 2D pasado como parametro.
	 */
	public Ranking getRanking(final String id2DGame) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * Agrega la quest al conjunto de quest que estan siendo jugadas por el
	 * jugador actualmente.
	 * 
	 * @param idPlayer Identificador del jugador.
	 * @param idQuest Identificador de la quest.
	 */
	public void startQuest(final String idPlayer, final String idQuest) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * Agrega la quest al conjunto de quest que ya han sido terminadas por el
	 * jugador.
	 * 
	 * @param idPlayer Identificador del jugador.
	 * @param idQuest Identificador de la quest.
	 */
	public void finishQuest(final String idPlayer, final String idQuest) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * Saca la quest al conjunto de quest que estan siendo jugadas por el
	 * jugador actualmente.
	 * 
	 * @param idPlayer Identificador del jugador.
	 * @param idQuest Identificador de la quest.
	 */
	public void abortQuest(final String idPlayer, final String idQuest) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * Cambia el estado de la quest en curso pasada como parametro indicandole
	 * que avance al siguiente estado de la misma.
	 * 
	 * @param idPlayer Identificador del jugador.
	 * @param idQuest Identificador de la quest.
	 * @param idQuestState El proximo estado de la quest.
	 */
	public void nextQuestState(final String idPlayer, final String idQuest,
			final String idQuestState) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * 
	 * @param idPlayer Identificador del jugador.
	 * @return el conjunto de PlayerStats del jugador pasado como parametro.
	 */
	public Set<PlayerStat> getPlayerStats(final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * @param idPlayer Identificador del jugador.
	 * @return El dinero del jugador pasado como parametro.
	 */
	public float getMoney(final String idPlayer) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return 0;
		// end-user-code
	}
	
	/**
	 * Setea el dinero del jugador y lo perciste en el modelo.
	 * 
	 * @param idPlayer Identificador del jugador.
	 * @param money El dinero a setear.
	 */
	public void setMoney(final String idPlayer, final float money) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * Retorna el costo de un juego determinado.
	 * 
	 * @param id2DGame identificador del juego que nos interesa consultar.
	 * 
	 * @return valor del juego especificado.
	 */
	public float get2DGamePrice(final String id2DGame) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
