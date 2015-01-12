package com.otro;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.openi.implement.OPENiActionsImpl;
import com.openi.interfaces.OPENiActions;

import eu.openiict.client.common.ApiException;
import eu.openiict.client.model.OPENiCloudlet;
import eu.openiict.client.model.Session;
//import eu.openiict.client.model.Session;
//import eu.openiict.client.model.Token;
import eu.openiict.client.model.Token;

/**
 * Log in/register activity
 * Starting point of the applicacion
 * @author CGI
 *
 */
public class MainActivity extends Activity {
	private OPENiActions openiActions = new OPENiActionsImpl();
	private EditText usrLogin;
	private EditText usrPwd;
	
	private String login;
	private String password;

	public String cloudletId = "";
	

	private static final String HL = "HealthService";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		usrLogin = (EditText) findViewById(R.id.usrLogin);
		usrPwd = (EditText) findViewById(R.id.userPwd);


	}

	/**
	 * Log in button action: It is intended to authorize the user to his cloudlet
	 * Gets cloudletId 
	 * @param v
	 * @throws ApiException
	 */
	public void btnConnectClicked(View v) throws ApiException {

		login = usrLogin.getText().toString(); // susana
		password = usrPwd.getText().toString(); // susana		
		
		
		 // Commented until we have the cloudlet platform running
		AsyncTaskRunner runner = new AsyncTaskRunner();
		runner.execute(login, password);
		 
		/*String cloudletId = "c_43c1ccbc9fa9b94ba0e93fc5d8e199fb-90";
		Intent intent = new Intent(getApplicationContext(), MainOptionsActivity.class);
		intent.putExtra("cloudletId", cloudletId);
		startActivity(intent);*/
	 
	}

	/**
	 * TODO Registers a new user
	 * @param v
	 */
	public void btnRegisterClicked(View v) {
		/*
		Intent intent = new Intent(this, RegisterActivity.class);		
		startActivityForResult(intent, 1);
		*/
		Toast.makeText(getApplicationContext(), "Register a new user", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            String result=data.getStringExtra("result");
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}

	private class AsyncTaskRunner extends AsyncTask<String, String, OPENiCloudlet> {


		private ProgressDialog dialog;
		
		/*
		 *  (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		
		@Override
		protected OPENiCloudlet doInBackground(String... params) {
			String login = params[0];
			String password = params[1];
			OPENiCloudlet cloudletData = null;
			Token token = null;
			Session userSession = null;
			
			// Register client		
			openiActions.setupClient("HealthLife"); //si lanza excepción?
			openiActions.registerUser(login, password); // susana susana
			
			userSession = openiActions.login(login, password);
			if (userSession != null) {
				
				Log.i(HL, "User "+login+" logged in");
				
				token = openiActions.authorizeClient("HealthLife", userSession);
				cloudletData = openiActions.getCloudletData(token);
				cloudletId = cloudletData.getId();
			}else{
				Toast.makeText(
						getApplicationContext(),
						"Invalid credentials. Please, try again with other credentials",
						Toast.LENGTH_LONG).show();
			}
			
			
			return cloudletData;
		}
		
		
		/**
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		
		@Override
		protected void onPostExecute(OPENiCloudlet cloudletData) {
			// execution of result of Long time consuming operation
					
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			if (cloudletData!=null){
				cloudletId = cloudletData.getId();
				
				Log.i(HL, "Cloudlet de "+login+": "+cloudletId);
				//cloudletId = "c_0967cab17835472735903ac113ce7775-90";
				Log.i(HL, "Registrado y logueado");
				

				if (!cloudletId.equals("")) {
					Intent intent = new Intent(getApplicationContext(), MainOptionsActivity.class);
					intent.putExtra("cloudletId", cloudletId);
					startActivity(intent);
				}
			}else{
				finish();
			}
			
			
		}
		
		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Checking User. Please wait.");
			dialog.show();
		}
		
	}	
}
