/**************************************************************************
  	dla_fractal is a program that implements a model to generate DLA 
  	aggregation of particles. At this stage the model implements 4 types 
  	of particle movements:
  	1) Snow-flake
  	2) Random
  	3) Balistic
  	4) Spiral
  	The program is implemented with the MVC type of architecture.
  	This class is not part of the MVC architecture and it's only purpose is
  	for the graph generation.
  	
    Copyright (C) 2014  Stefano Bettinelli

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package dla_franctal;

//import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
//import org.jfree.ui.RefineryUtilities;
//import org.jfree.ui.Spacer;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class LineChart extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1622147701910445504L;

	public LineChart(final String title, ArrayList<Double> bbAreaRatio, ArrayList<Integer> staticParticlesAtCurrentTick) {

		super(title);

		final XYDataset dataset = createBBAreaRationDataset(bbAreaRatio, staticParticlesAtCurrentTick);
		final JFreeChart chart = createChart(dataset,null,null);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(650, 350));
		setContentPane(chartPanel);
		try {
			ChartUtilities.saveChartAsJPEG(new File("./"+title+".jpeg"), chart, 650, 350);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LineChart(final String title,Map<Integer,Integer> snowMap,Map<Integer,Integer> randMap,Map<Integer,Integer> balisticMap,Map<Integer,Integer> spiralMap) {

		super(title);

		final XYDataset dataset = createSimulationGraphs(snowMap,randMap,balisticMap,spiralMap);
		final JFreeChart chart = createChart(dataset,"Particles","Iterations");
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(650, 350));
		setContentPane(chartPanel);
		try {
			ChartUtilities.saveChartAsJPEG(new File("./"+title+".jpeg"), chart, 650, 350);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XYDataset createBBAreaRationDataset(ArrayList<Double> bbAreaRatio, ArrayList<Integer> staticParticlesAtCurrentTick){
		final XYSeries currentBBAreaRatio = new XYSeries("");
		for(int i=0; i<bbAreaRatio.size(); i++){
			currentBBAreaRatio.add(staticParticlesAtCurrentTick.get(i), bbAreaRatio.get(i) );
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(currentBBAreaRatio);
		return dataset;
	}

	public XYDataset createSimulationGraphs(Map<Integer,Integer> snowMap,Map<Integer,Integer> randMap,Map<Integer,Integer> balisticMap,Map<Integer,Integer> spiralMap){
		final XYSeries snowFlakeSeries = new XYSeries("snowFlake");
		final XYSeries randomSeries = new XYSeries("randomSeries");
		final XYSeries balisticSeries = new XYSeries("balisticSeries");
		final XYSeries spiralSeries = new XYSeries("spiralSeries");
		for (int i = 10000; i <= 60000; i+=10000) {
			snowFlakeSeries.add(i, snowMap.get(new Integer(i)) );
			randomSeries.add(i, randMap.get(new Integer(i)) );
			balisticSeries.add(i, balisticMap.get(new Integer(i)) );
			spiralSeries.add(i, spiralMap.get(new Integer(i)) );
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(snowFlakeSeries);
		dataset.addSeries(randomSeries);
		dataset.addSeries(balisticSeries);
		dataset.addSeries(spiralSeries);
		return dataset;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset  the data for the chart.
	 * @param yS 
	 * @param xS 
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final XYDataset dataset, String xS, String yS) {

		JFreeChart chart = null;

		if( xS == null && yS == null ){
			// create the chart...
			chart = ChartFactory.createXYLineChart(
					"",      // chart title
					"DLA_particles",                      // x axis label
					"DLA_particles/DLA_BB_Area",                      // y axis label
					dataset,                  // data
					PlotOrientation.VERTICAL,
					true,                     // include legend
					true,                     // tooltips
					false                     // urls
					);
		}
		else{
			chart = ChartFactory.createXYLineChart(
					"",      // chart title
					xS,                      // x axis label
					yS,                      // y axis label
					dataset,                  // data
					PlotOrientation.VERTICAL,
					true,                     // include legend
					true,                     // tooltips
					false                     // urls
					);
		}

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		//        final StandardLegend legend = (StandardLegend) chart.getLegend();
		//      legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		//        final XYPlot plot = chart.getXYPlot();
		//       // int seriesCount = plot.getSeriesCount();
		//        for (int i = 0; i < seriesCount; i++) {
		//        	plot.getRenderer().setSeriesStroke(i, new BasicStroke(
		//        	        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
		//        	        1.0f, new float[] {6.0f, 6.0f}, 0.0f
		//        	    ));
		//        }
		//plot.setBackgroundPaint(Color.lightGray);
		//plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		//plot.setDomainGridlinePaint(Color.white);
		//plot.setRangeGridlinePaint(Color.white);



		//final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		//renderer.setSeriesLinesVisible(0, false);
		//renderer.setSeriesShapesVisible(1, false);
		//plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		//        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		//        rangeAxis.setRange(0.0, 1.0);
		//rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.*/

		return chart;

	}

}