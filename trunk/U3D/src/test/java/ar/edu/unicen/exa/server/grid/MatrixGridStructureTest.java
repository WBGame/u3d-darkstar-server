/**
 * 
 */
package ar.edu.unicen.exa.server.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.jme.math.Vector3f;

/**
 * @author Sebastián Perruolo
 * 
 */
public class MatrixGridStructureTest {
	/** Instancia de  MatrixGridStructure. */
	private MatrixGridStructure instance = null;
	/** Instancia de  Cell. */
	private Cell theCell = null;
	
	/** limite inferior X. */
	@SuppressWarnings("unused")
	private static final int BOUND_X = -10;
	/** limite inferior Y. */
	@SuppressWarnings("unused")
	private static final int BOUND_Y = -10;
	/** Ancho de la celda. */
	@SuppressWarnings("unused")
	private static final int BOUND_WIDTH = 100;
	/** Alto de la celda. */
	@SuppressWarnings("unused")
	private static final int BOUND_HEIGHT = 100;
	
	
	/** Variables para poder cumplir con checkstyle. */
	/** Ancho de la grilla. */
	private static final int X = 1;
	/** Alto de la grilla. */
	private static final int Y = 1;
	/** Tamaño de la grilla. */
	private static final int Z = 100;
	
	/**
	 * Crea una única instancia de MatrixGridStructure para los propósitos
	 * del test.
	 * 
	 * @return úna instancia de MatrixGridStructure
	 */
	private MatrixGridStructure getInstance() {
		if (instance == null) {
			// X = 1, Y = 1, Z = 100.
			instance = new MatrixGridStructure(X, Y, Z);
		}
		return instance;
	}
	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#setIdWorld(java.lang.String)}
	 * .
	 */
	@Test
	public final void testSetIdWorld() {
		getInstance().setIdWorld("1");
		assertEquals(instance.getIdWorld(), 1);
	}

	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#getIdWorld()}.
	 */
	@Test
	public final void testGetIdWorld() {
		getInstance().setIdWorld("1");
		assertEquals(instance.getIdWorld(), 1);
	}

	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#getCell(com.jme.math.Vector3f)}
	 * .
	 */
	@Test
	public final void testGetCell() {
    	Vector3f positionInSide = new Vector3f(
    			Float.parseFloat("1"),
    			Float.parseFloat("1"),
    			Float.parseFloat("1")
    		);
    	assertEquals(getInstance().getCell(positionInSide), theCell);
    	
    	Vector3f positionOutSide = new Vector3f(
    			Float.parseFloat("200"),
    			Float.parseFloat("1"),
    			Float.parseFloat("1")
    		);
    	assertNull(getInstance().getCell(positionOutSide));
	}

	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#getAdjacents(
	 * 			ar.edu.unicen.exa.server.grid.Cell, 
	 * 			com.jme.math.Vector3f)
	 * 		}
	 * .
	 */
	@Test
	public final void testGetAdjacents() {
		//TODO implementar!
	}

}
