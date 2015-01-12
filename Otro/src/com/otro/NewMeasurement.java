package com.otro;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openi.implement.HealthServiceImpl;
import com.openi.interfaces.HealthService;
import com.openi.interfaces.Measurements;

import eu.openiict.client.api.CloudletsApi;
import eu.openiict.client.api.ObjectsApi;
import eu.openiict.client.common.ApiException;
import eu.openiict.client.model.OPENiObject;
import eu.openiict.client.model.ObjectResponse;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewMeasurement extends Activity {
	final HealthService hl = new HealthServiceImpl();
	private Spinner selectMeasurement;
	private String metric;
	private String measurement;
	private EditText result;
	String cloudletId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_measurement);
		
		//Get the cloudletId
		Bundle reicieveParams = getIntent().getExtras();
		cloudletId = reicieveParams.getString("cloudletId");
		
		result = (EditText) findViewById(R.id.result);
		
		final List<String> values = hl.defineMeasurementsList();

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
		            						android.R.layout.simple_spinner_item, values);
		
		selectMeasurement = (Spinner)findViewById(R.id.spinner1);
		 
		adaptador.setDropDownViewResource(
		        android.R.layout.simple_spinner_dropdown_item);
		 
		selectMeasurement.setAdapter(adaptador);
		
		selectMeasurement.setOnItemSelectedListener(
		        new AdapterView.OnItemSelectedListener() {
		        
		        public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
		        	measurement = values.get(position);
		        	
		        	Map<String, String> dictionary = Measurements.getDictionary();
		        	metric = dictionary.get(measurement);
		        	
		        }
		 
		        public void onNothingSelected(AdapterView<?> parent) {
		            //TODO
		        }
		});
		
	}
	
	public void btnSaveClicked(View v){
		//TODO save info to the cloudlet and clean the fields
		/*
		Toast.makeText(getApplicationContext(), (CharSequence) result.getText() ,
				Toast.LENGTH_LONG).show();
		
		AsyncTaskRunner runner = new AsyncTaskRunner();

		runner.execute(cloudletId,measurement);
		*/ 
		
		Toast.makeText(getApplicationContext(), "Add new measurement" ,
				Toast.LENGTH_LONG).show();
		
	}
		
	public void btnCancelClicked(View v){
		//TODO back to MainOptionsActivity
	}
	
	private class AsyncTaskRunner extends AsyncTask<String, String, Map<Date, Float>> {

		@Override
		protected Map<Date, Float> doInBackground(String... arg0) {
			
			String cloudletId = arg0[0];
			String measurementType = arg0[1];
			
			Map<String, String> dictionary = Measurements.getDictionary();
        	metric = dictionary.get(measurementType);
 
			Map<String,String> data = new HashMap<String,String >();
			
			data.put("metric", metric);
			data.put("value", result.getText().toString());
			
			//Create OPENi Measurement object
			
			ObjectsApi objapi = new ObjectsApi();
			objapi.getInvoker().ignoreSSLCertificates(true);
			OPENiObject newObj = new OPENiObject();
			newObj.setCloudlet(cloudletId);
			newObj.setData(data); //!!!!!!
			//How to get the measurement type?
			//newObj.setOpeniType(openiType);
			
			
			
			
			newObj.setDateCreated(new Date());
			newObj.setDateModified(new Date());
			try {
				ObjectResponse objResponse = objapi.createObject("Measurement", newObj);
				objResponse.toString();
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Map<Date, Float> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
	}
		
}
