package willy.server.gui.tabs;

import java.util.LinkedList;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.csstudio.swt.xygraph.dataprovider.Sample;
//import org.csstudio.swt.xygraph.figures.ToolbarArmedXYGraph;
import org.csstudio.swt.xygraph.figures.ToolbarArmedXYGraph;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.XYGraph;
//import org.csstudio.swt.xygraph.figures.Trace.PointStyle;
import org.csstudio.swt.xygraph.figures.Trace.TraceType;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import willy.server.dba.DBA;
import willy.server.gui.ProcessListCountMeter;
import willy.server.gui.SlowQueriesTable;
import willy.server.gui.ThreadCacheMeter;
import willy.server.sql.extra.DBQuery;
import willy.server.sql.extra.MySQLState;
import willy.singletons.GUIImages;
import willy.singletons.GUIMessages;

public class ProcessListTab extends GenericTab{

	protected GUIMessages rb=null;
	protected GUIImages rm=null;
	
	protected long xcounter=0; 
	protected XYGraph chart=null;
	
	protected CircularBufferDataProvider sleepsprovider=null;
	protected CircularBufferDataProvider queriesprovider=null;
	
	protected ProcessListCountMeter plcmeter=null;
	protected ThreadCacheMeter tcmeter=null;
	protected SlowQueriesTable sqtable=null;
	protected SlowQueriesTable rtsqtable=null;
	
	protected Canvas canvas_pl_meter=null;
	protected Canvas canvas_tc_meter=null;
	protected Composite composite_slows_table=null;
	protected Composite composite_meters=null;
	protected Composite composite_sub_pl=null;
	
	protected ToolbarArmedXYGraph toolbarArmedXYGraph=null;
	
	protected CTabFolder contenidor_taules_slows=null;
	protected CTabItem tabslowsRT=null;
	protected CTabItem tabslowsG=null;
	
	public 	ProcessListTab(Composite comp)
	{
		rb=GUIMessages.getInstance();
		rm=GUIImages.getInstance(comp.getShell());
		
		composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		
		Canvas canvas=new Canvas(composite, SWT.BORDER | SWT.FULL_SELECTION);
		canvas.setLayout(new FillLayout(SWT.HORIZONTAL));

		final LightweightSystem lws = new LightweightSystem(canvas);

		//create a new XY Graph.
		chart = new XYGraph();
		chart.setTitle("processlist");

		chart.primaryXAxis.setShowMajorGrid(true);
		chart.primaryXAxis.setAutoScale(true);
		chart.primaryXAxis.setAutoScaleThreshold(0.0);
		chart.primaryXAxis.setDateEnabled(true);

		chart.primaryYAxis.setShowMajorGrid(true);
		chart.primaryYAxis.setAutoScale(true);
		chart.primaryYAxis.setAutoScaleThreshold(0.0);
		//chart.primaryYAxis.setShowMinorGrid(false);

		

		//create a trace data provider, which will provide the data to the trace.
		sleepsprovider = new CircularBufferDataProvider(true);
		queriesprovider = new CircularBufferDataProvider(true);
		sleepsprovider.setBufferSize(10000);
		queriesprovider.setBufferSize(10000);
		//	    traceDataProvider.setBufferSize(ldba.getFirst().getLastProcessListCount());           
		//	    traceDataProvider.setCurrentYDataArray(ldba.getFirst().getGlobalProcessListStateCount(DBA.MYSQL_STATUS_SLEEP));
		//	    traceDataProvider.setCurrentXDataArray(new double[]{1, 2, 3, 4, 5});
		//traceDataProvider.setCurrentYDataArray(new double[]{11, 44, 55, 65, 88, 98, 52, 23}); 
		//traceDataProvider.setCurrentXDataArray(new double[]{11, 44, 55, 65, 88, 98, 99, 100});

		//create the trace
		Trace sleeps = new Trace("#sleeps", 
				chart.primaryXAxis, chart.primaryYAxis, sleepsprovider);

		Trace queries = new Trace("#queries", 
				chart.primaryXAxis, chart.primaryYAxis, queriesprovider);      

		//set trace property
		//sleeps.setPointStyle(PointStyle.CIRCLE);
		sleeps.setTraceType(TraceType.AREA);
		sleeps.setLineWidth(15);

		//queries.setPointStyle(PointStyle.CIRCLE);

		//add the trace to xyGraph
		chart.addTrace(sleeps);  
		chart.addTrace(queries);  

		//afegim toolbar
		toolbarArmedXYGraph = new ToolbarArmedXYGraph(chart);
		lws.setContents(toolbarArmedXYGraph);
		//sense toolbar
		//lws.setContents(chart);
		
		composite_sub_pl = new Composite(composite, SWT.NONE);
		composite_sub_pl.setLayout(new FormLayout());
		//composite_sub_pl.setLayout(new FormLayout());
		//http://book.javanb.com/swt-the-standard-widget-toolkit/ch15lev1sec8.html



		composite_meters = new Composite(composite_sub_pl, SWT.NONE);
		composite_meters.setLayout(new FillLayout(SWT.VERTICAL));

		FormData fd_composite_meters = new FormData();
		fd_composite_meters.top = new FormAttachment(0);
		fd_composite_meters.bottom = new FormAttachment(100);
		fd_composite_meters.left = new FormAttachment(0);
		composite_meters.setLayoutData(fd_composite_meters);

		canvas_pl_meter=new Canvas(composite_meters, SWT.NONE);
		canvas_pl_meter.setLayout(new FillLayout(SWT.HORIZONTAL));

		canvas_tc_meter=new Canvas(composite_meters, SWT.NONE);
		canvas_tc_meter.setLayout(new FillLayout(SWT.HORIZONTAL));

		composite_slows_table =new Composite(composite_sub_pl,SWT.NONE);
		composite_slows_table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fd_composite_meters.right = new FormAttachment(100, -360);
		composite_slows_table.setLayout(new FillLayout(SWT.HORIZONTAL));


		FormData fd_composite_slows_table = new FormData();
		fd_composite_slows_table.right = new FormAttachment(0, 360, SWT.RIGHT);
		fd_composite_slows_table.bottom = new FormAttachment(100);
		fd_composite_slows_table.left = new FormAttachment(composite_meters, 6);
		fd_composite_slows_table.top = new FormAttachment(100, -197);

		composite_slows_table.setLayoutData(fd_composite_slows_table);


		contenidor_taules_slows = new CTabFolder(composite_slows_table, SWT.NONE);
		//contenidor_taules_slows.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		//contenidor_taules_slows.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		tabslowsRT = new CTabItem(contenidor_taules_slows, SWT.NONE);
		tabslowsRT.setText(this.rb.getString("pestanyes.global.processlist.rtqueries"));

		tabslowsG = new CTabItem(contenidor_taules_slows, SWT.NONE);
		tabslowsG.setText(this.rb.getString("pestanyes.global.processlist.topqueries"));

		//CTabItem tabkillemall = new CTabItem(contenidor_taules_slows, SWT.NONE);
		//tabkillemall.setText(this.rb.getString("pestanyes.global.processlist.killemall"));
		//pestanyes.global.processlist.killemall

		//pestanya per defecte
		contenidor_taules_slows.setSelection(tabslowsRT);

		sqtable=new SlowQueriesTable(contenidor_taules_slows);
		tabslowsG.setControl(sqtable.getComposite());

		rtsqtable=new SlowQueriesTable(contenidor_taules_slows);
		tabslowsRT.setControl(rtsqtable.getComposite());
	}

	public void updateValues(MySQLState ms)
	{
		if(this.plcmeter==null) plcmeter=new ProcessListCountMeter(canvas_pl_meter);
		if(this.tcmeter==null) tcmeter=new ThreadCacheMeter(canvas_tc_meter);

		this.plcmeter.updateValue(ms);
		this.tcmeter.updateValue(ms);
		
		sleepsprovider.addSample(
								new Sample(
								ms.getDBProcessList().getTimestamp()*1000,
								ms.getDBProcessList().getStateCount(DBA.MYSQL_STATUS_SLEEP)
								)
								);

		queriesprovider.addSample(
								new Sample(
								ms.getDBProcessList().getTimestamp()*1000,
								ms.getDBProcessList().getStateCount(DBA.MYSQL_STATUS_QUERY)
								)
								);
	}

	public void updateQueries(LinkedList<DBQuery> ldbq)
	{
		this.rtsqtable.resetUpdateList(ldbq);
		this.sqtable.updateList(ldbq);
	}

}
