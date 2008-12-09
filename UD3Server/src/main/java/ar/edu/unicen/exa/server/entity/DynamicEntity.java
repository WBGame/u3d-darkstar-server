package ar.edu.unicen.exa.server.entity;

import com.jme.math.Vector3f;
import com.sun.sgs.app.AppContext;

/** 
 * Ofrece funcionalidad a las entidades que tienen movimiento dentro del mundo.
 * 
 * @author Kopp Roberto <robertokopp at hotmail dot com>
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
	 * @param angle donde se encuentra el jugador
	 * 
	 */
	public final void setAngle(final Vector3f angle) {
		AppContext.getDataManager().markForUpdate(this);
		this.angle = angle;
	}

	/**
	 * @param position donde se encuentra el jugador
	 * 
	 */
	public final void setPosition(final Vector3f position) {
		AppContext.getDataManager().markForUpdate(this);
		this.position = position;      
	}
}