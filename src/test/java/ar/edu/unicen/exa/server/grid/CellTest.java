package ar.edu.unicen.exa.server.grid;

import java.awt.Rectangle;

import com.jme.math.Vector3f;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Este test testea la clase {@link Cell}.
 * @author Sebastián Perruolo
 *
 */
public class CellTest extends TestCase {
	/** limite inferior X. */
	private static final int BOUND_X = -10;
	/** limite inferior Y. */
	private static final int BOUND_Y = -10;
	/** Ancho de la celda. */
	private static final int BOUND_WIDTH = 100;
	/** Alto de la celda. */
	private static final int BOUND_HEIGHT = 100;
    /**
     * Create the test case.
     *
     * @param testName name of the test case
     */
    public CellTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CellTest.class);
    }

    /**
     * test.
     */
    public final void testCellIsInside() {
    	Rectangle cellBunds = new Rectangle(
    			BOUND_X, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT
    		);
		//IGridStructure parent = new MatrixGridStructure();
    	Cell cell = new Cell(cellBunds, null);
    	Vector3f positionInSide = new Vector3f(
    			Float.parseFloat("1"),
    			Float.parseFloat("1"),
    			Float.parseFloat("1")
    		);
    	assertTrue(cell.isInside(positionInSide));
    	Vector3f positionOutSide = new Vector3f(
    			Float.parseFloat("200"),
    			Float.parseFloat("1"),
    			Float.parseFloat("1")
    		);
    	assertFalse(cell.isInside(positionOutSide));
    	Vector3f positionBorder = new Vector3f(
    			BOUND_X,
    			BOUND_Y,
    			BOUND_Y * BOUND_WIDTH		//para demostrar que no se usa
    		);
    	assertTrue(cell.isInside(positionBorder));
    }
}

