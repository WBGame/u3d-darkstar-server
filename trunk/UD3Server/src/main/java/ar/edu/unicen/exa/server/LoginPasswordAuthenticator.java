package ar.edu.unicen.exa.server;

import java.util.Properties;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import ar.edu.unicen.exa.server.serverLogic.*;
//import ar.edu.unicen.exa.server.player.Player;

//import com.sun.sgs.app.AppContext;
//import com.sun.sgs.app.DataManager;
import com.sun.sgs.auth.IdentityAuthenticator;

import com.sun.sgs.auth.IdentityCredentials;
import com.sun.sgs.auth.Identity;
import com.sun.sgs.impl.auth.IdentityImpl;
import com.sun.sgs.impl.auth.NamePasswordCredentials;

/**
 * Autentificador de jugadores. Para utilizarla, el cliente debe tener 
 * implementado el metodo {@link #getPasswordAuthentication()} (ya que 
 * es necesario conocer el nombre de usuario y su password). Esta clase 
 * permite validar un jugador a partir de sus datos ingresados contra la 
 * informacion correspondiente a dicho jugador por medio del metodo 
 * {@link #checkPlayer()}.
 * 
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com>
 * @encoding UTF-8
 * 
 * TODO review String about password for security reasons.
 * TODO document throw new LoginException(); decision.
 */
public class LoginPasswordAuthenticator implements IdentityAuthenticator {

	/** Creamos un logger para esta clase. */
	private static final Logger logger =
		Logger.getLogger(LoginPasswordAuthenticator.class.getName());

	/**
	 * Constructor
	 * @param properties propiedades del auntenticador.
	 */
	public LoginPasswordAuthenticator(final Properties properties) {}

	/**
	 * Este metodo es invocado cuando el usuario se desea logearse en el 
	 * servidor con su nombre y contraseña. Authentica por medio de una 
	 * credencial implementada y provista por Darkstar. Solo se hace uso 
	 * de ella pero se pueden crear nuevas credencials si es ncesesario.
	 * 
	 * @param credentials para autentificacion 
	 * @return identidad del player 
	 * @throws LoginException rechazo del password
	 */
	public final Identity authenticateIdentity(
			final IdentityCredentials credentials) throws LoginException {

		if (!(credentials instanceof NamePasswordCredentials)) {
			throw new LoginException();
		}

		NamePasswordCredentials npc = (NamePasswordCredentials) credentials;
		//Obtengo el password y el nombre que ingresó el usuario. 
		String name = npc.getName();
		char[] passaux = npc.getPassword();
		String password = new String(passaux);

		logger.info( "logeando usuario: " + name + ' ' + password);

		//Verifico si la información del jugador es correcta con la base de 
		//datos.
		boolean isValid = ModelAccess.getInstance().checkPlayer(password, name);

		if (!isValid) {
			throw new LoginException();
		}

//		boolean isConnected = false;

		//Se chequea un jugador no pueda logearse si el mismo ya se encuentra
		//logeado. Esto es por razones de seguridad y para que uno o mas 
		//jugadores no esten simultaneamente logeados en el sistema. 
//		DataManager dataMgr = null;
//
//		try {
//			dataMgr = AppContext.getDataManager();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			//throw new LoginException(); 
//		}
//
//		try {
//			logger.info( "Nombre de identificacion: " + name );
//			Player player = (Player) dataMgr.getBinding(name);
//			isConnected = player.getSession().isConnected();
//		} catch (Exception e) {
//			isConnected = false;
//		}
//
//		if (isConnected) {
//			throw new LoginException();
//		}

		return new IdentityImpl(name);
	}

	/**
	 * @return los identificadores para los tipos de credenciales soportados
	 */

	public final String[] getSupportedCredentialTypes() {
		return new String [] { NamePasswordCredentials.TYPE_IDENTIFIER };
	}
}