package ar.edu.unicen.exa.server.entity;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import java.io.Serializable;

/** 
 * Almacena un identificador unico para cada una de las entidades.
 * 
 * @author Kopp Roberto &lt;robertokopp at hotmail dot com&gt;
 * @encoding UTF-8
 */
public class Entity implements ManagedObject, Serializable {
	
	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;
	
	/** Identificador de la entidad. */
	private String idEntity;

	/**
	 * Se obtiene el identificador de una entidad.
	 * @return idEntity el id de la entidad.
	 * 
	 */
	public final String getIdEntity() {
		return this.idEntity;
	}

	/**
	 * Se setea el id de la entidad.
	 * @param entity el id de la entidad.
	 * 
	 */
	
	public final void setIdEntity(final String entity) {
		AppContext.getDataManager().markForUpdate(this);
		idEntity = entity;
	}
}
