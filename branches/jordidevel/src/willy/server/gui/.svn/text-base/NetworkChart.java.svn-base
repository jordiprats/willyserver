package willy.server.gui;

import java.awt.Frame;
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
	
	protected TimeSeriesCollection data=null;
	protected TimeSeries dataavg=null;
	
	public Composite getComposite()
	{
		return this.composite;
	}
	
	public NetworkChart(Composite comp)
	{
		series = new TimeSeries("random");
        
		dataavg = MovingAverage.createMovingAverage(
                series, "average", 2, 2
            );
		
		data = new TimeSeriesCollection();
        data.addSeries(series);
		data.addSeries(dataavg);
        
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
        
        jfreeChartPanel.setRangeZoomable(false); //deshabilitar zoom en Y
        
        //jfreeChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        frame.add(jfreeChartPanel);

	}
	
	public void updateValues()
	{
		double value=(Math.random()*10+Math.random()*3);
		series.addOrUpdate(new Millisecond(), 
				value);
		
		//System.out.println("dades random chart: "+new Millisecond().toString()+" ### "+value);
        
        
        dataavg.addAndOrUpdate(series);
	}

}
