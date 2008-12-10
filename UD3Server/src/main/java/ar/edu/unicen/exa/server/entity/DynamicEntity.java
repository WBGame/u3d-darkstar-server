package server.entity;

import com.jme.math.Vector3f;
import com.sun.sgs.app.AppContext;

/** 
 * Ofrece funcionalidad a las entidades que tienen movimiento dentro del mundo.
 * 
 * @author Kopp Roberto <robertokopp at hotmail dot com/>
 * @encoding UTF-8 
 */
public class DynamicEntity extends Entity {
	
	/**  Para cumplir con la version de la clase Serializable. */
	
	private static final long serialVersionUID = 1L;

	/** 
	 *  Mundo donde se encuentra el jugador o personaje.
	 */
	
	private String actualWorld;

	/** 
	 *  Angulo donde se encuentra el jugador o personaje. 
	 */
	
	private Vector3f angle;

	/** 
	 *  Posicion donde se encuentra el jugador o personaje.
	 */
	
	private Vector3f position;

	/**
	 * @return actualWorld donde se encuentra el jugador
	 * 
	 */
	
	public final String getActualWorld() {
	
		return this.actualWorld;
	
	}

	/**
	 * @return Vector3f donde se encuentra el jugador
	 * 
	 */
	public final Vector3f getAngle() {

		return this.angle;
	}

	/**
	 * @return Vector3f donde se encuentra el jugador. 
	 * 
	 */
	public final Vector3f getPosition() {

		return this.position;
	
	}

	/**
	 * @param world donde se encuentra el jugador
	 * 
	 */
	public final void setActualWorld(final String world) {
		AppContext.getDataManager().markForUpdate(this);
		this.actualWorld = world;
	}

	/**
	 * Se setea el angulo donde se encuentra el jugador.
	 * @param jangle donde se encuentra el jugador
	 * 
	 */
	public final void setAngle(final Vector3f jangle) {
		AppContext.getDataManager().markForUpdate(this);
		this.angle = jangle;
	}

	/**
	 * Se setea la posicion donde se encuentra el jugador.
	 * @param jposition donde se encuentra el jugador
	 * 
	 */
	public final void setPosition(final Vector3f jposition) {
		AppContext.getDataManager().markForUpdate(this);
		this.position = jposition;      
	}
}
