package ar.edu.unicen.exa.server;

import ar.edu.unicen.exa.server.JugadorSimulado.PLAYERSTATE;


public class PruebaJugadorSimulado {

	/**
	 * @param args
	 * @throws Exception 
	 */
	private static int nro_clientes = 20;
	private static JugadorSimulado[] clientes;
	
	private static boolean fin(){
		boolean retorno=true;
		    for(int i=0;i<nro_clientes & retorno;i++)
		        if (clientes[i].getEstado()!=PLAYERSTATE.salir)
		        	retorno=false;
		return retorno;
	}
	
	public static void main(String[] args) throws Exception {
	   ControlMover controlMovimiento = new ControlMover();
	   clientes = new JugadorSimulado[nro_clientes];
	   for (int i = 0; i < clientes.length; i++){
		   clientes[i] = new JugadorSimulado("CLIENTE_"+Integer.toString(i),
				   							 "CLIENTE_"+Integer.toString(i),
				   							 controlMovimiento);
		controlMovimiento.addJugador(clientes[i]);   
		new Thread(clientes[i]).start();
	  }
	 while(!fin())
		 Thread.sleep(180000);
	}
}
