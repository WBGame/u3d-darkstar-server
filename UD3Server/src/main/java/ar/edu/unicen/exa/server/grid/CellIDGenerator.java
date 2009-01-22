/**
 * Project Looking Glass
 *
 * $RCSfile: CellIDGenerator.java,v $
 *
 * Copyright (c) 2004-2007, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.1 $
 * $Date: 2007/07/18 18:45:06 $
 * $State: Exp $
 */
package ar.edu.unicen.exa.server.grid;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.NameNotBoundException;
import java.io.Serializable;

/**
 * Clase que genera id's unicos para cada celda. 
 * 
 * @author Pablo Inchausti
 */
public class CellIDGenerator implements ManagedObject, Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private static CellIDGenerator tileIDgen = null;
	
	/**
     * Creates a new instance of CellIDGenerator
     */

    private CellIDGenerator(long initial) {
    	nextID = initial;
    }

    /**
     * Holds value of property nextID.
     */
    private static long nextID;
    

    /**
     * Obtiene el proximo id unico para una celda
     * 
     * @return String nuevo id para la celda
     */
    public static String getNextID() {

    	DataManager dataMgr = AppContext.getDataManager();
    	
    	if(tileIDgen ==  null){
	        
	        String name = CellIDGenerator.class.getSimpleName();
	        
	        try {
	            tileIDgen = (CellIDGenerator) dataMgr.getBinding(name);
	        } catch(NameNotBoundException ex) {
	            tileIDgen = new CellIDGenerator(0L);
		    dataMgr.setBinding(name, tileIDgen );            
	        }
    	}
        dataMgr.markForUpdate(tileIDgen);
        
        tileIDgen.nextID();
   
        return "Cell_" + String.valueOf(nextID);
    }
    
	private long nextID() {
		nextID++;
		return nextID; 
	}
}
