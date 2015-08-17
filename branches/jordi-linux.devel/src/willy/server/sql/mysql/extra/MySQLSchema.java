package willy.server.sql.mysql.extra;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSchema {
	
	protected String catalog_name=null;
	protected String schema_name=null;
	protected String default_character_set_name=null;
	protected String default_collation_name=null;
	protected String sql_path=null;
	
	public String getSchemaName() { return this.schema_name; }
	
	public MySQLSchema(ResultSet rs) throws SQLException
	{
		//CATALOG_NAME, SCHEMA_NAME, DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME, SQL_PATH
		//      1           2                    3                       4                   5
		
		this.catalog_name               = rs.getString(1);
		this.schema_name                = rs.getString(2);
		this.default_character_set_name = rs.getString(3);
		this.default_collation_name     = rs.getString(4);
		this.sql_path                   = rs.getString(5);
		
	}

}
