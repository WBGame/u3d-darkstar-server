/**
 * 
 */
package server;

import com.sun.sgs.auth.IdentityAuthenticator;
import server.serverLogic.ModelAccess;
import com.sun.sgs.auth.IdentityCredentials;
import com.sun.sgs.auth.Identity;

/** 
 * @author esolis
 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LoginPasswordAuthenticator implements IdentityAuthenticator {
	/**
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LoginPasswordAuthenticator() {
		// begin-user-code
		// TODO Apéndice de constructor generado automáticamente
		// end-user-code
	}

	/**
	 * Este metodo es invocado cuando el usuario se desea logearse en el servidor con su nombre y contraseña.
	 * @param credentials 
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Identity authenticateIdentity(IdentityCredentials credentials) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * @return
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String[] getSupportedCredentialTypes() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
}