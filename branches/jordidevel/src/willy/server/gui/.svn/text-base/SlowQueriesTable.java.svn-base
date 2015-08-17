package willy.server.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import willy.server.gui.tabs.GenericGUIObject;
import willy.server.sql.extra.DBQuery;
import willy.server.sql.extra.ListSlowQueries;

/*
DEMO TAULA

Table table = new Table(composite_5, SWT.BORDER | SWT.FULL_SELECTION);
table.setHeaderVisible(true);
table.setLinesVisible(true);

TableColumn tblclmnCol = new TableColumn(table, SWT.NONE);
tblclmnCol.setWidth(100);
tblclmnCol.setText("COL1");

TableColumn tblclmnCol_1 = new TableColumn(table, SWT.NONE);
tblclmnCol_1.setWidth(100);
tblclmnCol_1.setText("COL2");

TableItem tableItem = new TableItem(table, SWT.NONE);
tableItem.setText("New TableItem");

TableItem tableItem_1 = new TableItem(table, SWT.NONE);
tableItem_1.setText("New TableItem"); 

*/

public class SlowQueriesTable extends GenericGUIObject{

	protected Table table=null;
	protected TableColumn querycol=null;
	protected TableColumn maxtimecol=null;

	//TODO: evaluar si canviar el index per el punter a DBQuery
	protected HashMap<DBQuery, Integer> dbquerytoindex=null;
	protected int count_items=0;
	

	public SlowQueriesTable(Composite comp)
	{
		dbquerytoindex=new HashMap<DBQuery, Integer>();
		
		composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		//table=new Table(comp, SWT.NONE);
		table=new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		maxtimecol = new TableColumn(table, SWT.NONE);
		maxtimecol.setWidth(100);
		maxtimecol.setText("Max time");
		
		querycol = new TableColumn(table, SWT.NONE);
		querycol.setWidth(100);
		querycol.setText("Query");
	}
	
	public void setData(ListSlowQueries lsq)
	{
		//table.getItemCount();
		//table.getItem(index)
	}
	
	public void addItem(DBQuery dbq)
	{
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(new String[] {
										String.valueOf(dbq.getMax_time_seen()), 
										dbq.getDBQueryString()
										}
								);
		
		this.dbquerytoindex.put(dbq, new Integer((int) count_items++));
	}
	
	public void updateList(LinkedList<DBQuery> ldbq)
	{
		//TODO: funció igual pero amb límit de queries a mostrar
		DBQuery dbqtmp=null;
		Integer tmpidx=0;
		Iterator<DBQuery> it=ldbq.iterator();
		
		while(it.hasNext())
		{
			dbqtmp=it.next();
			
			tmpidx=this.dbquerytoindex.get(dbqtmp);
			
			if(tmpidx==null) //si no el tenim a la llista
				this.addItem(dbqtmp);
			else
			{
				table.getItems()[tmpidx].setText(new String[] {
														String.valueOf(dbqtmp.getMax_time_seen()), 
														dbqtmp.getDBQueryString()
													});
				if((dbqtmp.getLastDBProcessItem().getInnoDBTransaction()!=null)&&
						(dbqtmp.getLastDBProcessItem().getInnoDBTransaction().isBlocking()))
				{
					table.getItems()[tmpidx].setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
				}
			}
			//itemtbl.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));


		}
	}
	
	/**
	 * reseteja la llista i hi posa la nova llista
	 * @param ldbq llista de queries a updatejar
	 */
	public void resetUpdateList(LinkedList<DBQuery> ldbq)
	{
		table.removeAll();
		Iterator<DBQuery> it=ldbq.iterator();
		
		while(it.hasNext())
			this.addItem(it.next());
	}
	
	public int getItemCount()
	{
		return ((this.count_items>0)?count_items-1:count_items);
			
	}
}
