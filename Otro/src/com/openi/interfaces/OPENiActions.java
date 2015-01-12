package com.openi.interfaces;

import eu.openiict.client.model.OPENiCloudlet;
import eu.openiict.client.model.Session;
import eu.openiict.client.model.Token;

public interface OPENiActions {

	public Session login(String username, String pwd);

	public Token authorizeClient(String clientId, Session userSession);

	public OPENiCloudlet getCloudletData(Token authToken);

	public boolean setupClient(String clientId);

	public boolean registerUser(String username, String password);
	
	public String addMeasurementToCloudlet(String cloudletId,String result, String measurementType);
}
