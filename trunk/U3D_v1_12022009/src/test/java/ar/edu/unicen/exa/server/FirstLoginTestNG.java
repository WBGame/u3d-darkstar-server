package ar.edu.unicen.exa.server;

/**
* @author Eduardo Torella 
* @encoding UTF-8   
*/

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jme.math.Vector3f;

import common.exceptions.UnsopportedMessageException;
import common.messages.MsgPlainText;
import common.messages.responses.MsgGetPlayerResponse;

public class FirstLoginTestNG {
   private TestClient cliente1;
   
    private static final String EXTERIOR = "510";
   
   @BeforeClass
   public void setUp(){
	   cliente1= new TestClient();
   }
   
   /*El time out corresponde al declarado en el archivo u3dserver.propierties**/ 
   @Test(timeOut=100000)
    public void coneccionAceptada(){
    	cliente1.setLogin("Eduardo");
    	cliente1.setPassword("Eduardo");
    	cliente1.login();
    	while(!cliente1.isJoinedToChannel());   
   }
   
   @Test(dependsOnMethods={"coneccionAceptada"})
   public void enviado(){
	   MsgPlainText msg = null;
       msg=(MsgPlainText) cliente1.buildMessageGetPlayer();
       cliente1.sendMessage(msg);
       assert(msg!=null);
   };
   
   @Test(dependsOnMethods={"enviado"})
   public void recibido() {
	   boolean recibidoMsg=false;
      while(!recibidoMsg){
    	  recibidoMsg=cliente1.recibido();
      }
   }
   
    /**Primer logueo
    * Los datos corresponden a los consignados en el archivo u3dserver.propierties*/
   @Test(dependsOnMethods={"recibido"})
   public void primerLogueo() throws UnsopportedMessageException{
	   MsgGetPlayerResponse msgGetPlayer = (MsgGetPlayerResponse) cliente1.serverMsg();
	   assert(msgGetPlayer.getActualWorld().equals(EXTERIOR));
	   Vector3f pos = new Vector3f();
	   pos.set(13000f,1.5f,7000f);
	   assert(msgGetPlayer.getPosition().equals(pos));
   }
   
    @AfterClass
    public void desconectar(){
    	if (cliente1.isConnected())
    		cliente1.logout();
    }
}
