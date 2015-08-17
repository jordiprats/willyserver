package willy.server.sql.extra.grants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import willy.server.sql.*;


/*

mysql> show privileges;
+-------------------------+---------------------------------------+-------------------------------------------------------+
| Privilege               | Context                               | Comment                                               |
+-------------------------+---------------------------------------+-------------------------------------------------------+
| Alter                   | Tables                                | To alter the table                                    |
| Alter routine           | Functions,Procedures                  | To alter or drop stored functions/procedures          |
| Create                  | Databases,Tables,Indexes              | To create new databases and tables                    |
| Create routine          | Databases                             | To use CREATE FUNCTION/PROCEDURE                      |
| Create temporary tables | Databases                             | To use CREATE TEMPORARY TABLE                         |
| Create view             | Tables                                | To create new views                                   |
| Create user             | Server Admin                          | To create new users                                   |
| Delete                  | Tables                                | To delete existing rows                               |
| Drop                    | Databases,Tables                      | To drop databases, tables, and views                  |
| Event                   | Server Admin                          | To create, alter, drop and execute events             |
| Execute                 | Functions,Procedures                  | To execute stored routines                            |
| File                    | File access on server                 | To read and write files on the server                 |
| Grant option            | Databases,Tables,Functions,Procedures | To give to other users those privileges you possess   |
| Index                   | Tables                                | To create or drop indexes                             |
| Insert                  | Tables                                | To insert data into tables                            |
| Lock tables             | Databases                             | To use LOCK TABLES (together with SELECT privilege)   |
| Process                 | Server Admin                          | To view the plain text of currently executing queries |
| References              | Databases,Tables                      | To have references on tables                          |
| Reload                  | Server Admin                          | To reload or refresh tables, logs and privileges      |
| Replication client      | Server Admin                          | To ask where the slave or master servers are          |
| Replication slave       | Server Admin                          | To read binary log events from the master             |
| Select                  | Tables                                | To retrieve rows from table                           |
| Show databases          | Server Admin                          | To see all databases with SHOW DATABASES              |
| Show view               | Tables                                | To see views with SHOW CREATE VIEW                    |
| Shutdown                | Server Admin                          | To shut down the server                               |
| Super                   | Server Admin                          | To use KILL thread, SET GLOBAL, CHANGE MASTER, etc.   |
| Trigger                 | Tables                                | To use triggers                                       |
| Update                  | Tables                                | To update existing rows                               |
| Usage                   | Server Admin                          | No privileges - allow connect only                    |
+-------------------------+---------------------------------------+-------------------------------------------------------+
29 rows in set (0.00 sec)

*/

public class MySQLGrants extends DBGrants {
	
	protected HashMap<String,DBUser> grants=null;
	
	public MySQLGrants()
	{
		this.grants=new HashMap<String,DBUser>();
	}
	
	/**
	 * get data
	 */
	public void getGrants(DBAccess db) throws SQLException
	{
		super.timestamp = System.currentTimeMillis()/1000;
		
		ResultSet rs=db.getGrants();
		ResultSet rsgrants=null;
		String grantstmp=null;
		
		DBUser dbu=null;
		
		while(rs.next())
		{
			String username=rs.getString(1);
			String host=rs.getString(2);
			String password=rs.getString(3);
			
			rsgrants=((MySQLAccess)db).getMySQLGrants(username, host);
			
			
			if(rsgrants!=null && rsgrants.next())
				grantstmp=rsgrants.getString(1);
			else 
				grantstmp=null;
			
			dbu=new DBUser(
							username,
							password,
							host,
							grantstmp
					);
			
			this.grants.put(username+":"+password+"@"+host, dbu);
			//System.out.println("GRANT: "+username+":"+password+"@"+host);
		}
	}

	/**
	 * busca els usuaris invàlids
	 * @return vector de usuaris invalids
	 */
	public Vector<DBUser> getInvalidUsers()
	{
		//TODO: validar que sigui un hash
		Vector<DBUser> vdbu=new Vector<DBUser>();
		
		Set<String> ks=this.grants.keySet();
		Iterator<String> it=ks.iterator();
		String ktmp=null;
		DBUser dbutmp=null;
		
		while(it.hasNext())
		{
			dbutmp=null;
			
			ktmp=it.next();
			
			dbutmp=this.grants.get(ktmp);
			
			if(dbutmp.getGrants()==null)
			{
				vdbu.add(dbutmp);
			}
			
			//TODO: check hash sigui vàlid
		}
		
		return vdbu;
	}
	
	/**
	 * busca els usuaris amb oldpasswords
	 * @return vector de usuaris amb password antic
	 */
	public Vector<DBUser> getWeakPasswords()
	{
		//TODO: validar que sigui un hash
		Vector<DBUser> vdbu=new Vector<DBUser>();
		
		Set<String> ks=this.grants.keySet();
		Iterator<String> it=ks.iterator();
		String ktmp=null;
		DBUser dbutmp=null;
		
		while(it.hasNext())
		{
			dbutmp=null;
			
			ktmp=it.next();
			
			dbutmp=this.grants.get(ktmp);
			
			if(dbutmp.getPassword().length()<=16)
			{
				vdbu.add(dbutmp);
			}
		}
		
		return vdbu;
	}
	
	/**
	 * donat un hash busca els usuaris amb tal hash
	 * @param password: hash del password
	 * @return vector de usuaris
	 */
	public Vector<DBUser> getByPassword(String password)
	{
		//TODO: validar que sigui un hash
		Vector<DBUser> vdbu=new Vector<DBUser>();
		
		Set<String> ks=this.grants.keySet();
		Iterator<String> it=ks.iterator();
		String ktmp=null;
		DBUser dbutmp=null;
		
		while(it.hasNext())
		{
			dbutmp=null;
			
			ktmp=it.next();
			
			dbutmp=this.grants.get(ktmp);
			
			if(dbutmp.getPassword().compareTo(password)==0)
			{
				vdbu.add(dbutmp);
			}
		}
		
		return vdbu;
	}
	
	public String toString()
	{
		return this.grants.toString();
	}
	
}
