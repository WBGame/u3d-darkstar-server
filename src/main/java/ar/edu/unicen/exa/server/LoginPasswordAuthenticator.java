package ar.edu.unicen.exa.server;

import java.util.Properties;
import java.util.logging.Logger;

import javax.security.auth.login.CredentialException;

//import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.serverLogic.ModelAccess;

import com.sun.sgs.auth.IdentityAuthenticator;
import com.sun.sgs.auth.IdentityCredentials;
import com.sun.sgs.auth.Identity;
import com.sun.sgs.impl.auth.IdentityImpl;
import com.sun.sgs.impl.auth.NamePasswordCredentials;

/**
 * Autentificador de usuarios. 
 * Para utilizar el autenticador, el cliente 
 * debe implementar el metodo {@link #getPasswordAuthentication()} para 
 * obtener el nombre de usuario y contraseña a travez de la credencial
 * que se utiliza para obtener dicha información. Esta clase permite 
 * validar un usuario a partir de sus datos ingresados contra la 
 * informacion correspondiente por medio del metodo {@link #checkPlayer()}
 * de la clase {@link ModelAccess}.
 * 
 * @author Pablo Inchausti &lt;inchausti.pablo at gmail dot com&gt;
 * @encoding UTF-8.
 */
public final class LoginPasswordAuthenticator implements IdentityAuthenticator {

	/** Creamos un logger para esta clase. */
	private static Logger logger =
		Logger.getLogger(LoginPasswordAuthenticator.class.getName());

	/**
	 * Constructor para esta clase. Es necesario aunque no contenga codigo,
	 * debido a que Darkstar lo requiere para la creacion de esta clase.
	 * @param properties Propiedades del auntenticador.
	 */
	public LoginPasswordAuthenticator(final Properties properties) { }

	/**
	 * Este metodo es invocado cuando el usuario desea logearse al 
	 * servidor. Se autentica por medio de una credencial provista por
	 * Darkstar y a apartir de esta se obtiene el nombre y el password
	 * que seran dados al metodo {@link #checkPlayer()} para que este
	 * verifique si dicha informacion es valida para autentificarse. 
	 * 
	 * @param credentials para autentificacion. 
	 * @return Identity asosiada al {@link Player}.
	 * @throws CredentialException si la credencial no es valida.
	 */
	public Identity authenticateIdentity(final IdentityCredentials credentials) 
	throws CredentialException {

		if (!(credentials instanceof NamePasswordCredentials)) {
			throw new CredentialException("Credencial desconocida");
		}

		NamePasswordCredentials npc = (NamePasswordCredentials) credentials;

		// Se obtiene el password y nombre que ingreso el usuario. 
		String name = npc.getName();
		char[] passaux = npc.getPassword();
		String password = new String(passaux);

		// Verificar si la informacion ingresada es valida, con la 
		// que esta registrada en el ModelAccess. 
		boolean isValid = ModelAccess.getInstance().checkPlayer(password, name);

		// Si no es valida se rechaza el ingreso al usuario.
		if (!isValid) {
			throw new CredentialException(
			"El nombre de usuario o contraseña no es válida");
		}

		logger.info("Es válida la autenticación para el usuario " 
				+ name + " con password " + password);

		return new IdentityImpl(name);
	}

	/**
	 * Getter.
	 * 
	 * @return identificadores para los tipos de credenciales soportados
	 * por esta clase.
	 */
	
	public String[] getSupportedCredentialTypes() {
		return new String [] { NamePasswordCredentials.TYPE_IDENTIFIER };
	}
}
