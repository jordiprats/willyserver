package willy.server.sql.extra;

import willy.server.sql.extra.generics.DBState;;

public class MySQLState extends DBState{
	
	protected DBProcessList dbpl=null;
	protected MySQLStatus dbs=null;
	protected MySQLVariables dbv=null;
	
	public MySQLState(DBProcessList dbpl, MySQLStatus dbs, MySQLVariables dbv)
	{
		this.dbpl=dbpl;
		this.dbs=dbs;
		this.dbv=dbv;
	}
	
	public DBProcessList getDBProcessList() {
		return dbpl;
	}
	public void setDBProcessList(DBProcessList dbpl) {
		this.dbpl = dbpl;
	}
	public MySQLStatus getMySQLStatus() {
		return dbs;
	}
	public void setMySQLStatus(MySQLStatus dbs) {
		this.dbs = dbs;
	}
	public MySQLVariables getMySQLVariables() {
		return dbv;
	}
	public void setMySQLVariables(MySQLVariables dbv) {
		this.dbv = dbv;
	}

}
