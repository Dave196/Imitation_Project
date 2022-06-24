package spatial;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.bar.BarChart01;
import org.knowm.xchart.demo.charts.bar.BarChart06;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class endThread implements Runnable {
	
	public static List<Integer> coopPop = new ArrayList<Integer>();
	public static List<Integer> defectPop = new ArrayList<Integer>();
	public static List<Integer> rounds = new ArrayList<Integer>();
	public static List<String> cycles = new ArrayList<String>();
	public static List<Float> coopPopMean = new ArrayList<Float>();
	public static List<Float> defectPopMean = new ArrayList<Float>();
	public static List<Float> coopPopMeanPer = new ArrayList<Float>();
	public static List<Float> defectPopMeanPer = new ArrayList<Float>();
		
	@Override
	public void run() {
		float meanCoopPop;
		int coopSum =0, defectSum=0 ;
		// TODO Auto-generated method stub
		//System.out.println(coopPop);
		//System.out.println(defectPop);
		//System.out.println(rounds);
		//System.out.println(cycles);
		
		for(int i=2; i<500; i=i+50) {
			int coopCycleSum=0, defectcycleSum=0;
			//System.out.println(i);
			for(int j=i; j<i+48; j++) {
				coopCycleSum = coopCycleSum + coopPop.get(j);	
				defectcycleSum = defectcycleSum + defectPop.get(j);
			}
			coopPopMean.add((float) (coopCycleSum/50));
			defectPopMean.add((float) (defectcycleSum/50));
			//Calulate percentaged
			coopPopMeanPer.add(((float) (coopCycleSum/50) / ((float) (coopCycleSum/50) +(defectcycleSum/50)))*100);
			defectPopMeanPer.add(((float) (defectcycleSum/50) / ((float) (coopCycleSum/50) +(defectcycleSum/50)))*100);
			
			
		}
		//System.out.println(coopPopMean);
		//System.out.println(defectPopMean);
		
		//Calulate the average cooperation population
		for(int i=0; i<coopPop.size(); i++) {
			coopSum = coopSum + coopPop.get(i);
			defectSum = defectSum + defectPop.get(i);
		}
		meanCoopPop = (((float) coopSum/coopPop.size() /((float) coopSum/coopPop.size() + defectSum/defectPop.size()))*100 );
		System.out.println("Mean cooperation: "+meanCoopPop);
	    
		 CategoryChart chart = new CategoryChartBuilder().width(1000).height(600).title("Mean population of cooperation and defection in various cycles (Test 8)").xAxisTitle("cycle").yAxisTitle("Mean Population proportion (%)").build();
		 
		 chart.getStyler().setStacked(true);
		 
		 // Series
		 CategorySeries cooperation = chart.addSeries("Cooperation", cycles, coopPopMeanPer);
		 cooperation.setFillColor(XChartSeriesColors.GREEN);
		 
		 CategorySeries defection = chart.addSeries("Defection", cycles, defectPopMeanPer);
		 defection.setFillColor(XChartSeriesColors.RED);
	
		 new SwingWrapper<CategoryChart>(chart).displayChart();
		 
	    //line graph of population distance 
	    /*final XYChart chartone = new XYChartBuilder().width(600).height(400).title("Grapth representing the conflict between cooperative and defective agents").xAxisTitle("Rounds").yAxisTitle("Population").build();
	    
	    XYSeries cooperationone = chartone.addSeries("Cooperation",coopPop);
	    cooperationone.setLineColor(XChartSeriesColors.GREEN);
	    cooperationone.setMarker(SeriesMarkers.NONE);
	    cooperationone.setLineStyle(SeriesLines.SOLID);
	    
	    XYSeries defectionone = chartone.addSeries("Defection", defectPop);
	    defectionone.setLineColor(XChartSeriesColors.RED);
	    defectionone.setMarker(SeriesMarkers.NONE);
	    defectionone.setLineStyle(SeriesLines.SOLID);
	    
		new SwingWrapper<XYChart>(chartone).displayChart();*/
		
	}



	

}
