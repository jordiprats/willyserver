package willy.server.gui.tabs;

import java.util.Iterator;
import java.util.LinkedList;

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

public class TableStatusList extends GenericTab{

	
	protected GUIMessages rb=null;
	protected GUIImages rm=null;
	
	protected Table tblschemas=null;
	protected Table tbltables=null;
	
	TableColumn dbcol=null;
	TableColumn tblcol=null;
	
	MySQLTables tbls=null;
	
	boolean updated=false;
	

	
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
		
		dbcol = new TableColumn(tblschemas, SWT.NONE);
		dbcol.setWidth(100);
		dbcol.setText(rb.getString("pestanyes.global.tablestatuslist.schemas"));
		
		tbltables = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tbltables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbltables.setHeaderVisible(true);
		tbltables.setLinesVisible(true);
		
		tblcol = new TableColumn(tbltables, SWT.NONE);
		tblcol.setWidth(100);
		tblcol.setText(rb.getString("pestanyes.global.tablestatuslist.tables"));
		
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
					itemtbl.setText(dbt.getTableName());
					if(dbt.isFailed())
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
