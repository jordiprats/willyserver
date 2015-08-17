package willy.server.sql.extra;

import java.sql.ResultSet;
import java.sql.SQLException;

import willy.server.sql.mysql.engines.InnoDBTransaction;

public class DBProcessItem {
	
	protected String id=null;
	protected String user=null;
	protected String host=null;
	protected String db=null;
	protected String command=null;
	protected String time=null;
	protected String state=null;
	protected String info=null;
	
	//Això ja ho estem enmerdant amb mysql!!!
	protected InnoDBTransaction idbt=null;
	public void setInnoDBTransaction(InnoDBTransaction idbt) { this.idbt=idbt; }
	public InnoDBTransaction getInnoDBTransaction() { return this.idbt; }
	
	protected boolean locked=false;
	
	public boolean isLocked() {
		if(this.locked)
			return locked;
		else
			return this.idbt.isLocked();
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * Processlist que pertany aquest item
	 */
	protected DBProcessList dbpl=null;
	
	/**
	 * timestamp recolecciÃ³ dades
	 */
	protected long timestamp=0;

	public DBProcessItem(ResultSet rs, long timestamp, DBProcessList dbpl) throws SQLException
	{
		//algun dia: identificar motor DB avans d'agafar camps a lo bruto
		
		this.id=rs.getString(1);
		this.user=rs.getString(2);
		this.host=rs.getString(3);
		this.db=rs.getString(4);
		
		// http://dev.mysql.com/doc/refman/5.1/en/thread-commands.html
		this.command=rs.getString(5);
		this.time=rs.getString(6);
		this.state=rs.getString(7);
		this.info=rs.getString(8);
		
		this.timestamp=timestamp;
	}
	
	public DBProcessList getProcessList()
	{
		return this.dbpl;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public String getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public String getHost() {
		return host;
	}
	public String getDb() {
		return db;
	}
	public String getCommand() {
		return command;
	}
	public String getTime() {
		return time;
	}
	public String getState() {
		return state;
	}
	public String getInfo() {
		return info;
	}
	
	public String toString()
	{
		return "user=>"+this.user+
		",timestamp=>"+this.timestamp;
		//",query=>"+this.info;
	}
}
