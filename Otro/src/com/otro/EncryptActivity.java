package com.otro;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;


import crypto.aes.CryptoAES;
import android.app.Activity;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Simple encryption example
 * @author CGI
 *
 */
public class EncryptActivity extends Activity {
	private CryptoAES cryptoAes;
	
	EditText plainText;
	EditText encryptionPwd;
	EditText encryptedText;
	EditText decryptedText;
	
	String encryptionPwdS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encrypt);
		
		this.cryptoAes = new CryptoAES(getApplicationContext());
		
		plainText = (EditText)findViewById(R.id.plainText);
		encryptionPwd = (EditText)findViewById(R.id.encryptionPwd);
		encryptedText = (EditText)findViewById(R.id.encryptedText);
		decryptedText = (EditText)findViewById(R.id.decryptedText);

	}
	
	
	public void btnEncrypt(View v){
		
		if (checkEmptyFieldsForEncryption()){
			
			String plainTextS = plainText.getText().toString();
			encryptionPwdS = encryptionPwd.getText().toString(); //susana
			String storedPwd = cryptoAes.getSecrectkeyIdCloud(1); //secretKey
			String calculated = cryptoAes.createDeriveKey(encryptionPwdS);

			if (storedPwd != null && storedPwd.length() > 0){
						
				try {
		
					boolean checked = cryptoAes.comproClaveDerivada(calculated, getApplicationContext());
					if (checked){
						String txtEncrypted = cryptoAes.encrypt(plainTextS, calculated);
						encryptedText.setText(txtEncrypted);	
					}else{
						Toast.makeText(
								getApplicationContext(),
								"Wrong key for encryption", Toast.LENGTH_LONG).show();
					}
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchProviderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}else{
				Toast.makeText(getApplicationContext(), "No key stored. You have to provide one", Toast.LENGTH_SHORT).show();
				if (encryptionPwdS!=null && encryptionPwdS.length()>0){
					String derived= cryptoAes.createDeriveKey(encryptionPwdS);
					cryptoAes.createSecrectkeyIdCloud(derived, 1);
				}
			}			
		}
	}
	
	public void btnDecrypt(View v) {
		if (checkEmptyFieldsForDecryption()){
			String plainText = "";
			boolean checked = false;		
			encryptionPwdS = encryptionPwd.getText().toString(); //susana
			String storedPwd = cryptoAes.getSecrectkeyIdCloud(1);
			
			String calculated = cryptoAes.createDeriveKey(encryptionPwdS);
			
			if (storedPwd.equalsIgnoreCase(calculated)){
				Toast.makeText(getApplicationContext(), "claves iguales", Toast.LENGTH_SHORT);
			}else{
				Toast.makeText(getApplicationContext(), "claves diferentes", Toast.LENGTH_SHORT);
			}
			
			
			String encryptedTextString =encryptedText.getText().toString();
			
			try{
				checked = cryptoAes.comproClaveDerivada(calculated, getApplicationContext());
				if (checked){
					plainText = cryptoAes.descrypt(encryptedTextString, calculated);	
					
					((EditText)findViewById(R.id.decryptedText)).setText(plainText);
				}else{
					Toast.makeText(
							getApplicationContext(),
							"Wrong key for decryption", Toast.LENGTH_LONG).show();
				}
			}catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"Wrong key Excpetion: "+e.getCause().getMessage(), Toast.LENGTH_LONG).show();

			}
		}
	}
	
	/**
	 * Save new cipher password for the current user
	 * @param v
	 */
	public void btnSaveClicked(View v){
		Toast.makeText(getApplicationContext(), "Not implemented", Toast.LENGTH_SHORT).show();
	}
	
	
	/**
	 * clear all fields
	 * @param v
	 */
	public void btnCancelClicked(View v){

		plainText.setText("");
		encryptionPwd.setText("");
		encryptedText.setText("");
		decryptedText.setText("");		

	}
	
	/**
	 * Check if there is any empty field that prevents encryption
	 * @return true if possible
	 */
	private boolean checkEmptyFieldsForEncryption(){
		if (encryptionPwd.getText().length()<=0 || plainText.getText().length()<=0){
			Toast.makeText(getApplicationContext(), "Your encryption password or the text to encrypt are empty. You cannot encrypt", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	
	/**
	 * Check if there is any empty field that prevents decryption
	 * @return true if possible
	 */
	private boolean checkEmptyFieldsForDecryption(){
		if (encryptionPwd.getText().length()<=0 || encryptedText.getText().length()<=0){
			Toast.makeText(getApplicationContext(), "Your encryption password or the encrypted text are empty. You cannot encrypt", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
