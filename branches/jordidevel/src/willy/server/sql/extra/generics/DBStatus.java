package willy.server.sql.extra.generics;

public abstract class DBStatus {

	/**
	 * si timestamp (DBVariables) es 0, ignorar valors de variables
	 */
	protected long timestamp;
	
	public long getTimestamp() { return this.timestamp; }
	
	/**
	 * @return true si la classe conté informacio vàlida
	 */
	public boolean isValid()
	{
		return timestamp!=0;
	}
	
}
