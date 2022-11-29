package spatial;


import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.colors.XChartSeriesColors;


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
	
		for(int i=2; i<500; i=i+50) {
			int coopCycleSum=0, defectcycleSum=0;

			for(int j=i; j<i+48; j++) {
				coopCycleSum = coopCycleSum + coopPop.get(j);	
				defectcycleSum = defectcycleSum + defectPop.get(j);
			}
			coopPopMean.add((float) (coopCycleSum/50));
			defectPopMean.add((float) (defectcycleSum/50));
			//Calculate percentages
			coopPopMeanPer.add(((float) (coopCycleSum/50) / ((float) (coopCycleSum/50) +(defectcycleSum/50)))*100);
			defectPopMeanPer.add(((float) (defectcycleSum/50) / ((float) (coopCycleSum/50) +(defectcycleSum/50)))*100);
			
			
		}
		
		//Calculate the average cooperation population
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
		 
	}



	

}
