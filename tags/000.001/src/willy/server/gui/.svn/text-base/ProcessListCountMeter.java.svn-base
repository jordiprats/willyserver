package willy.server.gui;

import org.csstudio.swt.widgets.figures.MeterFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Canvas;

import willy.server.sql.extra.MySQLState;

public class ProcessListCountMeter {
	
	protected MeterFigure chart=null;
	LightweightSystem lws;
	protected boolean stylish=false;

	public ProcessListCountMeter(Canvas canvas)
	{
		this.lws = new LightweightSystem(canvas);

		this.chart = new MeterFigure();

		this.lws.setContents(chart);
	}

	public void updateValue(MySQLState ms)
	{
		if(!this.stylish) this.setStyle(ms);
		
		this.chart.setValue(ms.getDBProcessList().getCount());
	}
	
	private void setStyle(MySQLState ms)
	{
		/*
		 * 		meterFigure.setBackgroundColor(
		 * 				XYGraphMediaFactory.getInstance().getColor(255, 255, 255));
		 * 
		 * meterFigure.setBorder(new SchemeBorder(SchemeBorder.SCHEMES.ETCHED));
		 * meterFigure.setRange(-100, 100);
		 * meterFigure.setLoLevel(-50);
		 * meterFigure.setLoloLevel(-80);
		 * meterFigure.setHiLevel(60);
		 * meterFigure.setHihiLevel(80);
		 * meterFigure.setMajorTickMarkStepHint(50);
		 * 
		 */
		double max=ms.getMySQLVariables().max_connections()+1;
		//+1 perque root sempre te reservada una conexió
		this.chart.setRange(0, max);
//		this.chart.setLoLevel(max/4);
//		this.chart.setLoloLevel(max/8);
		//this.chart.setLoLevel(0);
		//this.chart.setLoloLevel(0);
		this.chart.setShowLo(false);
		this.chart.setShowLolo(false);
		this.chart.setHiLevel((max/8)*5);
		this.chart.setHihiLevel((max/8)*6);
		this.chart.setValueLabelFormat("max_connections\ncurrent value: ");
		//this.chart.setValueLabelVisibility(false);
		

		
		this.stylish=true;
	}

}
