package willy.server.sql.extra;

import java.util.*;

public class DBQuery {
	
	protected Vector<DBProcessItem> listexec=null;
	protected String dbquerystring=null;
	protected int count=0;
	
	/*
	 * TODO: EXPLAIN
	 */
	
	/*
	 * TODO: SHOW PROFILE http://systemadmin.es/2011/11/show-profile-de-mysql
	 */
	
	/**
	 * per relacionar amb l'index de la taula de la GUI
	 */
	protected int gui_index=0;
	
	public int getGUIindex() {
		return gui_index;
	}

	public void setGUIindex(int gui_index) {
		this.gui_index = gui_index;
	}

	protected double max_time_seen=0.0;
	protected double mean_time_seen=0.0;
	//potser també el 95% de les mostres?

	protected double total_time=0.0;
	
	//TODO: Afegir el explain a la query 
	
	public DBQuery(DBProcessItem dbpi)
	{
		this.listexec=new Vector<DBProcessItem>();
		this.dbquerystring=dbpi.getInfo();
		
		this.updateData(dbpi);
		
		this.listexec.add(dbpi);
		
		this.count=1;
	}
	
	public int getCountInstances()
	{
		return this.listexec.size();
	}
	
	public void addInstance(DBProcessItem dbpi)
	{
		//debug
		//System.out.println(this.getClass().getName()+" adding: "+dbpi);
		this.updateData(dbpi);
		
		this.listexec.add(dbpi);
	}

	public String getDBQueryString() {
		return this.dbquerystring;
	}

	public void setDBQueryString(String dbqueryname) {
		this.dbquerystring = dbqueryname;
	}
	
	public String toString()
	{
		return "querystring=>"+this.dbquerystring+ 
		",#instancies=>"+this.listexec.size()+
		//",llista=>"+this.listexec+
		",max_time_seen=>"+this.max_time_seen+
		"\n";

	}
	
	public double getMax_time_seen() {
		return max_time_seen;
	}

	public double getMean_time_seen() {
		return mean_time_seen;
	}

	//actualitzar valors
	private void updateData(DBProcessItem dbpi)
	{
		if(Double.parseDouble(dbpi.getTime())>this.max_time_seen)
			this.max_time_seen=Double.parseDouble(dbpi.getTime());
		
		//check si ja es una query de la llista
		//if(dbpi.getId()!=)
	}
}
