package com.openi.implement;

import java.text.NumberFormat;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;


public class DrawTwoLinesChart {

		/**
		 * 
		 * Create a dataset with all privided series
		 */
		public XYMultipleSeriesDataset createDataset(List<TimeSeries> timeSeries){
			
			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			
			int size = timeSeries.size();
			for(int i=0;i<size;i++){
				dataset.addSeries(timeSeries.get(i));
			}
			
			return dataset;
			
		}
		
		/**
		 * Create a multipleRenderer with the given titles.
		 * 
		 * @param title
		 * @param metric
		 * @return
		 */
		public XYMultipleSeriesRenderer createMultipleSeriesRenderer(String title, String metric){
		
			XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
			multiRenderer.setChartTitle(title);
			multiRenderer.setXTitle(null); // siempre
			multiRenderer.setYTitle(metric);
			multiRenderer.setZoomButtonsVisible(true);
			multiRenderer.setApplyBackgroundColor(true);
			multiRenderer.setBackgroundColor(Color.WHITE);
			multiRenderer.setMarginsColor(Color.WHITE);
			
			return multiRenderer;
		}

		public XYSeriesRenderer createSeriesRenderer(int color,NumberFormat numFormat){
			XYSeriesRenderer chartRenderer = new XYSeriesRenderer();
			chartRenderer.setColor(color);
			chartRenderer.setPointStyle(PointStyle.CIRCLE);
			chartRenderer.setFillPoints(true);
			chartRenderer.setLineWidth(2);
			chartRenderer.setChartValuesFormat(numFormat);
			chartRenderer.setDisplayChartValues(true);
			chartRenderer.setShowLegendItem(true);
			chartRenderer.setDisplayBoundingPoints(true);
			
			return chartRenderer;
		}
}
