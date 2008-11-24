package ar.com.cubenet.server.leasson5;

/**
 * ModelAccess facade like the real design. I put this to show a real example.
 * 
 * @author Cabrera Emilio Facundo <cabrerafacundo at gmail dot com>
 */
public class ModelAccess {

	/**
	 * ModelAccess unique instance. 
	 */
	private static ModelAccess instance = null;
	
	/**
	 * Default constructor. 
	 */
	private ModelAccess() {};
	
	/**
	 * Get a instance of this class for use. The only way for use this class is
	 * using this method. 
	 */
	public static ModelAccess getInstance() {
		if( instance == null )
			instance = new ModelAccess();
		return instance;
	}
	
	/**
	 * Dummy implementation of authentication. For now login & pass must be the
	 * same for login into de the system.
	 * 
	 * @param login user login to check
	 * @param pass  user password to check too
	 * 
	 * @return true if login & pass are asociated into the data model.
	 */
	public boolean checkUser( String login, String pass ) {
		return login.equals(pass);
	}
	
}
