package willy.server.sql.extra;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import willy.server.sql.extra.generics.DBStatus;

//http://www.mysqlperformanceblog.com/2012/08/29/heres-a-quick-way-to-foresee-if-replication-slave-is-ever-going-to-catch-up-and-when/

public class MySQLSlaveStatus extends DBStatus{
	
	protected long sbm=0;
	protected boolean replicacio_activa=false;
	
	public MySQLSlaveStatus(ResultSet rs) throws SQLException
	{
		super.timestamp = System.currentTimeMillis()/1000;
		
		//es una sola row amb moltes columnes
		//mysql> show slave status\G
		//*************************** 1. row ***************************

		if(rs.next())
		{
			ResultSetMetaData md = rs.getMetaData();

			for (int i = 1; i <= md.getColumnCount(); i++)
			{
				String key=md.getColumnName(i);
				String value=rs.getString(i);

				this.getSBM(key, value);
				//System.out.println(key+"=>"+value);
			}
		}
	}

	private void getSBM(String key, String value)
	{
		if(key.compareTo("Seconds_Behind_Master")==0)
		{
			if(value.compareTo("NULL")==0)
			{
				this.sbm=0;
				this.replicacio_activa=false;
			}
			else
			{
				this.sbm=Long.parseLong(value);
				this.replicacio_activa=true;
			}
		}
	}
	
}
