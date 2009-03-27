package ar.edu.unicen.exa.server;

import com.jme.math.Vector3f;

public interface IControlJugador {
	   public void nuevo(String Jugador);
       public void comenzar(String Jugador);
	   public void listo(String Jugador);
       public void terminar(String Jugador);
       public Vector3f getPosDefecto();
}
