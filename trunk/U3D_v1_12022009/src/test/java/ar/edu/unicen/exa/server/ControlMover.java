package ar.edu.unicen.exa.server;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.edu.unicen.exa.server.JugadorSimulado.PLAYERSTATE;

import com.jme.math.Vector3f;

public class ControlMover implements IControlJugador {
	
	static final Logger logger =
        Logger.getLogger(JugadorSimulado.class.getName());
    
	private int demora=5;
		
    HashMap<String, JugadorSimulado> jugadores = new HashMap<String, JugadorSimulado>();	 
	
	@Override
	public void comenzar(String jugador) {
		JugadorSimulado cjugador = jugadores.get(jugador);
		if (cjugador.setEstado(PLAYERSTATE.jugando)) {
             logger.log(Level.FINE, "Comienzo de juego para {0}", cjugador.getNombre());
             for(int i=0;i<100;i++){
            	 try {
					Thread.sleep(demora);
				} catch (InterruptedException e) {
					 e.printStackTrace();
				}
            	 cjugador.mover();
             }
        } else
             logger.log(Level.WARNING, "Recibido comienzo pero {0} ha salido",
                        cjugador.getNombre());
	}

	@Override
	public void listo(String jugador) {
		 JugadorSimulado cjugador = jugadores.get(jugador);
		if (cjugador.getEstado() != PLAYERSTATE.enEspera) {
            try{
                //dormir hasta que se este listo
              Thread.sleep(5000); 
            } catch (Exception ioe) {}
        } else
        	logger.log(Level.FINE, "Jugador {0} Listo para comenzar",cjugador.getNombre());
     }

	@Override
	public void nuevo(String jugador) {
		 JugadorSimulado cjugador = jugadores.get(jugador);
		 if (cjugador.getEstado() == PLAYERSTATE.enLogueo) {
             logger.log(Level.FINE, "Nuevo juego para {0}",
                        new Object[] {cjugador.getNombre()});
          } else
             logger.log(Level.WARNING, "Recibido nuevo juego pero {0} no esta en espera",
            		 cjugador.getNombre());

	}

	@Override
	public void terminar(String jugador) {
		JugadorSimulado cjugador = jugadores.get(jugador);
		cjugador.salir();
	}

	@Override
	public Vector3f getPosDefecto() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addJugador(JugadorSimulado jugador){
          jugadores.put(jugador.getNombre(), jugador);		
	}
 public void setDemora(int demora){
	 this.demora=demora;	 
 }
	
}
