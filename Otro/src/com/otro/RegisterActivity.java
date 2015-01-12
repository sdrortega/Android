package com.otro;

import com.openi.implement.OPENiActionsImpl;
import com.openi.interfaces.OPENiActions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	private OPENiActions openiActions = new OPENiActionsImpl();
	
	private EditText usrLogin;
	private EditText usrPwd;
	
	private String login;
	private String password;
	private boolean registered;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		usrLogin = (EditText) findViewById(R.id.login);
		usrPwd = (EditText) findViewById(R.id.password);

		/*Bundle reicieveParams = getIntent().getExtras();
		registered = reicieveParams.getBoolean("registered",false);*/
	}
	
	public void btnSaveClicked(View v) {
		login = usrLogin.getText().toString(); // susana
		password = usrPwd.getText().toString(); // susana
		
		Intent returnIntent = new Intent();
		
		if (openiActions.setupClient("HealthLife")){//si lanza excepción?
			if (!openiActions.registerUser(login, password)){
				Toast.makeText(getApplicationContext(), "It was not possible to register a new user into the platform",
						Toast.LENGTH_LONG).show();
				returnIntent.putExtra("result",false);
				setResult(RESULT_CANCELED,returnIntent);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Please, do log in",
						Toast.LENGTH_LONG).show();
				
				// Go back to login activity
				
				returnIntent.putExtra("result",true);
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		}
	}
}
