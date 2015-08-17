package willy.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

//cachedrowset
//import javax.sql.rowset.*;

import willy.server.sql.extra.*;
import willy.server.sql.extra.generics.DBStatus;
import willy.server.sql.extra.generics.DBVariables;
import willy.server.sql.mysql.engines.InnoDBStatus;
import willy.singletons.DBLink;

public class MySQLAccess extends DBAccess {
	
    protected Connection con = null;
    protected Statement st = null;
    protected ResultSet rs = null;
    protected SQLException ex=null;
 
/*
 * TODO: detectar versió més detalladament:

 * compilat a mà:
 
    mysql> show variables like 'version%';
    +-------------------------+---------------------+
    | Variable_name           | Value               |
    +-------------------------+---------------------+
    | version                 | 5.1.54-log          |
    | version_comment         | Source distribution |
    | version_compile_machine | x86_64              |
    | version_compile_os      | unknown-linux-gnu   |
    +-------------------------+---------------------+
    4 rows in set (0.00 sec)
 
 * paquet:

	mysql> show variables like 'version%';
	+-------------------------+------------------------------+
	| Variable_name           | Value                        |
	+-------------------------+------------------------------+
	| version                 | 5.1.43-log                   |
	| version_comment         | MySQL Community Server (GPL) |
	| version_compile_machine | x86_64                       |
	| version_compile_os      | unknown-linux-gnu            |
	+-------------------------+------------------------------+
	4 rows in set (0.00 sec)

	mysql> show variables like 'version%';
	+-------------------------+------------------------------+
	| Variable_name           | Value                        |
	+-------------------------+------------------------------+
	| version                 | 5.5.27                       |
	| version_comment         | MySQL Community Server (GPL) |
	| version_compile_machine | x86                          |
	| version_compile_os      | Win64                        |
	+-------------------------+------------------------------+
	4 rows in set (0.00 sec)

*/
    
    public DBAccess newConnection()
    {
    	return new MySQLAccess(dbp);
    }
    
    public SQLException getLastSQLException() {
		return ex;
	}

	public MySQLAccess(DBParam dbp)
    {
    	this.dbp=dbp;
    }
	
	public DBParam getDBParam()
	{
		return this.dbp;
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
    		//st = con.createStatement();
    		rs = this.getDatalink("SELECT VERSION()");

    		if (rs.next()) {
    			ver=rs.getString(1);
    		}
    		
    		rs.close();
    		//st.close();
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
    
    public ResultSet runQuery(String query)
    {
    	try
    	{
    		//System.out.println(query);
    		return this.getDatalink(query);	
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		return null;
    	}
    }
    
    public void nextQuery() throws SQLException
    {
    	rs.close();
    	st.close();
    	
    	rs=null;
    	st=null;
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
    	
    	//System.out.println("RUN QUERY: "+query);
    	DBLink.getInstance().newQuery();
    	
    	
    	if(this.dbp.isRecording())
    	{
    		//guardar a disc (thread?) com copio el RS?
    		
    		/*
    		import com.sun.rowset.CachedRowSetImpl;
    		
    	    ResultSet rs = stmt.executeQuery("SELECT * FROM EMPLOYEES");
    	    CachedRowSetImpl crs = new CachedRowSetImpl();
    	    crs.populate(rs);
    	    */

    	}
    	
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
    
    public InnoDBStatus getInnoDBStatus()
    {
    	InnoDBStatus idbs=null;

    	try 
    	{
    		this.getDatalink("show engine innodb status");

    		idbs=new InnoDBStatus(rs);
    		
    		this.closeDatalink();
    	}
    	catch(SQLException ex)
    	{
    		this.con=null;
    		Logger lgr = Logger.getLogger(this.getClass().getName());
    		lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}

    	return idbs;
    }
    
    public MySQLStatus getGlobalStatus()
    {
    	MySQLStatus dbs=null;

    	try 
    	{
    		//SHOW /*!50002 GLOBAL */ STATUS 
//    		mysql> show status like '%ytes%';
//    		+----------------+-------+
//    		| Variable_name  | Value |
//    		+----------------+-------+
//    		| Bytes_received | 503   |
//    		| Bytes_sent     | 23624 |
//    		+----------------+-------+
//    		2 rows in set (0.00 sec)
//
//    		mysql> show global status like '%ytes%';
//    		+----------------+---------------+
//    		| Variable_name  | Value         |
//    		+----------------+---------------+
//    		| Bytes_received | 372068210928  |
//    		| Bytes_sent     | 1330061175742 |
//    		+----------------+---------------+
//    		2 rows in set (0.00 sec)
    		
    		//per la sessió creo: public MySQLStatus getSessionStatus()
    		
    		//http://dev.mysql.com/doc/refman/5.1/en/server-status-variables.html

    		//TODO: potser posar un informational
//    		Delayed_writes
//
//    		The number of INSERT DELAYED rows written. 
//
//    		mysql> SHOW global STATUS LIKE 'Dela_%';
//    		+------------------------+-------+
//    		| Variable_name          | Value |
//    		+------------------------+-------+
//    		| Delayed_errors         | 0     |
//    		| Delayed_insert_threads | 0     |
//    		| Delayed_writes         | 0     |
//    		+------------------------+-------+
//    		3 rows in set (0.00 sec)
    		
//    		When a client uses INSERT DELAYED, it gets an okay from the server at once, 
//    		and the row is queued to be inserted when the table is not in use by 
//    		any other thread. 
//    		The queued rows are held only in memory until they are inserted into the table. 
//    		This means that if you terminate mysqld forcibly (for example, with kill -9) 
//    		or if mysqld dies unexpectedly, any queued rows that have not been written 
//    		to disk are lost. 

    		
    		this.getDatalink("show global status");

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
    
    public MySQLStatus getSessionStatus()
    {
    	MySQLStatus dbs=null;

    	try 
    	{
    		this.getDatalink("show session status");

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
    		
        	//evitar anar a parar al querylog
        	//set session sql_log_off=1;
        	//TODO: de pas testejem que som SUPER
    		st = con.createStatement();
        	rs = st.executeQuery("set session sql_log_off=1;");
        	
        	st.close();
        	rs.close();
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
