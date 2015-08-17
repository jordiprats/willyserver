package willy.server.sql.extra;

import willy.server.sql.extra.generics.DBState;
import willy.server.sql.extra.grants.DBGrants;
import willy.server.sql.mysql.MySQLTables;

public class MySQLState extends DBState{
	
	protected DBProcessList dbpl=null;
	protected MySQLStatus dbs=null;
	protected MySQLVariables dbv=null;
	protected DBGrants grants=null;
	protected MySQLTables dbitems=null;
	//TODO: show engine innodb status;
	
	/**
	 * @param dbpl
	 * @param dbs
	 * @param dbv
	 * @param grants
	 */
	public MySQLState(DBProcessList dbpl, MySQLStatus dbs, 
						MySQLVariables dbv, DBGrants grants)
	{
		this.dbpl=dbpl;
		this.dbs=dbs;
		this.dbv=dbv;
		this.grants=grants;
	}
	
	public MySQLTables getDBItems()
	{
		return this.dbitems;
	}
	
	public void setDBItems(MySQLTables dbitems)
	{
		this.dbitems=dbitems;
	}
	
	public DBGrants getDBGrants() {
		return grants;
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
