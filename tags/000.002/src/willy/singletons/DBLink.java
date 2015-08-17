package willy.singletons;

import willy.server.sql.DBAccess;



public class DBLink {
	private static DBLink instance = null;
	
	private DBAccess db=null;
	protected long count=0;

	
	/**
	 *  soc protected, fuck you! (soc un singleton)
	 */
	protected DBLink() 
	{
		// fuck you!
	}

	public DBAccess getDBAccess() {
		return db;
	}

	public void setDBAccess(DBAccess db) {
		this.db = db;
	}

	public void newQuery() { this.count++; }
	public void resetQueryCounter() { this.count=0; }
	public long getQueryCounter() { return this.count; }
	
	public static DBLink getInstance() 
	{
		if(instance == null) instance = new DBLink();

		return instance;
	}

}