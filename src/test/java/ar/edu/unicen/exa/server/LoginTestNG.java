package ar.edu.unicen.exa.server;

/**
* @author Eduardo Torella 
* @encoding UTF-8   
*/

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jme.math.Vector3f;

import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MsgPlainText;
import common.messages.responses.MsgGetPlayerResponse;

public class LoginTestNG {
   
	private TestClient cliente1;
	private TestClient cliente2;
	private TestClient cliente3;
	private TestClient cliente4;
	private TestClient cliente5;
   //mundo y posición por defecto segun u3dserver.propierties 
   
   private static final String MUNDO_DEFECTO = "5";
   private static final String EXACTAS = "3";
   private static final float  POS_DEFECTO_X = 13000;
   private static final float  POS_DEFECTO_Y = 1.5f;
   private static final float  POS_DEFECTO_Z =7000;
   private static final float  POS_DEFECTO_EXACTAS_X = 300;
   private static final float  POS_DEFECTO_EXACTAS_Y = 1.5f;
   private static final float  POS_DEFECTO_EXACTAS_Z = 300;
   
   @BeforeClass
   public void setUp(){
	   cliente1= new TestClient();
	   cliente2= new TestClient();
	   cliente3= new TestClient();
	   cliente4= new TestClient();
	   cliente5= new TestClient();
	   	   
	   cliente1.setLogin("Eduardo");
	   cliente1.setPassword("Eduardo");
	   
	   /*para intentar con un usuario ya logeado*/
	   cliente2.setLogin("Eduardo");
	   cliente2.setPassword("Eduardo");
	   
	   cliente3.setLogin("Juan");
	   cliente3.setPassword("Juan");
	   
	   /*para probar con password nulo*/
	   cliente4.setLogin("Jose");
	   cliente4.setPassword("");
	   /*Password incorrecto*/
	   cliente5.setLogin("Jorge");
	   cliente5.setPassword("123");
	   
       /*Configuración para el caso de un logeo posterior al primero*/
	   IMessage cambiarMundo=cliente3.buildMessageChangeWorld(EXACTAS,
			   POS_DEFECTO_EXACTAS_X,POS_DEFECTO_EXACTAS_Y,POS_DEFECTO_EXACTAS_Z);
	   cliente3.login();
	   while(!cliente3.isConnected()&&!cliente3.isJoinedToChannel());
	   cliente3.sendMessage(cambiarMundo);
       if (cliente3.isConnected()) 	   
	       cliente3.logout();
   }
   
      
   @DataProvider(name = "coneccionAceptada")
   public Object[][] createData1() {
    return new Object[][] {
      { cliente1 },
      { cliente3}
     };
   }
   
   @DataProvider(name = "coneccionNegada")
   public Object[][] createData2() {
    return new Object[][] {
      { cliente2 },
      { cliente4},
      { cliente5},
    };
   }

   /*El time out corresponde al declarado en el archivo u3dserver.propierties**/ 
   @Test(timeOut=100000,dataProvider="coneccionAceptada")
    public void coneccionAceptada(TestClient cliente){
    	cliente.login();
    	while(!cliente.isConnected() &&
    			!cliente.isJoinedToChannel());
     }
   
   @Test(timeOut=100000 ,dataProvider="coneccionNegada")
   public void coneccionNegada(TestClient cliente){
	cliente.login();
   	assert(!cliente.isConnected());
   }
   
   @Test(dependsOnMethods="coneccionAceptada",dataProvider="coneccionAceptada")
   public void enviado(TestClient cliente){
	   MsgPlainText msg = (MsgPlainText) cliente.buildMessageGetPlayer();
       assert(cliente.sendMessage(msg));
   };
   
   
   @Test(dependsOnMethods="enviado",dataProvider="coneccionAceptada")
   public void recibido(TestClient cliente) {
	   boolean recibidoMsg=false;
      while(!recibidoMsg){
    	  recibidoMsg=cliente.recibido();
      }
   }
   
    /**Primer logueo
    * Los datos corresponden a los consignados en el archivo u3dserver.propierties*/
   @Test(dependsOnMethods={"recibido"})
   public void primerLogueo() throws UnsopportedMessageException{
	   MsgGetPlayerResponse msgGetPlayer = (MsgGetPlayerResponse) cliente1.serverMsg();
	   assert(msgGetPlayer.getActualWorld().equals(MUNDO_DEFECTO));
	   Vector3f pos = new Vector3f();
	   pos.set(POS_DEFECTO_X,POS_DEFECTO_Y,POS_DEFECTO_Z);
	   assert(msgGetPlayer.getPosition().equals(pos));
   }
   
   /**Luego de un  logueo
    * Los datos corresponden a los consignados en el archivo u3dserver.propierties*/
   @Test(dependsOnMethods="recibido")
   public void logueo() throws UnsopportedMessageException{
	   MsgGetPlayerResponse msgGetPlayer = (MsgGetPlayerResponse) cliente3.serverMsg();
	   assert(msgGetPlayer.getActualWorld().equals(EXACTAS));
	   Vector3f pos = new Vector3f();
	   pos.set(POS_DEFECTO_EXACTAS_X,POS_DEFECTO_EXACTAS_Y,POS_DEFECTO_EXACTAS_Z);
	   assert(msgGetPlayer.getPosition().equals(pos));
   }
   
    @AfterClass
    public void desconectar(){
    	if (cliente1.isConnected())
    		cliente1.logout();
    	if (cliente3.isConnected())
    		cliente3.logout();
    }
}
