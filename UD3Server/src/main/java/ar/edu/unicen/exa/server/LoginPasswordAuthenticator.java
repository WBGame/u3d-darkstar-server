package ar.edu.unicen.exa.server;

import java.util.Properties;
import java.util.logging.Logger;

import javax.security.auth.login.CredentialException;

import ar.edu.unicen.exa.server.serverLogic.ModelAccess;

import com.sun.sgs.auth.IdentityAuthenticator;
import com.sun.sgs.auth.IdentityCredentials;
import com.sun.sgs.auth.Identity;
import com.sun.sgs.impl.auth.IdentityImpl;
import com.sun.sgs.impl.auth.NamePasswordCredentials;

/**
 * Autentificador de usuarios. Para utilizar el autenticador, el cliente 
 * debe implementar el método {@link #getPasswordAuthentication()} para 
 * obtener el nombre de usuario y contraseña a travez de la credencial
 * que se utiliza para obtener dicha información. Esta clase permite 
 * validar un usuario a partir de sus datos ingresados contra la 
 * informacion correspondiente por medio del método {@link #checkPlayer()}
 * de la clase {@link ModelAccess}.
 * 
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
 * @encoding UTF-8
 */
public final class LoginPasswordAuthenticator implements IdentityAuthenticator {

	/** Creamos un logger para esta clase. */
	private final Logger logger =
		Logger.getLogger(LoginPasswordAuthenticator.class.getName());

	/**
	 * Constructor para esta clase. Es necesario aunque no contenga código
	 * debido a que Darkstar lo requiere para la creación de esta clase.
	 * @param properties propiedades del auntenticador.
	 */
	public LoginPasswordAuthenticator(final Properties properties) { }

	/**
	 * Este método es invocado cuando el usuario desea logearse al 
	 * servidor. Se autentica por medio de una credencial provista por
	 * Darkstar y a apartir de esta se obtiene el nombre y el password
	 * que serán dados al método {@link #checkPlayer()} para que éste
	 * verifique si dicha información es válida para autentificarse. 
	 * 
	 * @param credentials para autentificación 
	 * @return identidad del player 
	 * @throws CredentialException si la credencial no es válida
	 */
	public Identity authenticateIdentity(
			final IdentityCredentials credentials) 
				throws CredentialException {

		if (!(credentials instanceof NamePasswordCredentials)) {
			throw new CredentialException("Credencial desconocida");
		}

		NamePasswordCredentials npc = (NamePasswordCredentials) credentials;
		
		// se obtiene el password y nombre que ingresó el usuario. 
		
		String name = npc.getName();
		char[] passaux = npc.getPassword();
		String password = new String(passaux);

		// verificar si la información ingresada es válida con la 
		// registrada en el ModelAccess. 
		boolean isValid = ModelAccess.getInstance().checkPlayer(password, name);

		// si no es válida se rechaza el ingreso al usuario.
		if (!isValid) {
			throw new CredentialException(
					"El nombre de usuario o contraseña no es válida");
		}
		
		logger.info("Es válida la autenticación para el usuario " 
				+ name + " con password " + password);
		
		return new IdentityImpl(name);
	}

	/**
	 * @return los identificadores para los tipos de credenciales soportados
	 * por esta clase.
	 */

	public String[] getSupportedCredentialTypes() {
		return new String [] { NamePasswordCredentials.TYPE_IDENTIFIER };
	}
}
