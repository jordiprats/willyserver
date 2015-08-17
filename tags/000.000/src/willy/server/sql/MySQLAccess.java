package willy.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import willy.server.sql.extra.*;
import willy.server.sql.extra.generics.DBStatus;
import willy.server.sql.extra.generics.DBVariables;

public class MySQLAccess extends DBAccess {
	
    protected Connection con = null;
    protected Statement st = null;
    protected ResultSet rs = null;
    protected SQLException ex=null;
    
    public SQLException getLastSQLException() {
		return ex;
	}

	public MySQLAccess(DBParam dbp)
    {
    	this.dbp=dbp;
    }
    
    public void close()
    {
    	try
    	{
    		if(con!=null) con.close();
    	}
    	catch(SQLException e)
    	{
    		//estem tancant, no cal ser tant polit
    		e.printStackTrace();
    	}
    }
    
    public String getType()
    {
    	return "MySQL";
    }
    
    /* 
     * @return Consulta DB la versió 
     * @deprecated s'ha de substituir per la info obtinguda del show variables
     */
    public String getVersion()
    {
    	String ver=null;
    	
    	if(con==null) this.Connect();

    	try
    	{
    		st = con.createStatement();
    		rs = st.executeQuery("SELECT VERSION()");

    		if (rs.next()) {
    			ver=rs.getString(1);
    		}
    		
    		rs.close();
    		st.close();
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}
    	
    	return ver;
    }
    
    public DBProcessList getProcessList()
    {
    	DBProcessList dbpl=null;
    	
    	/* mysql> show full processlist;
    	 * +-----------+------+-----------+------+---------+------+-------+-----------------------+
    	 * | Id        | User | Host      | db   | Command | Time | State | Info                  |
    	 * +-----------+------+-----------+------+---------+------+-------+-----------------------+
    	 * | 130386383 | root | localhost | NULL | Query   |    0 | NULL  | show full processlist |
    	 * +-----------+------+-----------+------+---------+------+-------+-----------------------+
    	 * 1 row in set (0.00 sec)
    	 */
    	
    	if(con==null) this.Connect();

    	try
    	{
    		this.getDatalink("show full processlist");
    		
    		dbpl=new DBProcessList(rs);

    		this.closeDatalink();
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}
    	
    	return dbpl;
    }

    public ResultSet getGrants()
    {
    	try
    	{
    		return this.getDatalink("select User,Host,Password from mysql.user;");	
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    		return null;
    	}
    }
    
    public ResultSet getMySQLGrants(String username, String host)
    {
    	try
    	{
    		//System.out.println("show grants for '"+username+"'@"+"'"+host+"'");
    		return this.getDatalink("show grants for '"+username+"'@"+
    								"'"+host+"'"
    							);	
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		return null;
    	}
    }
    
    private ResultSet getDatalink(String query) throws SQLException
    {
    	if(con==null) this.Connect();

    	st = con.createStatement();
    	rs = st.executeQuery(query);
    	
    	return rs;
    }
    
    private void closeDatalink() throws SQLException
    {
    	rs.close();
    	st.close();
    }

    public DBVariables getVariables()
    {
    	DBVariables dbv=null;

    	try 
    	{
    		this.getDatalink("show variables");

    		dbv=new MySQLVariables(rs);
    		
    		this.closeDatalink();
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}

    	return dbv;
    }

    public DBStatus getSlaveStatus()
    {
    	MySQLSlaveStatus dbs=null;

    	try 
    	{
    		this.getDatalink("show slave status");

    		dbs=new MySQLSlaveStatus(rs);
    		
    		this.closeDatalink();
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}

    	return dbs;
    }
    
    public MySQLStatus getStatus()
    {
    	MySQLStatus dbs=null;

    	try 
    	{
    		//SHOW /*!50002 GLOBAL */ STATUS ?????
    		this.getDatalink("show status");

    		dbs=new MySQLStatus(rs);
    		
    		this.closeDatalink();
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}

    	return dbs;
    }
    
    protected void Connect()
    {
    	try
    	{
    		con = DriverManager.getConnection(
    				dbp.getUrl(), 
    				dbp.getUser(), 
    				dbp.getPassword()
    		);
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    		System.out.println(ex.getMessage());
    	}
    }
    
    /**
     * provem si el server esta actiu
     */
    public boolean ping()
    {
       	try 
    	{
    		this.getDatalink("select 1");
    		
    		this.closeDatalink();
    		
    		return true;
    	}
    	catch(Exception ex)
    	{
    		return false;
    	}
    }

    /**
     * funció FAKE - falta implementar
     * @return
     */
    public boolean testDB(String db)
    {
    	//show databases like 'test';
    	return false;
    }
    
    /**
     * funció FAKE - falta implementar
     * @return
     */
    public String getCurrentUser()
    {
    	/*
    	mysql> select current_user();
    	+----------------+
    	| current_user() |
    	+----------------+
    	| root@localhost |
    	+----------------+
    	1 row in set (0.00 sec)
    	
    	  private static List a(aI paramaI, P paramP)
  {
    if (paramP.a("4.1.2") >= 0)
    {
      return paramaI.a("SHOW GRANTS", new Object[0]);
    }
    if (paramP.a("4.0.6") >= 0)
    {
      String[] arrayOfString;
      if ((
        arrayOfString = (
        paramP = (String)paramaI.a("SELECT CURRENT_USER()", new Object[0]))
        .split("@")).length != 
        2)
      {
        throw new RuntimeException("Failed to check grants - unparseable CURRENT_USER(), got \"" + paramP + "\", expected user@host");
      }
      return paramaI.a("SHOW GRANTS FOR ?@?", new Object[] { arrayOfString[0], arrayOfString[1] });
    }

    throw new RuntimeException("Version " + paramP + " not supported.");
  }
    	
    	*/
    	return null;
    }
    
}
