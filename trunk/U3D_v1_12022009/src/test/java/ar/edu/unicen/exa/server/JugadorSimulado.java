package ar.edu.unicen.exa.server;

/*
 * Copyright (c) 2008, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**Basado en SimulatedPlayer del proyecto snowman cuya licencia se adjunta.
 * @author Eduardo Torella 
 * @encoding UTF-8
 * */


import java.io.IOException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;

import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;

public class JugadorSimulado implements Runnable, SimpleClientListener  {
    
	static enum PLAYERSTATE {
        enLogueo,       // Estado Inicial esperando por el logueo
        enEspera,       // Logueado esperando para comenzar juego
        jugando,        // realizando movimientos
        pausado,        // sin realizar movimiento
        salir,          // saliendo
        logginRechazado  
    }
	
	static final Logger logger =
        Logger.getLogger(JugadorSimulado.class.getName());
	
	private String nombre;
	private String password;
	private SimpleClient cliente;
	private final String PORT="1119";
	private final String HOST="192.168.1.3";
	private PLAYERSTATE estado;
	private IControlJugador controlJugador;
	//private long ultimaMarcaDetiempo=0;
	private ClientChannel canal=null;
	private Vector3f posActual=new Vector3f();
	private float comX;
	private float comY;
	private float destX;
	private float destY;
	
	static private final Random random = new Random(System.currentTimeMillis());
	
	public JugadorSimulado(String nombre, String password, IControlJugador control){
		Properties props = new Properties();
		props.put("host", HOST);
		props.put("port", PORT);
		this.nombre=nombre;
		this.password=password;
		this.controlJugador=control;
		posActual.set(0,0,0);
		cliente = new SimpleClient(this);
		estado=PLAYERSTATE.enLogueo;
		try {
			cliente.login(props);
			} catch (IOException e) {
			System.out.println("Loggin del cliente darkstar ha fallado");
			e.printStackTrace();
			disconnected(false, e.getMessage());
			estado=PLAYERSTATE.logginRechazado;
		}
	 }
	
	
	/** metodo para generar la pos del movimiento */
	private Vector3f generarPos(){
		Vector3f retorno=new Vector3f();
		comX = posActual.x;
	    comY = posActual.y;
	    destX = comX + random.nextFloat();
        destY = comY + random.nextFloat();
        retorno.set(destX,destY,1);
		return retorno;
	}
	
    @Override
	public void run() {
    	controlJugador.nuevo(nombre);
		controlJugador.listo(nombre);
	    controlJugador.comenzar(nombre);
		controlJugador.terminar(nombre);
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication( 
				nombre,
				password.toCharArray() 
		);
	}

	@Override
	public void loggedIn() {
		if (estado != PLAYERSTATE.salir)
            logger.log(Level.FINE, "Jugador {0} logueado ",nombre);
        else
            logger.log(Level.WARNING, "Jugador {0} recibio logueo luego de salir",
                       nombre);	
	}

	@Override
	public void loginFailed(String reason) {
		logger.log(Level.WARNING, "Login failed for {0}", nombre);
        salir();
	}

	@Override
	public void disconnected(boolean graceful, String reason) {
		logger.log(Level.FINE, "Disconnected player: {0}, Reason: {1}", new Object[]{nombre, reason});
        salir();
	}

	@Override
	public ClientChannelListener joinedChannel(ClientChannel channel) {
		canal=channel;
		setEstado(PLAYERSTATE.enEspera);
		return new ChannelListener();
	}

	@Override
	public void receivedMessage(ByteBuffer message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnected() {
		logger.log(Level.FINE, "Re-conectado jugador: {0}", nombre);
	}

	@Override
	public void reconnecting() {
		logger.log(Level.FINE, "Re-conectando jugador: {0}", nombre);
		
	}
	
public void sendToChannel(final IMessage message) {
    	
    	// Convierto Mensaje a ByteBuffer
		ByteBuffer msj = message.toByteBuffer();
    	
        try {
                this.canal.send(msj);
    			} catch (Exception e) {
                e.printStackTrace();
        	}
    }
	
	public PLAYERSTATE getEstado(){
		return estado;
	}
	
	 public String getNombre(){
		 return nombre;
	 }
	 
	 public synchronized boolean setEstado(PLAYERSTATE estadoNuevo){
		   if (estado != PLAYERSTATE.salir) {
		        logger.log(Level.FINER, "El estado del Jugador {0} ha cambiado a {1}",
		                       new Object[] {nombre, estadoNuevo});
		        estado= estadoNuevo;
		        return true;
		   } else
		        return false;
     }
	 
	 void salir() {
	        if (setEstado(PLAYERSTATE.salir)) {
	            try {
	                cliente.logout(false);
	            } catch (Exception ignore) {}
	        }
	    }
	 
	 public void mover(){
		 //crear mensaje de movimiento
		 MsgMove iMsg =null;
		 try {
		  	iMsg = (MsgMove) MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_MOVE_SEND_TYPE);
		} catch (UnsopportedMessageException e) {
				e.printStackTrace();
		}
		Vector3f destino = new Vector3f();
		destino=generarPos();
		iMsg.setPosOrigen(posActual);
		iMsg.setPosDestino(destino);
		iMsg.setIdDynamicEntity(nombre);
		sendToChannel(iMsg);
		//actualizo posicición actual
		posActual.set(destX,destY,1);
	 }
/*----------------------------------------------------------------------------------*/
	 public final class ChannelListener implements ClientChannelListener {

			/**
			 * Constructora de la clase.
			 */
			public ChannelListener() { }

			/**
			 * Este método es invocado cuando el servidor desuscribe al usuario del
			 * canal channel al cual esta asociado. 
			 * 
			 * @param ch el canal que fue quitado el usuario.
			 */
			public void leftChannel(final ClientChannel ch) {
				logger.info("El "+nombre+" fue quitado del canal "
						+ ch.getName());
			}

			@Override
			public void receivedMessage(ClientChannel channel,
					ByteBuffer message) {
				// TODO Auto-generated method stub
				
			}
	 }
/*-----------------------------------------------------------------------------------*/	 
	
}
