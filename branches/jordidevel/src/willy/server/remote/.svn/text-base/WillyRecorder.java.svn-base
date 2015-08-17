package willy.server.remote;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import willy.server.sql.DBParam;
import willy.server.sql.MySQLAccess;

//per tests:
//http://stackoverflow.com/questions/878848/easy-way-to-fill-up-resultset-with-data
//Mockrunner is released under the terms of an Apache style license, i.e. it's free for commercial and non-commercial use

//pas a ResultSet a CSV
//http://opencsv.sourceforge.net/
//Yes. opencsv is available under a commercial-friendly Apache 2.0 license.
//CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"), '\t');
//java.sql.ResultSet myResultSet = ....
//writer.writeAll(myResultSet, includeHeaders);

public class WillyRecorder {

	protected static final String jetprofiler="Good artists copy, great artists steal. Pablo Picasso";

	/**
	 * llistat de parametres de la db. caldrà carregarse d'algun lloc
	 */
	protected static List<DBParam> ldbp=new Vector<DBParam>();

	public static void main(String[] args) 
	{
		//TODO: parsejar arguments, per testing:
		DBParam dbp=new DBParam(	"testro",
									"testroncdcdcweq721984",
									"caronte");

		dbp.enableRecording();

		ldbp.add(dbp);

		Iterator<DBParam> itdbp=ldbp.iterator();
		while(itdbp.hasNext())
		{
			dbp=itdbp.next();

			MySQLAccess db=new MySQLAccess(dbp);

			db.getClass();

			String queries_base[] = {
					"show variables",
					"select User,Host,Password from mysql.user;", //TODO: to be faked
					//"show grants for '"+dbp.getUser()+"'@'localhost'",
					"select table_schema,table_name,table_type,engine,data_length,data_free,table_comment from information_schema.tables where table_schema not in ('information_schema','mysql','performance_schema')",
					"show global status",
					"SELECT CATALOG_NAME,SCHEMA_NAME,DEFAULT_CHARACTER_SET_NAME,DEFAULT_COLLATION_NAME,SQL_PATH FROM information_schema.schemata WHERE schema_name NOT IN ('information_schema','performance_schema','mysql');",
					"show engines;",
					//de més a més
					"show master status;",
					"show slave status;"
			};
			//
			String queries_stat[] = {
					"show full processlist",
					"show engine innodb status"
			};

			

			try {
				RSWriter rsw=new RSWriter("/tmp/cacazip");

				for(int i=0;i<queries_base.length;i++)
				{
					System.out.println(queries_base[i]);
					rsw.write(db.runQuery(queries_base[i]), queries_base[i]);
				}

				//aixo cada segons N-vegades
				for(int i=0;i<queries_stat.length;i++)
				{
					System.out.println(queries_stat[i]);
					//TODO: incloure timestamp per identificarlo?
					rsw.write(db.runQuery(queries_stat[i]), queries_stat[i]);
				}
				
				rsw.close();
				
				RSReader rsr = new RSReader("/tmp/cacazip");
				
				rsr.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
