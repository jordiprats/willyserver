package willy.server.sql;

import java.sql.ResultSet;

import willy.server.sql.extra.*;
import willy.server.sql.extra.generics.DBStatus;
import willy.server.sql.extra.generics.DBVariables;

public abstract class DBAccess {
	
    protected DBParam dbp=null;
	
    /*
	public abstract void setParam(DBParam dbp);
	public abstract DBParam getParam();
	*/
    public abstract boolean ping();
	
	public abstract DBProcessList getProcessList();
	public abstract DBVariables getVariables();
	public abstract DBStatus getGlobalStatus();
	public abstract DBStatus getSessionStatus();
	public abstract DBStatus getSlaveStatus();
	
	public abstract String getVersion();
	public abstract String getType();
	public abstract DBAccess newConnection();
	
	public abstract ResultSet getGrants();
	
	public abstract void close();

    public void setParam(DBParam dbp)
    {
    	this.dbp=dbp;
    }
    
    public DBParam getParam()
    {
    	return dbp;
    }
	
}
