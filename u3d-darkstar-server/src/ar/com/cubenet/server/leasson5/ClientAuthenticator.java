package ar.com.cubenet.server.leasson5;

import java.util.Properties;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.LoginException;

import com.sun.sgs.auth.Identity;
import com.sun.sgs.auth.IdentityAuthenticator;
import com.sun.sgs.auth.IdentityCredentials;
import com.sun.sgs.impl.auth.IdentityImpl;
import com.sun.sgs.impl.auth.NamePasswordCredentials;

public class ClientAuthenticator implements IdentityAuthenticator {

	/**
	 * Default constructor. 
	 */
	public ClientAuthenticator(Properties properties) {}

	/**
	 * Este metodo es invocado cuando el usuario se desea logearse en el 
	 * servidor con su nombre y contraseña.
	 * 
	 * @param credentials Represents credentials that can be used for 
	 *                    authentication.
	 * 
	 * @return Identity
	 * 
	 * @throws LoginException, AccountNotFoundException, CredentialException.
	 */
	public Identity authenticateIdentity(IdentityCredentials credentials) 
	throws LoginException, AccountNotFoundException, CredentialException {

		// Se asegura que el tipo de credencial es correcta.
		// Esta credencial esta implementada en el framework solo se hace uso 
		// de ella pero se puede crear nuevas credencials si es que hace falta. 
		if (!(credentials instanceof NamePasswordCredentials))
			throw new CredentialException("Credencial Desconocida");

		NamePasswordCredentials npc = (NamePasswordCredentials)credentials;

		// obtenemos el nombre que provino desde el cliente.
		String login = npc.getName();
		String password = new String(npc.getPassword());

		// le pido al modelo que valide el user y el password.
		boolean chk = ModelAccess.getInstance().checkUser(login, password);
		
		if( !chk ) {
			// si no pasa la validación lo notifico al cliente
			throw new AccountNotFoundException("Credencial Invalida " + login);
		}
		
		// si todo esta ok se devuelve la identidad con el nombre.
		// este name es el mismo que el de la sesion del usuario en el servidor.
		return new IdentityImpl(login);
	}

	public String[] getSupportedCredentialTypes() {
		return new String [] { NamePasswordCredentials.TYPE_IDENTIFIER };
	}

}
