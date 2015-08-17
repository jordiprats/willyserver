package willy.server.gui;

import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Millisecond;
//http://www.java2s.com/Code/Java/Chart/Atimeserieschartwithamovingaverage.htm

public class NetworkChart {
	
	protected Composite composite=null;

	protected TimeSeries series=null;
	protected JFreeChart chart=null;
	
	TimeSeriesCollection data=null;
	
	public Composite getComposite()
	{
		return this.composite;
	}
	
	public NetworkChart(Composite comp)
	{
		series = new TimeSeries("caca");
        
        
		data = new TimeSeriesCollection();
        data.addSeries(series);
        
        

      
        
        
        chart = ChartFactory.createTimeSeriesChart(
            "XY Series Demo",
            "X", 
            "Y", 
            data,
            true,
            true,
            false
        );

        //final ChartPanel chartPanel = new ChartPanel(chart);

        composite = new Composite(comp, SWT.EMBEDDED | SWT.NO_BACKGROUND);
        composite.setLayout(new FillLayout(SWT.VERTICAL));
        Frame frame = SWT_AWT.new_Frame(composite);

        ChartPanel jfreeChartPanel = new ChartPanel(chart);
        //jfreeChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        frame.add(jfreeChartPanel);

	}
	
	public void updateValues()
	{
		double value=(Math.random()+10);
		series.addOrUpdate(new Millisecond(), 
				value);
		//System.out.println(new Millisecond().toString()+" ### "+value);
		
		if(series.getItemCount()==30)
		{
        final TimeSeries dataavg = MovingAverage.createMovingAverage(
                series, "Six Month Moving Average", 6, 0
            );
        
        data.addSeries(dataavg);
		}
		
		chart.fireChartChanged();
	}

}
