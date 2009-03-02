package ar.edu.unicen.exa.server.communication.tasks;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.sun.sgs.app.ClientSession;

public class PlayerSession implements ClientSession,Serializable {
  
	private static final long serialVersionUID = 6988790588972887996L;
	private String nombre="";
    private boolean conectado=false;
    
	@Override
	public String getName() {
		return nombre;
	}

	@Override
	public boolean isConnected() {
		return conectado;
	}

	@Override
	public ClientSession send(ByteBuffer message) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setConectado(boolean conectado){
		this.conectado=conectado;
	}
	
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
}
