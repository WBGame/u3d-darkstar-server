///   NOTA: VALIDO HASTA LA REVISION 295 DEL SERVER



package ar.edu.unicen.exa.server.communication.tasks;

import java.awt.Rectangle;

import net.java.dev.mocksgs.MockSGS;

import org.testng.annotations.*;

import com.jme.math.Vector3f;

import com.sun.sgs.app.ClientSession;

import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;

import ar.edu.unicen.exa.server.communication.tasks.TMove;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.player.Player;

public class TMoveTest {

	/*Variable de mensaje*/
	private MsgMove iMsg;
	/*Para la instancia de la tarea de movimiento*/
	private TMove mover;
	
	/*limites celda*/
	/** limite inferior X. */
	private static final int BOUND_X = 0;
	/** limite inferior Y. */
	private static final int BOUND_Y = 0;
	/** Ancho de la celda. */
	private static final int BOUND_WIDTH = 100;
	/** Alto de la celda. */
	private static final int BOUND_HEIGHT = 100;
	
	private static final int ORIGEN_X = 10;
	private static final int ORIGEN_Y = 15;
	private static final int ORIGEN_Z = 1;
	
	private static final int DESTINO_DENTRO_X = 52;
	private static final int DESTINO_DENTRO_Y = 25;
	private static final int DESTINO_DENTRO_Z = 1;
	
	private static final int DESTINO_FUERA_X = 200;
	private static final int DESTINO_FUERA_Y = 35;
	private static final int DESTINO_FUERA_Z = 1;
	
	/*destino que no se encuentra dentro de la estructura que representa al mundo*/
	private static final int DESTINO_INEXISTENTE_X = 300;
	private static final int DESTINO_INEXISTENTE_Y = 35;
	private static final int DESTINO_INEXISTENTE_Z = 1;
	
	Rectangle cellBunds = new Rectangle(
			BOUND_X, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT
	);
	
	Rectangle cellBunds1 = new Rectangle(
			200, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT
	);
	
	private Player jugador_movimiento;
	private Player jugador1;
	private Player jugador2;
	private ClientSession session_movimiento=new PlayerSession();
	private ClientSession session_jugador1=new PlayerSession();
	private ClientSession session_jugador2=new PlayerSession();
	private CellArray estructura = new CellArray();;
	private Cell celda; 
	private Cell celda1; 
	private Cell celda2; 
	private Cell celda3; 
	private Cell celda4; 
	
	/*Para poder formar mensaje de movimiento*/
	private Vector3f destino=  new Vector3f();
	private Vector3f origen =  new Vector3f();
	private String jugador_en_movimiento="Eduardo";
	
		
	@BeforeClass
	public void setUp() throws Exception {
		 /* inicializo mocking */
		 MockSGS.init();
		 celda = new Cell(cellBunds,estructura);
		 celda1 = new Cell(cellBunds,estructura);
		 celda2 = new Cell(cellBunds,estructura);
		 celda3 = new Cell(cellBunds,estructura);
		 celda4 = new Cell(cellBunds1,estructura);
		 estructura.addCelda(celda);
		 estructura.addCelda(celda1);
		 estructura.addCelda(celda2);
		 estructura.addCelda(celda3);
		 estructura.addCelda(celda4);
		 /*Creo mensaje y configuro*/
		 iMsg = (MsgMove) MessageFactory.getInstance()
			.createMessage(MsgTypes.MSG_MOVE_SEND_TYPE);
		 origen.set(ORIGEN_X, ORIGEN_Y,ORIGEN_Z);
		 iMsg.setPosOrigen(origen);
		 iMsg.setIdDynamicEntity(jugador_en_movimiento);
		 /*Creo tarea de movimiento*/
		 mover=new TMove(iMsg);
		 /*Configuro session*/
		 ((PlayerSession) session_movimiento).setConectado(false);
		 ((PlayerSession) session_movimiento).setNombre(jugador_en_movimiento);
		 ((PlayerSession) session_jugador1).setConectado(true);
		 ((PlayerSession) session_jugador1).setNombre("Pepe");
		 ((PlayerSession) session_jugador2).setConectado(true);
		 ((PlayerSession) session_jugador2).setNombre("Juan");
		 jugador_movimiento=Player.create(session_movimiento);
		 jugador_movimiento.setPosition(origen);
		 jugador_movimiento.setSession(session_movimiento);
		 jugador1=Player.create(session_jugador1);
		 jugador1.setSession(session_jugador1);
		 jugador2=Player.create(session_jugador2);
		 jugador2.setSession(session_jugador2);
		 celda1.joinToChannel(jugador1.getSession());
		 celda2.getChannel().join(jugador2.getSession());
		 mover.setPlayerAssociated(jugador_movimiento);
		 mover.setCellAssociated(celda);
    }
	
	//volver a la posición inicial luego de haber ejecutado un movimiento
	@AfterMethod (alwaysRun=true)
	public void VolverPos(){
		origen.set(ORIGEN_X, ORIGEN_Y,ORIGEN_Z);
		jugador_movimiento.setPosition(origen);
	}
	
	@AfterTest
    public void ResetearContexto()
    {
        MockSGS.reset();
    }
		
	/*probar una posicion dentro de la celda*/ 
	@Test (groups = { "TestMover" })
	public void moverDentroCelda(){
		destino.set(DESTINO_DENTRO_X,DESTINO_DENTRO_Y,DESTINO_DENTRO_Z);
		iMsg.setPosDestino(destino);
		try {
			MockSGS.run(mover);
		} catch (Exception e) {
		}
	   assert (jugador_movimiento.getPosition().equals(iMsg.getPosDestino()));
	}

	/*probar una posicion fuera de la celda que existe dentro de la extructura*/ 
	@Test (groups = { "TestMover" })
	public void moverFueraCelda(){
		destino.set(DESTINO_FUERA_X,DESTINO_FUERA_Y,DESTINO_FUERA_Z);
		iMsg.setPosDestino(destino);
		try {
			MockSGS.run(mover);
		} catch (Exception e) {
		}
		assert (jugador_movimiento.getPosition().equals(iMsg.getPosDestino()));
	}
	
	/*probar una posicion fuera de la celda que no existe dentro de la extructura*/ 
	@Test (groups = { "TestMover" })
	public void moverPosInexistente(){
		destino.set(DESTINO_INEXISTENTE_X,DESTINO_INEXISTENTE_Y,DESTINO_INEXISTENTE_Z);
		iMsg.setPosDestino(destino);
		try {
			MockSGS.run(mover);
		} catch (Exception e) {
		} 
		assert (!jugador_movimiento.getPosition().equals(iMsg.getPosDestino()));
	}
	
}
