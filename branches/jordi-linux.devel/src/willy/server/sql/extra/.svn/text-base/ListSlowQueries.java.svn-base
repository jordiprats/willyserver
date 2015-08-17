package willy.server.sql.extra;

import java.util.*;

public class ListSlowQueries {
	
	protected HashMap<String, DBQuery> slows=null;
	
	public ListSlowQueries()
	{
		this.slows=new HashMap<String, DBQuery>();
	}
	
	//TODO: top user
	//TODO: top schema
	//TODO: top host
	
	
	/**
	 * obtindre llistat de slowqueries segons criteri
	 * @param long_query_time mínim
	 * @return llistat de slowqueries max_time >= long_query_time
	 */
	public Vector<DBQuery> getSlowQueriesMaxTime(double long_query_time)
	{
		Vector<DBQuery> lsq=new Vector<DBQuery>();
		
		Set<String> setslows =this.slows.keySet();
		Iterator<String> itslows =setslows.iterator();
		
		//tmp pel bucle
		DBQuery qtmp=null;
		
		while(itslows.hasNext())
		{
			qtmp=this.slows.get(itslows.next());
			
			if(qtmp.getMax_time_seen()>=long_query_time)
			{
				lsq.add(qtmp);
			}
		}

		return lsq;
	}
	
	/**
	 * VIGILAR, no testejat
	 * @param long_query_time mínim
	 * @return llistat de slowqueries mean_time >= long_query_time
	 */
	public Vector<DBQuery> getSlowQueriesMeanTime(double long_query_time)
	{
		Vector<DBQuery> lsq=new Vector<DBQuery>();
		
		Set<String> setslows =this.slows.keySet();
		Iterator<String> itslows =setslows.iterator();
		
		//tmp pel bucle
		DBQuery qtmp=null;
		
		while(itslows.hasNext())
		{
			qtmp=this.slows.get(itslows.next());
			
			if(qtmp.getMean_time_seen()>=long_query_time)
			{
				lsq.add(qtmp);
			}
		}
		
		return lsq;
	}
	
	/**
	 * informa d'una nova query vista
	 * @return DBQuery que s'ha modificat
	 */
	public DBQuery seen(DBProcessItem dbpi)
	{
		DBQuery dbq=null;
		
		//obtinc la query exacte
		dbq=this.slows.get(dbpi.getInfo());
		
		//TODO: eliminar informació com IDs per agrupar queries iguals
		// http://jsqlparser.sourceforge.net/example.php
		// jar instalat, falta fer
		
		//TODO: guardar els queryid per saber si es un update o una nova query
		
		if(dbq==null)
		{
			//query nova, add
			dbq=new DBQuery(dbpi);
			
			this.slows.put(dbpi.getInfo(), dbq);
		}
		else
		{
			//query existent, update
			dbq.addInstance(dbpi);
		}
		
		return dbq;
	}
	
	public int getCount()
	{
		return slows.size();
	}
	
	public String toString()
	{
		return this.slows.toString();
	}
}
