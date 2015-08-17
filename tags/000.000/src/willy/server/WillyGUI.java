package willy.server;

//http://code.google.com/p/swt-xy-graph/

import java.util.LinkedList;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.csstudio.swt.xygraph.dataprovider.Sample;
import org.csstudio.swt.xygraph.figures.ToolbarArmedXYGraph;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.Trace.*;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Composite;
//import org.eclipse.wb.swt.SWTResourceManager;
//import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Control;
import willy.server.gui.ProcessListCountMeter;
import willy.server.gui.SlowQueriesTable;
import willy.server.gui.ThreadCacheMeter;
import willy.server.sql.extra.DBQuery;
import willy.server.sql.extra.MySQLState;

public class WillyGUI implements Runnable{
	
	protected volatile boolean open=false;
	protected Shell shell = null;
	protected Display display=null;
	
	protected long xcounter=0; 
	protected XYGraph chart=null;
	protected ProcessListCountMeter plcmeter=null;
	protected ThreadCacheMeter tcmeter=null;
	protected SlowQueriesTable sqtable=null;
	protected SlowQueriesTable rtsqtable=null;
	
	protected Canvas canvas_pl_meter=null;
	protected Canvas canvas_tc_meter=null;
	protected Composite composite_slows_table=null;
	
	
	protected CircularBufferDataProvider sleepsprovider=null;
	protected CircularBufferDataProvider queriesprovider=null;
	
	public void addSleepSample(double value)
	{
		sleepsprovider.addSample(new Sample(xcounter++,value));
	}
	
	public void addQuerySample(double value)
	{
		queriesprovider.addSample(new Sample(xcounter-1,value));
	}
	
	/**
	 * @wbp.parser.entryPoint
	 * arranca l'interficie gràfica
	 */
	public void run()
	{
		shell=new Shell ();
		shell.setSize(745, 471);
		shell.setMinimumSize(645, 471);
		
		display = Display.getDefault();

		shell.setMaximized(true);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.open ();
		
	    CTabFolder contenidor = new CTabFolder(shell, SWT.BORDER);
	    contenidor.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
	    CTabItem pestanyaglobal = new CTabItem(contenidor, SWT.NONE);
	    pestanyaglobal.setText("estat global");
	    
		CTabItem pestanyaprocesos = new CTabItem(contenidor, SWT.NONE);
		pestanyaprocesos.setText("processlist");
	    
	    CTabItem pestanyaslave = new CTabItem(contenidor, SWT.NONE);
	    pestanyaslave.setText("slave");
	    
	    //.getTabList()[2].setEnabled(false);
	    //contenidor.getTabList()[0].setEnabled(false); 
	    
	    //inbdiquem pestanya per defecte
	    contenidor.setSelection(pestanyaprocesos);
		
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
	    
		
		//Composite composite_4 = new Composite(contenidor, SWT.NONE);
	    
	    Composite composite = new Composite(contenidor, SWT.NONE);
	    pestanyaprocesos.setControl(composite);
	    
	    //composite.setBackgroundImage(SWTResourceManager.getImage("/home/jprats/Downloads/fuck_yeah.jpg"));
	    composite.setLayout(new FillLayout(SWT.VERTICAL));
	    
	    //Composite comp=new Composite(shell, SWT.NONE);
	    //comp.setLayout(new FillLayout(SWT.HORIZONTAL));
	    
	    Canvas canvas=new Canvas(composite, SWT.BORDER | SWT.FULL_SELECTION);
	    canvas.setLayout(new FillLayout(SWT.HORIZONTAL));
	    
	    final LightweightSystem lws = new LightweightSystem(canvas);
	    
	    //create a new XY Graph.
	    chart = new XYGraph();
	    chart.setTitle("processlist");
	    
	    chart.primaryXAxis.setShowMajorGrid(true);
	    chart.primaryXAxis.setAutoScale(true);
	    chart.primaryXAxis.setAutoScaleThreshold(0.0);
	    
	    chart.primaryYAxis.setShowMajorGrid(true);
	    chart.primaryYAxis.setAutoScale(true);
	    chart.primaryYAxis.setAutoScaleThreshold(0.0);
	    
	    
	    
	    //create a trace data provider, which will provide the data to the trace.
	    sleepsprovider = new CircularBufferDataProvider(false);
	    queriesprovider = new CircularBufferDataProvider(false);
//	    traceDataProvider.setBufferSize(ldba.getFirst().getLastProcessListCount());           
//	    traceDataProvider.setCurrentYDataArray(ldba.getFirst().getGlobalProcessListStateCount(DBA.MYSQL_STATUS_SLEEP));
//	    traceDataProvider.setCurrentXDataArray(new double[]{1, 2, 3, 4, 5});
	    //traceDataProvider.setCurrentYDataArray(new double[]{11, 44, 55, 65, 88, 98, 52, 23}); 
	    //traceDataProvider.setCurrentXDataArray(new double[]{11, 44, 55, 65, 88, 98, 99, 100});
	    
	    //TODO: querytime acomulat? meantime? meantime by host? ...
	    
	    //create the trace
	    Trace sleeps = new Trace("#sleeps", 
	    		chart.primaryXAxis, chart.primaryYAxis, sleepsprovider);
	    
	    Trace queries = new Trace("#queries", 
	    		chart.primaryXAxis, chart.primaryYAxis, queriesprovider);      
	    
	    //set trace property
	    //sleeps.setPointStyle(PointStyle.CIRCLE);
	    sleeps.setTraceType(TraceType.AREA);
	    sleeps.setLineWidth(15);
	    
	    queries.setPointStyle(PointStyle.CIRCLE);
	    
	    //add the trace to xyGraph
	    chart.addTrace(sleeps);  
	    chart.addTrace(queries);  
	    
	    //afegim toolbar
	    ToolbarArmedXYGraph toolbarArmedXYGraph = new ToolbarArmedXYGraph(chart);
	    lws.setContents(toolbarArmedXYGraph);
	    //sense toolbar
//	    lws.setContents(chart);
	    
	    
	    Composite composite_sub_pl = new Composite(composite, SWT.NONE);
	    composite_sub_pl.setLayout(new FillLayout(SWT.HORIZONTAL));
	    //composite_sub_pl.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
	    		
	    Composite composite_meters = new Composite(composite_sub_pl, SWT.NONE);
	    composite_meters.setLayout(new FillLayout(SWT.VERTICAL));
	    //composite_meters.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
	    
	    /*
	    Label status = new Label(composite_1, SWT.NONE);
	    status.setText("fuck yeah!");
	    status.setBackgroundImage(SWTResourceManager.getImage("/home/jprats/Downloads/fuck_yeah.jpg"));
	    */
	    
	    canvas_pl_meter=new Canvas(composite_meters, SWT.NONE);
	    canvas_pl_meter.setLayout(new FillLayout(SWT.HORIZONTAL));
	    
	    canvas_tc_meter=new Canvas(composite_meters, SWT.NONE);
	    canvas_tc_meter.setLayout(new FillLayout(SWT.HORIZONTAL));
	    
	    composite_slows_table =new Composite(composite_sub_pl,SWT.NONE);
	    composite_slows_table.setLayout(new FillLayout(SWT.HORIZONTAL));
	    
	    
	    CTabFolder contenidor_taules_slows = new CTabFolder(composite_slows_table, SWT.BORDER);
	    //contenidor_taules_slows.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
	    CTabItem tabslowsRT = new CTabItem(contenidor_taules_slows, SWT.NONE);
	    tabslowsRT.setText("realtime processlist");
	    
		CTabItem tabslowsG = new CTabItem(contenidor_taules_slows, SWT.NONE);
		tabslowsG.setText("Top Queries");
	    
		//pestanya per defecte
		contenidor_taules_slows.setSelection(tabslowsRT);
		
	    sqtable=new SlowQueriesTable(contenidor_taules_slows);
	    tabslowsG.setControl(sqtable.getTable());
	    
	    rtsqtable=new SlowQueriesTable(contenidor_taules_slows);
	    tabslowsRT.setControl(rtsqtable.getTable());
	    
	    shell.setTabList(new Control[]{contenidor});
	    
		//shell.pack (); //tamany mínim
		this.open=true;
		while (!shell.isDisposed())
		{
			//mentre no tanqui la finestre
			if (!display.readAndDispatch ()) display.sleep ();
		}
		this.open=false;
	}

	/**
	 * @return esta arrancada linterficie gràfica?
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * obté una mostra mitjançant el WillyWorker
	 * @param WillyWorker a executar
	 */
	public void addData(WillyWorker ww)
	{
		display.asyncExec(ww);
	}
	
	public void updateValues(MySQLState ms)
	{
		if(this.plcmeter==null) plcmeter=new ProcessListCountMeter(canvas_pl_meter);
		if(this.tcmeter==null) tcmeter=new ThreadCacheMeter(canvas_tc_meter);
		
		this.plcmeter.updateValue(ms);
		this.tcmeter.updateValue(ms);
	}
	
	public void updateQueries(LinkedList<DBQuery> ldbq)
	{
		this.rtsqtable.resetUpdateList(ldbq);
		this.sqtable.updateList(ldbq);
	}
	
	public void setTitle(String text)
	{
		shell.setText(text);
	}
}
