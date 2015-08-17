package willy.server.gui;

import org.csstudio.swt.widgets.figures.*;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Canvas;

import willy.server.sql.extra.MySQLState;

public class ThreadCacheMeter {

	protected MeterFigure chart=null;
	//protected GaugeFigure chart=null;
	LightweightSystem lws;
	protected boolean stylish=false;

	public ThreadCacheMeter(Canvas canvas)
	{
		this.lws = new LightweightSystem(canvas);

		this.chart = new MeterFigure();
		//this.chart = new GaugeFigure();

		this.lws.setContents(chart);
	}
	
	public void updateValue(MySQLState ms)
	{
		if(!this.stylish) this.setStyle(ms);
		
		if(ms.getMySQLStatus().getConnections()!=0)
			this.chart.setValue((int)(100.0-((double)ms.getMySQLStatus().getThreads_created()/(double)ms.getMySQLStatus().getConnections())*100.0));
		else
			this.chart.setValue(0);
		
//	    # Thread cache
//	    $mycalc{'thread_cache_hit_rate'} = int(100 - (($mystat{'Threads_created'} / $mystat{'Connections'}) * 100));

	}
	
	private void setStyle(MySQLState ms)
	{
			//ms.getMySQLVariables().getMax_connections()+1;
		//+1 perque root sempre te reservada una conexió
		this.chart.setRange(0, 100);
//		this.chart.setLoLevel(max/4);
//		this.chart.setLoloLevel(max/8);
		//this.chart.setLoLevel(0);
		//this.chart.setLoloLevel(0);
		//this.chart.setShowLo(false);
		//this.chart.setShowLolo(false);
		//this.chart.setHiLevel(0);
		//this.chart.setHihiLevel(0);
		this.chart.setShowHi(false);
		this.chart.setShowHihi(false);
		this.chart.setLoLevel(90);
		this.chart.setLoloLevel(100);
		this.chart.setValueLabelFormat("threadcache hitrate: ");
		//this.chart.setValueLabelVisibility(false);
		
		this.stylish=true;
	}


}
