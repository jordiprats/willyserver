package willy.server.gui.tabs;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import willy.server.sql.extra.MySQLState;
import willy.singletons.GUIImages;
import willy.singletons.GUIMessages;
import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

/*
TableItem item = new TableItem(table, SWT.NONE);
item.setText(new String[] {"","item"});
item.setImage(rm.getImage("check_ok"));

TableItem item2 = new TableItem(table, SWT.NONE);
item2.setText(new String[] {"","item"});
item2.setImage(rm.getImage("check_fail"));
*/

public class InstallationCheckList extends GenericTab{

	//protected Composite composite=null;
	protected HashMap<String, TableItem> tableitems=null;
	protected HashMap<String,String> descriptions=null;
	
	protected GUIMessages rb=null;
	protected GUIImages rm=null;
	
	protected Table table=null;
	protected Text descripcio=null;
	protected TableColumn estatcol=null;
	protected TableColumn nomvarcol=null;
	
	protected Button btnArreglar=null;
	protected Button btnDescartar=null;

	public InstallationCheckList(Composite comp)
	{
		rb=GUIMessages.getInstance();
		rm=GUIImages.getInstance(comp.getShell());
		
		tableitems=new HashMap<String,TableItem>();
		descriptions=new HashMap<String,String>();

		composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		//composite.setContent(table);
		//composite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		estatcol = new TableColumn(table, SWT.NONE);
		estatcol.setWidth(50);
		estatcol.setText("estat");

		nomvarcol = new TableColumn(table, SWT.NONE);
		nomvarcol.setWidth(100);
		nomvarcol.setText("item");
		
		Composite col_desc_accions = new Composite(composite, SWT.NONE);
		col_desc_accions.setLayout(new GridLayout(1, false));
		
		descripcio = new Text(col_desc_accions, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		descripcio.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		descripcio.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		descripcio.setEditable(false);
		descripcio.setText(rb.getString("pestanyes.global.checklist.nodescription"));
				
		Composite area_accions = new Composite(col_desc_accions, SWT.NONE);
		area_accions.setLayout(null);
		area_accions.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		btnArreglar = new Button(area_accions, SWT.CENTER);
		btnArreglar.setBounds(10, 10, 86, 29);
		btnArreglar.setText(rb.getString("pestanyes.global.checklist.arreglar"));
		btnArreglar.setEnabled(false);
		
		btnDescartar = new Button(area_accions, SWT.NONE);
		btnDescartar.setBounds(103, 10, 86, 29);
		btnDescartar.setText(rb.getString("pestanyes.global.checklist.descartar"));
		btnDescartar.setEnabled(false);

		
		//AIXO NO FUNCIONA
		table.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				String str=descriptions.get(table.getSelection()[0].getText(1));
				if(str==null) str=rb.getString("pestanyes.global.checklist.nodescription");
				descripcio.setText(str);
			}
		});
		
	}
	
	public void updateValues(MySQLState ms)
	{
		//cas inicial
		if((table.getItems().length==0)&&(ms.getDBItems()!=null))
		{
			System.out.println("=>=>=>=> no items");
			
			this.addItem("pestanyes.global.checklist.innodb_file_per_table",
							ms.getMySQLVariables().innodb_file_per_table());
			
			this.addWarning("pestanyes.global.checklist.have_query_cache", 
							ms.getMySQLVariables().have_query_cache());
			
			this.addWarning("pestanyes.global.checklist.delete_test_schema",
							ms.getDBItems().getSchema("test")==null);
			
			this.addSeparador("pestanyes.global.checklist.separador.users");
			
			this.addItem("pestanyes.global.checklist.invalid_db_users", 
							ms.getDBGrants().getInvalidUsers().size()==0);
			
			this.addItem("pestanyes.global.checklist.insecure_db_users", 
					ms.getDBGrants().getWeakPasswords().size()==0);
			
			this.addItem("pestanyes.global.checklist.wo_password_db_users", 
					ms.getDBGrants().getByPassword("").size()==0);
			
			if(ms.getDBProcessList().isSlave())
			{
				//checklist en cas que sigui un slave
				this.addSeparador("pestanyes.global.checklist.separador.slave");
				
				this.addItem("pestanyes.global.checklist.slave.slave_in_readonly", 
						ms.getMySQLVariables().read_only());
			}
			
			this.addSeparador("pestanyes.global.checklist.separador.engines");
			
			//NO FUNCIONA PQ NO EXISTEIX LA VARIABLE
			if(ms.getMySQLVariables().have_archive())
				this.addItem("pestanyes.global.checklist.engines.archive", 
						ms.getDBItems().getItemCountByEngine("archive")!=0);
			
			if(ms.getMySQLVariables().have_innodb())
				this.addItem("pestanyes.global.checklist.engines.innodb", 
						ms.getDBItems().getItemCountByEngine("innodb")!=0);
			
			//TODO: WTF? isam vs myisam? check documentation
			if(ms.getMySQLVariables().have_isam())
				this.addItem("pestanyes.global.checklist.engines.myisam", 
						ms.getDBItems().getItemCountByEngine("myisam")!=0);
			
			if(ms.getMySQLVariables().have_csv())
				this.addItem("pestanyes.global.checklist.engines.csv", 
						ms.getDBItems().getItemCountByEngine("csv")!=0);
				
			if(ms.getDBItems().getEngineDetails("federated")!=null)
				this.addItem("pestanyes.global.checklist.engines.federated", 
						ms.getDBItems().getItemCountByEngine("federeated")!=0);
			
			if(ms.getDBItems().getEngineDetails("blackhole")!=null)
				this.addItem("pestanyes.global.checklist.engines.blackhole", 
						ms.getDBItems().getItemCountByEngine("blackhole")!=0);
			
			nomvarcol.pack();
			
		}
		/* DEBUG: else
		{
			System.out.println(
					"tablesize: "+(table.getItems().length==0)
					+
					"dbitems"+(ms.getDBItems()!=null)
					);
		}*/
		//TODO: else actualizar
	}

	private void addDescription(String key)
	{
		String str;
		try
		{
			str=rb.getString(key+".desc");
		}
		catch(Exception e)
		{
			str=rb.getString("pestanyes.global.checklist.nodescription");
		}
		
		this.descriptions.put(rb.getString(key), str);
	}
	
	private void addSeparador(String key)
	{
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"",rb.getString(key)});
		item.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		/*if(value)
			item.setImage(rm.getImage("check_ok"));
		else
			item.setImage(rm.getImage("check_fail"));*/
		
		this.addDescription(key);

	}
	
	/**
	 * 
	 * @param key string a buscar al llistat de textos
	 * @param value: si es true surt icon ok, sino icona critical
	 */
	private void addItem(String key, boolean value)
	{
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"",rb.getString(key)});
		if(value)
			item.setImage(rm.getImage("check_ok"));
		else
			item.setImage(rm.getImage("check_fail"));
		
		this.addDescription(key);
	}
	
	/**
	 * 
	 * @param key string a buscar al llistat de textos
	 * @param value: si es true surt icon ok, sino icona warning
	 */
	private void addWarning(String key, boolean value)
	{
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"",rb.getString(key)});
		if(value)
			item.setImage(rm.getImage("check_ok"));
		else
			item.setImage(rm.getImage("check_warn"));
		
		this.addDescription(key);
	}
	


}
