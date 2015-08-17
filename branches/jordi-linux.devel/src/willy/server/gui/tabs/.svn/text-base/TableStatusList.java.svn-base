package willy.server.gui.tabs;

import java.text.Collator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;

import willy.server.sql.mysql.MySQLTables;
import willy.server.sql.mysql.extra.DBItem;
import willy.singletons.GUIImages;
import willy.singletons.GUIMessages;
import org.eclipse.swt.layout.GridData;

public class TableStatusList extends GenericGUIObject{

	
	protected GUIMessages rb=null;
	protected GUIImages rm=null;
	
	protected Table tblschemas=null;
	protected Table tbltables=null;
	
	protected TableColumn dbcol=null;
	protected TableColumn tblcol=null;
	
	protected MySQLTables tbls=null;
	protected boolean updated=false;
	
	//guarrada per invertir el ordre del sort
	protected int lastcol=666;
	
	/**
	 * guardo el tipus de cada columna
	 */
	@SuppressWarnings("rawtypes")
	protected Class[] tipuscol=null;
	
	private static boolean isFailed(DBItem dbi)
	{
		return dbi.isFailed();
	}
	
	private static boolean isFailed(String[] values)
	{
//		dbt.getTableName(), 
//		dbt.getDataLengthString(),
//		dbt.getDataFreeString(),
//		dbt.getTableType(),
//		dbt.getEngine(), 
//		dbt.getTableComment()
		
		System.out.println("isfailed? "+values[4]+" "+values[3]);
		
		if(values[4].compareTo("")==0)
			return DBItem.isFailed(null, values[3]);
		else
			return DBItem.isFailed(values[4], values[3]);
	}
	
	private void sortbyCol(Event e)
	{
		int col=666;
		
		TableColumn[] columns = tbltables.getColumns();
		for(int i=0; i<columns.length;i++)
			if(columns[i]==(TableColumn)e.widget)
				col=i;
		
		if(tipuscol[col]==String.class)
			this.sortbyColString(e, col);
		else if(tipuscol[col]==Long.class)
			this.sortbyColLong(e, col);
		else this.sortbyColString(e, col); //default
		
		//marranu marranu
		tbltables.setSelection(0);
		
		//System.out.println("@@@@@@@@@@sort!");
		//pel ordre de sort
		if(lastcol==col) lastcol=666; else lastcol=col;
	}
	
	//Long.class
	private void sortbyColLong(Event e, int col)
	{	
		TableItem[] items=tbltables.getItems();
		
		Long value1=null;
		Long value2=null;
		for (int i = 1; i < items.length; i++) {
	          try { value1 = Long.parseLong(items[i].getText(col)); }
	          catch(Exception ex) { value1=new Long(-666); }
	          for (int j = 0; j < i; j++) {
	            try { value2 = Long.parseLong(items[j].getText(col)); }
	            catch(Exception ex) { value2=new Long(-666); }
	            if (
	            	((lastcol!=col)&&(value1 < value2)) ||
	            	((lastcol==col)&&(value1 > value2))
	            	)
	            {
	              String[] values = { 
	            		  items[i].getText(0),
	            		  items[i].getText(1),
	            		  items[i].getText(2),
	            		  items[i].getText(3),
	            		  items[i].getText(4),
	            		  items[i].getText(5)
	            		  };
	              items[i].dispose();
	              TableItem item = new TableItem(tbltables, SWT.NONE, j);
	              
	              if(TableStatusList.isFailed(values))
						item.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
	              
	              item.setText(values);
	              items = tbltables.getItems();
	              break;
	            }
	          }
		}
	}
	
	//String.class
	private void sortbyColString(Event e, int col)
	{	
		TableItem[] items=tbltables.getItems();
		Collator collator = Collator.getInstance(Locale.getDefault());
		
		for (int i = 1; i < items.length; i++) {
	          String value1 = items[i].getText(col);
	          for (int j = 0; j < i; j++) {
	            String value2 = items[j].getText(col);
	            if (
	            	((lastcol!=col)&&(collator.compare(value1, value2) < 0)) ||
	            	((lastcol==col)&&(collator.compare(value1, value2) > 0))
	            	)
	            {
	              String[] values = { 
	            		  items[i].getText(0),
	            		  items[i].getText(1),
	            		  items[i].getText(2),
	            		  items[i].getText(3),
	            		  items[i].getText(4),
	            		  items[i].getText(5)
	            		  };
	              items[i].dispose();
	              TableItem item = new TableItem(tbltables, SWT.NONE, j);
	              item.setText(values);
	              
	              if(TableStatusList.isFailed(values))
						item.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
	              
	              items = tbltables.getItems();
	              break;
	            }
	          }
		}
	}
	
	
	public TableStatusList(Composite comp)
	{
		rb=GUIMessages.getInstance();
		rm=GUIImages.getInstance(comp.getShell());
		
		composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		tblschemas = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tblschemas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		tblschemas.setHeaderVisible(true);
		tblschemas.setLinesVisible(true);
		//TODO: bot� add schema / edit schema
		
		dbcol = new TableColumn(tblschemas, SWT.NONE);
		dbcol.setWidth(100);
		dbcol.setText(rb.getString("pestanyes.global.tablestatuslist.schemas"));
		
		tbltables = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tbltables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbltables.setHeaderVisible(true);
		tbltables.setLinesVisible(true);
		//TODO: bot� add table / edit table
		
		tipuscol=new Class[6];
		
		tblcol = new TableColumn(tbltables, SWT.NONE);
		tblcol.setWidth(100);
		tblcol.setText(rb.getString("pestanyes.global.tablestatuslist.tablename"));
		tblcol.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  sortbyCol(e);
		      }
		});
		tipuscol[0]=String.class;
		
		tblcol = new TableColumn(tbltables, SWT.NONE);
		tblcol.setWidth(100);
		tblcol.setText(rb.getString("pestanyes.global.tablestatuslist.datalength"));
		tblcol.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  sortbyCol(e);
		      }
		});
		tipuscol[1]=Long.class;

		tblcol = new TableColumn(tbltables, SWT.NONE);
		tblcol.setWidth(100);
		tblcol.setText(rb.getString("pestanyes.global.tablestatuslist.datafree"));
		tblcol.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  sortbyCol(e);
		      }
		});
		tipuscol[2]=Long.class;
		
		tblcol = new TableColumn(tbltables, SWT.NONE);
		tblcol.setWidth(100);
		tblcol.setText(rb.getString("pestanyes.global.tablestatuslist.tabletype"));
		tblcol.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  sortbyCol(e);
		      }
		});
		tipuscol[3]=String.class;
		
		tblcol = new TableColumn(tbltables, SWT.NONE);
		tblcol.setWidth(100);
		tblcol.setText(rb.getString("pestanyes.global.tablestatuslist.engine"));
		tblcol.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  sortbyCol(e);
		      }
		});
		tipuscol[4]=String.class;
		
		tblcol = new TableColumn(tbltables, SWT.NONE);
		tblcol.setWidth(200);
		tblcol.setText(rb.getString("pestanyes.global.tablestatuslist.tablecomment"));
		tblcol.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  sortbyCol(e);
		      }
		});
		tipuscol[5]=String.class;
		
		tblschemas.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				if(tbls==null) return;
				
				tbltables.removeAll();
				LinkedList<DBItem> ltaules=tbls.getTables(tblschemas.getSelection()[0].getText(0));
				
				if(ltaules==null) return;
				Iterator<DBItem> ittbls=ltaules.iterator();
				
				while(ittbls.hasNext())
				{
					DBItem dbt=ittbls.next();
					TableItem itemtbl = new TableItem(tbltables, SWT.NONE);
					itemtbl.setText(new String[] {
													dbt.getTableName(), 
													dbt.getDataLengthString(),
													dbt.getDataFreeString(),
													dbt.getTableType(),
													dbt.getEngine(), 
													dbt.getTableComment()
													});
					
					if(TableStatusList.isFailed(dbt))
						itemtbl.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
					
				}
				tbltables.deselectAll();
			}
		});
		
		tblschemas.pack();
	}
	
	public void updateGUI(MySQLTables tbls)
	{
		if(updated) return;
		
		Iterator<String> itdbs=tbls.getSchemas().iterator();
		
		while(itdbs.hasNext())
		{
			String schemastr=itdbs.next();
			TableItem item = new TableItem(tblschemas, SWT.NONE);
			item.setText(schemastr);
			/*
			Iterator<DBTable> ittbls=tbls.getTables(schemastr).iterator();
			
			while(ittbls.hasNext())
			{
				TableItem itemtbl = new TableItem(tbltables, SWT.NONE);
				itemtbl.setText(ittbls.next().getTableName());
			}
			*/
		}
		
		this.tbls=tbls;
		updated=true;
	}

	
}
