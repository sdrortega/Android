package com.otro;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Presents the options to the user: add new result, view chart, encryption example
 * @author CGI
 *
 */

public class MainOptionsActivity extends Activity {

	String token;
	String cloudletId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_options);
		
		Bundle reicieveParams = getIntent().getExtras();
		token = reicieveParams.getString("token");
		cloudletId = reicieveParams.getString("cloudletId");
		 
	}

	public void btnAddMeasurement(View v) {
		
		  Intent intent = new Intent(this, NewMeasurement.class); //demo2
		  //intent.putExtra("cloudletId","c_088e818f2ec0e68379c891c3ca2a0860-90"); //demo1
		  
		  //Cloudlet real del usuario
		  intent.putExtra("cloudletId",cloudletId);
		  startActivity(intent);
		
		  /*Intent intent = new Intent(this, TwoLinesChart.class); //demo2		  
		  startActivity(intent);*/
		 
	}

	public void btnViewChart(View v) {
		Intent intent = new Intent(this, MeasurementsList.class);
		// demo2
		// intent.putExtra("cloudletId",
		// "c_088e818f2ec0e68379c891c3ca2a0860-90");
		// demo1
		intent.putExtra("cloudletId", cloudletId);
		intent.putExtra("token", token);
		startActivity(intent);
		//c_1df6f65ecadf13ba9706438194055914-90
	}
	

	public void btnEncryption(View v){
		Intent intent = new Intent(this, EncryptActivity.class);
		startActivity(intent);
	}
}
