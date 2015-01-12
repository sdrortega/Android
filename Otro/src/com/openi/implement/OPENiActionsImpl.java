package com.openi.implement;

import android.util.Log;

import com.openi.interfaces.OPENiActions;

import eu.openiict.client.api.AuthorizeApi;
import eu.openiict.client.api.ClientsApi;
import eu.openiict.client.api.CloudletsApi;
import eu.openiict.client.api.SessionApi;
import eu.openiict.client.api.UsersApi;
import eu.openiict.client.common.ApiException;
import eu.openiict.client.model.AuthorizationRequest;
import eu.openiict.client.model.ClientRegisterRequest;
import eu.openiict.client.model.Credentials;
import eu.openiict.client.model.OPENiCloudlet;
import eu.openiict.client.model.Session;
import eu.openiict.client.model.Token;
import eu.openiict.client.model.UserRegisterRequest;

public class OPENiActionsImpl implements OPENiActions {
	
	private String TAG ="OPENiActions";
	
	@Override
	public Session login(String username, String password) {
		// Setup API
		SessionApi sessionapi = new SessionApi();
		sessionapi.getInvoker().ignoreSSLCertificates(true);

		// Prepare User registration request
		Credentials creds = new Credentials();
		// Set login credentials
		creds.setName(username);
		creds.setPassword(password);
		// Retrieve Session token
		try {
			Session session = sessionapi.login(creds);
			Log.i(TAG, "Session for "+username+": "+session.getSession());
			return session;
		} catch (ApiException e) {
			Log.i(TAG, "Exception when logging user "+username+": "+e.getCause() + " " + e.getCode());
			return null;
		}
	}

	@Override
	public Token authorizeClient(String clientId, Session userSession) {
		// Setup API
		AuthorizeApi authorizeapi = new AuthorizeApi();
		authorizeapi.getInvoker().ignoreSSLCertificates(true);
		
		// Prepare authorization request
		AuthorizationRequest authreq = new AuthorizationRequest();
		// Set ClientId and Session
		authreq.setClientId(clientId);
		authreq.setSession(userSession.getSession());
		// Retrieve Authorization token
		try {
			Token token = authorizeapi.authorizeClient(authreq);
			Log.i(TAG, "Token for HealthLife: "+token.getToken());
			return token;
		} catch (ApiException e) {
			System.out.println(e.getMessage() + " " + e.getCode());
			e.printStackTrace();
			Log.i(TAG, "Exception when getting token");
			return null;
		}
	}

	@Override
	public OPENiCloudlet getCloudletData(Token authToken) {
		// Setup CloudletAPI
		CloudletsApi cloudletapi = new CloudletsApi();
		cloudletapi.getInvoker().ignoreSSLCertificates(true);
		// Get Cloudlet
		try {
			OPENiCloudlet cloudletData = cloudletapi.getCloudletId(authToken.getToken());		
			Log.i(TAG, "OPENiCloudlet: "+cloudletData.getId());
			return cloudletData;
		} catch (ApiException e) {
			Log.i(TAG, "Exception when getting cloudlet");			
			return null;
		}
	}

	/*********************************** From tutorials *****************************/
	public boolean setupClient(String clientId) { //inicialmente devuelve void
		// Setup Client API
		ClientsApi clientsapi = new ClientsApi();
		
		clientsapi.getInvoker().ignoreSSLCertificates(true);
		// Prepare the client request object.
		ClientRegisterRequest clientIdReq = new ClientRegisterRequest();
		// Set the Id of the new client to be created
		clientIdReq.setClientId(clientId);
		// Call ClientAPI to create new client.
		try {
			clientsapi.createClient(clientIdReq);
		} catch (ApiException e) {
			if (e.getCode() == 400) {
				Log.i(TAG, "User already exists");
				return true;
			} else {				
				System.out.println(e.getMessage() + " " + e.getCode());
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	
	public boolean registerUser(String username, String password) { //void
		// Setup User API
		UsersApi usersapi = new UsersApi();
		usersapi.getInvoker().ignoreSSLCertificates(true);

		// Prepare User registration request
		UserRegisterRequest userReq = new UserRegisterRequest();
		// Set name and password for user
		userReq.setName(username);
		userReq.setPassword(password);
		// Create User
		try {
			usersapi.createUser(userReq);
		} catch (ApiException e) {
			if (e.getCode() == 400) {
				Log.i(TAG,"User "+username+" already registered");
				
				return true;
			} else {
				Log.i(TAG, "Exception when registering user "+username+ " :"+e.getMessage() + " " + e.getCode());
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	
	/**
	 * 
	 * @return new object id, null otherwise
	 */
	public String addMeasurementToCloudlet(String cloudletId,String result, String measurementType){
		String objectId = null;
		
		return objectId;
		
	}
}
