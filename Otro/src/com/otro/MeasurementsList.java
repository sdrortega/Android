package com.otro;

import java.util.List;
import java.util.Locale;

import com.openi.implement.HealthServiceImpl;
import com.openi.interfaces.HealthService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MeasurementsList extends Activity {
	private static final String HL = "HealthService";	

	ListView listView;
	Button btnMeasurement;
	String token;
	String cloudletId;
	final HealthService hl = new HealthServiceImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle reicieveParams = getIntent().getExtras();
		cloudletId = reicieveParams.getString("cloudletId");
		token = reicieveParams.getString("token");

		setContentView(R.layout.activity_measurements_list);

		listView = (ListView) findViewById(R.id.list);
		
		List<String> values = hl.defineMeasurementsList();

		int listType = android.R.layout.simple_list_item_multiple_choice;
		//int listType = android.R.layout.simple_list_item_1;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,listType, android.R.id.text1, values);

		
		// listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		// Assign adapter to ListView
		listView.setAdapter(adapter);
		
		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ListView Clicked item value
				String itemValue = (String) listView
						.getItemAtPosition(position);

				Intent intent = new Intent(getApplicationContext(),
						DateChartActivity.class);
				// demo2
				// intent.putExtra("cloudletId",
				// "c_088e818f2ec0e68379c891c3ca2a0860-90");
				// demo1
				intent.putExtra("cloudletId", cloudletId);
				// intent.putExtra("measurement", itemPosition);
				intent.putExtra("measurement",itemValue.toUpperCase(Locale.ENGLISH));
				intent.putExtra("token", token);
				startActivity(intent);

			}

		});

	}
	
}
