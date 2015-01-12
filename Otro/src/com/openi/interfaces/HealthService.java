package com.openi.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import eu.openiict.client.model.OPENiObject;

public interface HealthService {
	/*
	 * public Session login(String username, String password);
	 * 
	 * public Token authorizeClient(String clientId, Session userSession);
	 * 
	 * public OPENiCloudlet getCloudletData(Token authToken);
	 */
	public List<OPENiObject> getObjectsFromMyCloudlet(String cloudletId);

	public List<OPENiObject> getObjectsFromMyCloudlet(String cloudletId,
			String metric);

	public Map<Date, Float> getBMIFromCloudlet(String cloudletId);

	public OPENiObject getOPENiObjectById(String cloudledId, String id);
	
	public List<String> defineMeasurementsList();
	
	public Map<Date, Double> dataWhenNoCloudlet(String measurement);
}
