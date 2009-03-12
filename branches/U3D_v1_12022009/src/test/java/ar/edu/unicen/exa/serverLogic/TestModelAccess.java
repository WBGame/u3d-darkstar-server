package ar.edu.unicen.exa.serverLogic;

import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;

import common.datatypes.PlayerStat;
import common.datatypes.Quest;
import common.datatypes.Ranking;
/**
 * Testeo de ModelAccess con la DB, tanto para escritura como lectura
 * @author Juan M. Arguindegui
 * @encoding UTF-8   
 */


public class TestModelAccess {
	ModelAccess ma;
	String name;
	String password;
	
//	Obtengo una nueva instancia del ModelAccess 
	@BeforeClass	
    public void setUp(){
    	ma= ModelAccess.getInstance();  
	    name="pepe";
	    password="pepe";
    }
	
	
	//Chequeo el player player=Pepe Contraseña=pepe
    @Test(groups = {"Player"})
    public void TestCheckPlayer() {
    	
    	  	// Chequeo la existencia del usuario para con las base de datos
    	    ma.checkPlayer(name, password);
	
    	  	// obtengo los stats de ese player sabiendo que en la base son 3 
    	    Set<PlayerStat> set = ma.getPlayerStats(name);
    	  	// cuenta los stats del player en playerStat en este caso 3
    	  	assert (set.size()==3);
    	  	
            // 	  Cuenta los juegos disponibles para ese player en este caso 4
    	  	Set<String> set2 = ma.getAvailableGames(name);
    	  	assert (set2.size()==4);
    	  	
    	  	// obtengo los quets disponible para ese player
    	  	Set<Quest> set3 = ma.getAvailableQuests(name);
    	  	// sabiendo que para este player es 1
	  	     assert (set3.size()==1);
    	  	
	  	     
	 	
    	  	
    	  	
    
    }
    
    
    
    // los juegos disponibles para el player
    // marcar como comprado el juego pasado como parametro
    // los juegos que se pueden comprar
    
   @Test(groups = {"Propiedades"})
    public void Testpropiedades(){
	   //prueba escribir y leer el puntaje global de un jugador
	   ma.setGlobalScore(name, 155);
	   
	   assert (ma.getGlobalScore(name)==155);
	   
	   
	   //Prueba escribir y leer la plata del jugador
	   
	   ma.setMoney(name, 700);
	   assert(ma.getMoney(name)==700);
	   
	   
        }
    
   @Test(groups= {"Quest"})
   public void TestQuest(){
	   
	  // Al player pepe le queda 1 quest disponible que es la 4
	  Set<Quest> q1=ma.getAvailableQuests(name);
      assert (q1.size()==1);
      
      // cuantas quest tiene corriendo en este caso es 1 la numero 3
      Set<Quest> q2=ma.getCurrentQuests(name);
      assert (q2.size()==1);
	  
      
      // probado pero ya esta no se puede sacar de nuevo tira execption
	  //ma.finishQuest(name, "3");
	  
      // quita la quest
	  ma.abortQuest(name, "3");
	  
	  // la vuelve agregar para mantener el estado
	 ma.startQuest(name, "3");
	 
	 // solo cambia los valores de los estados
	 ma.nextQuestState(name, "3","1");
	 // vuelve al que tenia por defecto en la base
	 ma.nextQuestState(name, "3","0");
	 
	  
   }
   
   // lista de juegos comprables
   // Tiempo que se jugo en 1 juego
   // Rankig en 1 juego
   //el precio de 1 juego
   @Test(groups= {"Varios"})
   public void varios(){
	   // que en nuestro caso son 5 del 0 al 4
	   Set<String> s1= ma.getBuyables2DGames();
	   assert(s1.size()==5);
	   
	   
	   //cuenta las veces que el jugador en este caso pepe jugo al juego 0 que
	   // son 28 para nuestro caso
	   assert(ma.getPlayedTimes("0", name)==28);
	   
	   
	   //el valor de un minijuego
	   // el 1 vale 75
	   assert(ma.get2DGamePrice("1")==75);
	   
	   //ranking de 1 minujuego
	   Ranking r1=ma.getRanking("0");
	   String[] s2=r1.getIdPlayers();
	   int[] s3=r1.getScores();
	   
	   assert (s2.length==s3.length);
	   // imprimo el ranking y compruebo si es el mismo largo
	   System.out.println("a="+ s2[0]);
	   System.out.println("a="+ s3[0]);
	   System.out.println("a="+ s2[1]);
	   System.out.println("a="+ s3[1]);
	   System.out.println("a="+ s2[2]);
	   System.out.println("a="+ s3[2]);
	   
	   System.out.println("length: " + s2.length);
	   System.out.println("length: " + s3.length); 
	   
   }
    

}
