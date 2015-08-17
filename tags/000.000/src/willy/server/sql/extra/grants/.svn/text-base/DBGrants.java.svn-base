package willy.server.sql.extra.grants;

import java.sql.SQLException;
import java.util.Vector;

import willy.server.sql.DBAccess;


public abstract class DBGrants {
	/**
	 * si timestamp (DBVariables) es 0, ignorar valors de variables
	 */
	protected long timestamp;
	
	/**
	 * @return true si la classe conté informacio vàlida
	 */
	public boolean isValid()
	{
		return timestamp!=0;
	}
	
	public abstract void addGrants(DBAccess db) throws SQLException;
	public abstract Vector<DBUser> getByPassword(String password);
	public abstract Vector<DBUser> getInvalidUsers();
	public abstract Vector<DBUser> getWeakPasswords();
}
