package ar.edu.unicen.exa.server.grid;

import java.awt.Rectangle;

import org.testng.annotations.*;

import ar.edu.unicen.exa.server.grid.Cell;

import com.jme.math.Vector3f;

import net.java.dev.mocksgs.*;


public class CellTestNG {
	
	/** limite inferior X. */
	private static final int BOUND_X = -10;
	/** limite inferior Y. */
	private static final int BOUND_Y = -10;
	/** Ancho de la celda. */
	private static final int BOUND_WIDTH = 100;
	/** Alto de la celda. */
	private static final int BOUND_HEIGHT = 100;
	/** Variable para los testeos*/
	Cell celda;
	Cell celda1;
	Cell celda2;
	
	
	Rectangle cellBunds = new Rectangle(
			BOUND_X, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT
	);
	
	@BeforeClass
	public void setUp() throws Exception {
		 /** inicializo mocking */
		 MockSGS.init();
	     celda=new Cell(cellBunds,null);
	     celda1=new Cell(cellBunds,null);
	     celda2=new Cell(cellBunds,null);
	}
	
	@Test (groups = { "TestCellMethod" })
	public void getIdTest  (){
		assert(celda.getId()==0);
	}
	
	@Test (groups = { "TestCellMethod" })
	public void getChannelTest(){
		assert(celda.getChannel()!=null);
	}	

	@Test (groups = { "TestCellMethod" })
		public void getBounds(){
		Rectangle rect = celda.getBounds(); 
		assert(rect.x==BOUND_X && rect.y==BOUND_Y && 
				rect.height==BOUND_HEIGHT && rect.width==BOUND_WIDTH);	
	}
	
	@Test (groups = { "TestCellMethod" })
	public void isInsideTest(){
		Vector3f positionInSide = new Vector3f(
    			BOUND_X + BOUND_WIDTH / 2,
    			BOUND_Y + BOUND_HEIGHT / 2,
    			1);
		Vector3f positionOutSide = new Vector3f(
    			BOUND_X + BOUND_WIDTH * 2,
    			BOUND_Y + BOUND_HEIGHT * 2,
    			1);
		assert(celda.isInside(positionInSide)==true &&
				celda.isInside(positionOutSide)==false);
	}
	
	@Test (groups = { "TestCellMethod" })
	public void equals(){
		//igual id que celda para probar metodo
		celda1.setId(celda.getId());
		//el id de celda2 es distinto por la forma en que esta implementado el
		//constructor.
		assert(celda1.equals(celda)&& !celda2.equals(celda));
	}
}
