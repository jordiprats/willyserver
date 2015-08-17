package willy.server.gui;

//http://code.google.com/p/swt-xy-graph/


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import willy.server.dba.DBA;
import willy.server.gui.tabs.EnginesTab;
import willy.server.gui.tabs.InstallationCheckList;
import willy.server.gui.tabs.ProcessListTab;
import willy.server.gui.tabs.TableStatusList;
import willy.server.sql.extra.MySQLState;
import willy.singletons.GUIMessages;


//integrar jfreechart
//http://jfree.org/phpBB2/viewtopic.php?t=4693

public class WillyGUI implements Runnable{

	protected volatile boolean open=false;
	protected Shell shell = null;
	protected Display display=null;
	
	protected ProcessListTab plt=null;
	protected InstallationCheckList icl=null;
	protected EnginesTab et=null;
	protected TableStatusList tsl=null;
	
	//PER DEMO
	protected NetworkChart netchart=null;
	
	//TODO: querytime acomulat? meantime? meantime by host? ...
	
	protected GUIMessages rb=null;

	public void updateGUI(DBA dba)
	{
		netchart.updateValues();
		
		MySQLState ms=(MySQLState)dba.getLastState();
		
		this.icl.updateValues(ms);
		this.plt.updateValues(ms);
		
		this.plt.updateQueries(dba.getUpdatedQueries());

		if(dba.hasTablesInfo())
			tsl.updateGUI(dba.getTablesInfo());
	}

	/**
	 * @wbp.parser.entryPoint
	 * arranca l'interficie gr�fica
	 */
	public void run()
	{
		rb=GUIMessages.getInstance();

		shell=new Shell ();
		shell.setSize(745, 471);
		shell.setMinimumSize(645, 471);

		display = Display.getDefault();

		shell.setMaximized(true);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.open ();

		this.setTitle("Free Willy!!!");

		this.setupMenu(shell);

		CTabFolder contenidor = new CTabFolder(shell, SWT.BORDER);
		contenidor.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem pestanyaglobal = new CTabItem(contenidor, SWT.NONE);
		pestanyaglobal.setText(rb.getString("pestanyes.global.estatglobal"));

		// queries running
		// traffic
		// querycache hitrate
		// myisam key efficiency
		// innodb buffer pool size ?

		CTabItem pestanyaprocesos = new CTabItem(contenidor, SWT.NONE);
		pestanyaprocesos.setText(rb.getString("pestanyes.global.processlist"));

		//CTabItem pestanyaslave = new CTabItem(contenidor, SWT.NONE);
		//pestanyaslave.setText("slave");

		CTabItem pestanyachecklist = new CTabItem(contenidor, SWT.NONE);
		pestanyachecklist.setText(rb.getString("pestanyes.global.checklist.installation"));

		CTabItem pestanyaengines = new CTabItem(contenidor, SWT.NONE);
		pestanyaengines.setText(rb.getString("pestanyes.global.engines"));

		// en �s?
		// deshabilitar alguns?
		// potser alguna dada per engine?

		//CTabItem pestanyaschemas = new CTabItem(contenidor, SWT.NONE);
		//pestanyaschemas.setText(rb.getString("pestanyes.global.schemas"));

		CTabItem pestanyataules = new CTabItem(contenidor, SWT.NONE);
		pestanyataules.setText(rb.getString("pestanyes.global.tables"));

		//llista schema
		//estat taula

		//inbdiquem pestanya per defecte
		contenidor.setSelection(pestanyaprocesos);

		plt=new ProcessListTab(contenidor);
		
		
		pestanyaprocesos.setControl(plt.getComposite());

		//composite.setBackgroundImage(SWTResourceManager.getImage("/home/jprats/Downloads/fuck_yeah.jpg"));
		

		//Composite comp=new Composite(shell, SWT.NONE);
		//comp.setLayout(new FillLayout(SWT.HORIZONTAL));
		

		shell.setTabList(new Control[]{contenidor});

		icl=new InstallationCheckList(contenidor);
		pestanyachecklist.setControl(icl.getComposite());

		et=new EnginesTab(contenidor);
		pestanyaengines.setControl(et.getComposite());

		tsl=new TableStatusList(contenidor);
		pestanyataules.setControl(tsl.getComposite());

		//PER DEMO
		netchart=new NetworkChart(contenidor);
		pestanyaglobal.setControl(netchart.getComposite());
		
		
		//shell.pack (); //tamany m�nim
		this.open=true;
		while (!shell.isDisposed())
		{
			//mentre no tanqui la finestre
			if (!display.readAndDispatch ()) display.sleep ();
		}
		this.open=false;
	}

	/**
	 * @return esta arrancada linterficie gr�fica?
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * obt� una mostra mitjan�ant el WillyWorker
	 * @param WillyWorker a executar
	 */
	public void addRunnable(Runnable ww)
	{
		display.asyncExec(ww);
	}



	public void setTitle(String text)
	{
		shell.setText(text);
	}

	private void setupMenu(Shell shell)
	{
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu desplegableFile = new Menu(mntmFile);
		mntmFile.setMenu(desplegableFile);

		MenuItem miexemple = new MenuItem(desplegableFile, SWT.NONE);
		miexemple.setText("No faig res");

		MenuItem miseparadorexit = new MenuItem(desplegableFile, SWT.SEPARATOR);
		miseparadorexit.setText("separador_exit");

		MenuItem miexit = new MenuItem(desplegableFile, SWT.NONE);
		miexit.setText("Exit");

		MenuItem mntmAbout = new MenuItem(menu, SWT.CASCADE);
		mntmAbout.setText("Help");

		Menu desplegableHelp = new Menu(mntmAbout);
		mntmAbout.setMenu(desplegableHelp);

		MenuItem mntmAbout_1 = new MenuItem(desplegableHelp, SWT.NONE);
		mntmAbout_1.setText("About");
	}



}