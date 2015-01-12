package com.otro;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.openi.implement.HealthServiceImpl;
import com.openi.interfaces.HealthService;

import eu.openiict.client.model.OPENiObject;

/**
 * Drawing the chart
 * @author CGI
 *
 */
public class DateChartActivity extends Activity {
	private GraphicalView mChart;
	private static final String HL = "HealthService";	

	String cloudletId;
	String measurementType;
	String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_chart);

		// Rreceiving cloudletId, measuremtn type and user's token
		Bundle reicieveParams = getIntent().getExtras();

		//cloudlet real del usuario
		cloudletId = reicieveParams.getString("cloudletId");
		token = reicieveParams.getString("token");
		//String cloudletId="c_0967cab17835472735903ac113ce7775-90";
		
		measurementType = reicieveParams.getString("measurement");

		if (!cloudletId.equals("")) {
			Toast.makeText(
					getApplicationContext(),
					"CloudletId :" + cloudletId + "  Measurement : "
							+ measurementType, Toast.LENGTH_LONG).show();
			AsyncTaskRunner runner = new AsyncTaskRunner();

			runner.execute(cloudletId, measurementType); 
		} else {
			Toast.makeText(getApplicationContext(),
					"It was not possible to find the user's cloudlet",
					Toast.LENGTH_LONG).show();
		}
	}


	
	public void btnChartClicked(View v) {
		finish();
	}

	/**
	 * Gets OPENiObjects from the given cloudlet 
	 */

	private class AsyncTaskRunner extends AsyncTask<String, String, TreeMap<Date, Double>> {

		private ProgressDialog dialog;
		
		/*
		 *  (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */

		@Override
		protected TreeMap<Date, Double> doInBackground(String... params) {
			String cloudletId = params[0];
			String measurement = params[1];
			//Map<Date, Double> data = getObjectsFromCloudlet(cloudletId,measurement );
			TreeMap<Date, Double> result =getObjectsToDraw(cloudletId, measurement);
			
			return result;
		}
		
		
		/**
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */

		@Override
		protected void onPostExecute(TreeMap<Date, Double> result) {
			// execution of result of Long time consuming operation

			
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			//if (result!=null && result.size()>0){
			if (!result.isEmpty()){
				//	After getting objects, we draw them into the chart
				try{
					drawChart(result,measurementType);
				}catch (IndexOutOfBoundsException e) {
					e.getLocalizedMessage();		
				}
			}else{
					Toast.makeText(
							getApplicationContext(),
							"There is not "+ measurementType+" information into the user's clouldet",
							Toast.LENGTH_LONG).show();
				}
		}
		

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(DateChartActivity.this);
			dialog.setMessage("Getting cloudlet info. Please wait.");
			dialog.show();
		}

	}	
	
	private TreeMap<Date,Double> getObjectsToDraw(String cloudletId, String measurement){
		TreeMap<Date,Double> content = new TreeMap<Date, Double>();
		HealthService hs = new HealthServiceImpl();
		List<OPENiObject> objects = hs.getObjectsFromMyCloudlet(cloudletId, measurement);
		
		int hasta = objects.size();
		String resultado = "";
		Double f2 = null;
		Date fecha = null;

		
		if("BMI".equalsIgnoreCase(measurement)){
			hs.getBMIFromCloudlet(cloudletId);
		}else{
			for (int i = 0; i < hasta; i++) {
				OPENiObject openiobj = objects.get(i);
				// Get Data devuelve vacío
				String objectId = openiobj.getId();
	
				OPENiObject obj = hs.getOPENiObjectById(cloudletId, objectId);
	
				Map mapa = obj.getData();
				fecha = new Date(obj.getDateModified().toString());
				resultado = (String) mapa.get("result");
				if (resultado!=null && resultado.trim().length()>0){
					f2 = Double.parseDouble(resultado);
					content.put(fecha, f2);
				}
				
			}
			// If there are no data, write them
			if (content.isEmpty()){
				content.putAll(hs.dataWhenNoCloudlet(measurement));
			}
		
		}
		return content;
		
	}
	
	
	private void drawChart(TreeMap<Date, Double> result,String measurementType) {
		// "c_088e818f2ec0e68379c891c3ca2a0860-90"
		
		DecimalFormat numFormat = new DecimalFormat("000.###");
		// Renderer para la serie de datos
		XYSeriesRenderer chartRenderer = new XYSeriesRenderer();
		chartRenderer.setColor(Color.BLACK);
		chartRenderer.setPointStyle(PointStyle.CIRCLE);
		chartRenderer.setFillPoints(true);
		chartRenderer.setLineWidth(2);		
		chartRenderer.setChartValuesFormat(numFormat);
		// dates has to be properly ordered if this is set as true. Disordered dates throw IndoexOutOfBounds exception 
		chartRenderer.setDisplayChartValues(true);
		chartRenderer.setShowLegendItem(false);
		chartRenderer.setDisplayBoundingPoints(true);
		chartRenderer.setDisplayChartValuesDistance(1);

		TimeSeries timeSeries = new TimeSeries(measurementType);

		// Adding visualization
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setChartTitle(measurementType);
		multiRenderer.setXTitle(null); // siempre
		multiRenderer.setYTitle(measurementType);
		multiRenderer.setZoomButtonsVisible(true);

		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setBackgroundColor(Color.WHITE);
		multiRenderer.setMarginsColor(Color.WHITE);
		multiRenderer.setLabelsColor(Color.BLUE);
		multiRenderer.setAxesColor(Color.BLUE);
		multiRenderer.setScale((float) 1.0);
		

		Log.v(HL, "graph features set ");

		try {

			for (Map.Entry<Date, Double> entry : result.entrySet()) {
				timeSeries.add(entry.getKey(), entry.getValue());
			}	 	
			
			// Adding data to a dataset

			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			dataset.addSeries(timeSeries);

			// Adding the serie renderer to the  multirenderer
			multiRenderer.addSeriesRenderer(chartRenderer);

			// Getting a reference to LinearLayout of the MainActivity Layout
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
			Log.v(HL, "graph created ");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
