package willy.server.sql.mysql.extra;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLEngine {
	
//	+---------+---------+-----------+--------------+------+------------+
//	| Engine  | Support | Comment   | Transactions | XA   | Savepoints |
//	+---------+---------+-----------+--------------+------+------------+
//       1         2         3              4         5          6	
	protected String engine=null;
	protected String support=null;
	protected String comment=null;
	protected String transactions=null;
	protected String xa=null;
	protected String savepoints=null;
	
	public String getEngineName() { return this.engine.toLowerCase(); }
	
	public MySQLEngine(ResultSet rs) throws SQLException
	{
		this.engine       = rs.getString(1);
		this.support      = rs.getString(2);
		this.comment      = rs.getString(3);
		this.transactions = rs.getString(4);
		this.xa           = rs.getString(5);
		this.savepoints   = rs.getString(6);
	}

}
