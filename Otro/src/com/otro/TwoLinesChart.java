package com.otro;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.openi.implement.DrawTwoLinesChart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class TwoLinesChart extends Activity {
	private GraphicalView mChart;
	DrawTwoLinesChart drawing = new DrawTwoLinesChart();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_lines_chart);
		
		DecimalFormat numFormat = new DecimalFormat("000.###");
		
		String measurementType1 = "Medida1";
		String measurementType2 = "Medida2";
		
		// Renderer para la serie de datos (series 1 y 2)
		/*
		XYSeriesRenderer chartRenderer = new XYSeriesRenderer();
		chartRenderer.setColor(Color.BLACK);
		chartRenderer.setPointStyle(PointStyle.CIRCLE);
		chartRenderer.setFillPoints(true);
		chartRenderer.setLineWidth(2);
		chartRenderer.setChartValuesFormat(numFormat);
		chartRenderer.setDisplayChartValues(true);
		chartRenderer.setShowLegendItem(true);
		chartRenderer.setDisplayBoundingPoints(true);
		// chartRenderer.setDisplayChartValuesDistance(1);
		
		XYSeriesRenderer chartRenderer2 = new XYSeriesRenderer();
		chartRenderer2.setColor(Color.BLUE);
		chartRenderer2.setPointStyle(PointStyle.SQUARE);
		chartRenderer2.setFillPoints(true);
		chartRenderer2.setLineWidth(2);
		chartRenderer2.setChartValuesFormat(numFormat);
		chartRenderer2.setDisplayChartValues(true);
		chartRenderer2.setShowLegendItem(true);
		chartRenderer2.setDisplayBoundingPoints(true);
		// chartRenderer.setDisplayChartValuesDistance(1);
		*/
		

		// Adding visualization: renderer1 y renderer2
/*		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setChartTitle(measurementType1 + "/"+measurementType2);
		multiRenderer.setXTitle(null); // siempre
		multiRenderer.setYTitle(measurementType1);
		multiRenderer.setZoomButtonsVisible(true);
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setBackgroundColor(Color.WHITE);
		multiRenderer.setMarginsColor(Color.WHITE);*/
	
		
/*		TimeSeries timeSeries = new TimeSeries(measurementType1);
		TimeSeries timeSeries2 = new TimeSeries(measurementType2);
		*//**
		 * Rellenar los datos en sí
		 *//*
				
		 timeSeries.add(new Date("15/10/2014"),12);
		 timeSeries.add(new Date("16/10/2014"),12);
		 timeSeries.add(new Date("18/10/2014"),12.5);
		 timeSeries.add(new Date("19/10/2014"),12);
		 
		 
		 timeSeries2.add(new Date("15/10/2014"),6);
		 timeSeries2.add(new Date("16/10/2014"),6.2);
		 timeSeries2.add(new Date("18/10/2014"),6.5);
		 timeSeries2.add(new Date("19/10/2014"),6);*/
		
		/*XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(timeSeries);
		dataset.addSeries(timeSeries2);*/
		
		// Adding the serie renderer to the  multirenderer
/*		multiRenderer.addSeriesRenderer(chartRenderer);
		multiRenderer.addSeriesRenderer(chartRenderer2);*/
		
		/*********************************************************************/
		
		TimeSeries timeSeries = new TimeSeries(measurementType1);
		TimeSeries timeSeries2 = new TimeSeries(measurementType2);
		/**
		 * Rellenar los datos en sí
		 */
				
		 timeSeries.add(new Date("15/10/2014"),12);
		 timeSeries.add(new Date("16/10/2014"),12);
		 timeSeries.add(new Date("18/10/2014"),12.5);
		 timeSeries.add(new Date("19/10/2014"),12);
		 
		 
		 timeSeries2.add(new Date("15/10/2014"),6);
		 timeSeries2.add(new Date("16/10/2014"),6.2);
		 timeSeries2.add(new Date("18/10/2014"),6.5);
		 timeSeries2.add(new Date("19/10/2014"),6);
		
		List<TimeSeries> timeSeriesList = new ArrayList<TimeSeries>();
		timeSeriesList.add(timeSeries);
		timeSeriesList.add(timeSeries2);
		
		XYSeriesRenderer chartRenderer = drawing.createSeriesRenderer(Color.BLACK, numFormat);
		XYSeriesRenderer chartRenderer2 = drawing.createSeriesRenderer(Color.BLUE, numFormat);
		
		XYMultipleSeriesDataset dataset = drawing.createDataset(timeSeriesList);
		
		XYMultipleSeriesRenderer multiRenderer = drawing.createMultipleSeriesRenderer(measurementType1 + "/"+measurementType2, "mmGH");
		multiRenderer.addSeriesRenderer(chartRenderer);
		multiRenderer.addSeriesRenderer(chartRenderer2);
		
		
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
		chartContainer.setBackgroundColor(Color.WHITE);
		chartContainer.setDrawingCacheBackgroundColor(Color.WHITE);

		// Creating a Time Chart
		mChart = ChartFactory.getTimeChartView(getBaseContext(), dataset,
				multiRenderer, "dd-MM-yyyy hh:mm");
		mChart.setBackgroundColor(Color.WHITE);
		mChart.setPadding(2, 2, 2, 10);
		mChart.setDrawingCacheBackgroundColor(Color.WHITE);

		// Adding the Line Chart to the LinearLayout
		chartContainer.addView(mChart);
	}
}
