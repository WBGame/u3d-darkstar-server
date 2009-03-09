package ar.edu.unicen.exa.serverLogic;

import org.testng.annotations.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import common.datatypes.D2GameScore;
import common.datatypes.PlayerStat;
import common.datatypes.Quest;
import common.datatypes.Ranking;
import ar.edu.unicen.exa.server.serverLogic.ModelAccess;

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
    	
    	  	ma.checkPlayer(name, password);
	
    	  	Set<PlayerStat> set = ma.getPlayerStats(name);
    	  	
    	  	// cuenta los stats del player en playerStat en este caso 3
    	  	assert (set.size()==3);
    	  	
            // 	  Cuenta los juegos disponibles para ese player en este caso 4
    	  	Set<String> set2 = ma.getAvailableGames(name);
    	  	assert (set2.size()==4);
    	  	
    	  	Set<Quest> set3 = ma.getAvailableQuests(name);
    	  	
    	  	
    	  	
    
    }
    
    
    
    
    

}
