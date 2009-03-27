package ar.edu.unicen.exa.server.entity;

import com.jme.math.Vector3f;
import com.sun.sgs.app.AppContext;

/** 
 * Ofrece funcionalidad a las entidades que tienen movimiento dentro del mundo
 * ({@link IGridStructure}).
 * 
 * @author Kopp Roberto &lt;robertokopp at hotmail dot com&gt;
 * @encoding UTF-8 
 */
public class DynamicEntity extends Entity {

	/**  Para cumplir con la version de la clase {@Serializable}. */

	private static final long serialVersionUID = 1L;

	/** 
	 *  Mundo ({@link IGridStructure}) donde se encuentra el {@link Player}.
	 */

	private String actualWorld = null;

	/** 
	 *  Angulo donde se encuentra el {@link Player}. 
	 */

	private Vector3f angle = null;

	/** 
	 *  Posicion donde se encuentra el {@link Player}.
	 */

	private Vector3f position = null;

	/**
	 *  Recuperacion del mundo ({@link IGridStructure})actual.
	 * 
	 *  @return String Con el id del mundo actual donde  se encuentra la 
	 *  entidad o null en caso de no haber sido inicializado.
	 *         
	 *  @see GridManager#getStructure(String) permite recuperar el mundo dado 
	 *       este identificador.
	 */
	public final String getActualWorld() {
		return this.actualWorld;
	}

	/**
	 * @return Vector3f Angulo donde se encuentra el {@link Player}.
	 * 
	 */
	public final Vector3f getAngle() {
		return this.angle;
	}

	/**
	 * @return Vector3f Posision donde se encuentra el {@link Player}. 
	 * 
	 */
	public final Vector3f getPosition() {

		return this.position;

	}

	/**
	 * @param world Mundo({@link IGridStructure}) donde se encuentra el {@link
	 * Player}.
	 * 
	 */
	public final void setActualWorld(final String world) {
		AppContext.getDataManager().markForUpdate(this);
		this.actualWorld = world;
	}

	/**
	 * Se setea el angulo donde se encuentra el {@link Player}.
	 * @param jangle donde se encuentra el {@link Player}.
	 * 
	 */
	public final void setAngle(final Vector3f jangle) {
		AppContext.getDataManager().markForUpdate(this);
		this.angle = jangle;
	}

	/**
	 * 
	 * Se setea la posicion donde se encuentra el {@link Player}.
	 * @param jposition Posicion donde se encuentra el {@link Player}.
	 * 
	 */
	public final void setPosition(final Vector3f jposition) {
		AppContext.getDataManager().markForUpdate(this);
		this.position = jposition;      
	}
}
