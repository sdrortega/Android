package com.openi.implement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import android.util.Log;

import com.openi.interfaces.HealthService;

import eu.openiict.client.api.ObjectsApi;
import eu.openiict.client.common.ApiException;
import eu.openiict.client.model.OPENiObject;

public class HealthServiceImpl implements HealthService {

	/**
	 * Gets all objects in the given cloudletId
	 */
	private static final String HL = "HealthService";

	@Override
	public List<OPENiObject> getObjectsFromMyCloudlet(String cloudletId) {

		// cloudletId = "c_088e818f2ec0e68379c891c3ca2a0860-90";

		ObjectsApi objapi = new ObjectsApi();
		objapi.getInvoker().ignoreSSLCertificates(true);
		List<OPENiObject> objects = new ArrayList<OPENiObject>();

		try {
			// objects = objapi.getObjects(cloudletId,0,0,"", false, "", "",
			// "");
			objects = objapi.getObjects(cloudletId, 0, null, null, false, null,null, null);// cloudletID, skip, limit, type, id_only,
								// with_property, property_filter,
								// only_show_properties
			// return objects;

		} catch (ApiException e) {
			Log.v(HL, "objapi.getObjects", e);
			System.out.println(e.getMessage());
			objects = null;
			// return null;
		}
		return objects;
	}

	@Override
	public List<OPENiObject> getObjectsFromMyCloudlet(String cloudletId,String metric) {
		// cloudletId = "c_088e818f2ec0e68379c891c3ca2a0860-90";

		ObjectsApi objapi = new ObjectsApi();
		objapi.getInvoker().ignoreSSLCertificates(true);
		List<OPENiObject> toDraw = new ArrayList<OPENiObject>();

		try {
			// objects = objapi.getObjects(cloudletId,0,0,"", false, "metric",
			// metric, ""); // cloudletID, skip, limit, type, id_only,
			// with_property, property_filter, only_show_properties
			List<OPENiObject> objects = objapi.getObjects(cloudletId, 0, null, null, false,null, null, null);			
			
			toDraw = getObjectsByMeasurementType(objects,metric);
			
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return toDraw;
	}

	/**
	 * Get the complete content from the given object in the given cloudlet
	 */
	@Override
	public OPENiObject getOPENiObjectById(String cloudletId, String id) {
		ObjectsApi objapi = new ObjectsApi();
		objapi.getInvoker().ignoreSSLCertificates(true);

		OPENiObject obj;
		try {
			obj = objapi.getObject(cloudletId, id); // c_088e818f2ec0e68379c891c3ca2a0860-90,
													// o_f8fefa9b9dff918636741fe202dcc779-353
			return obj;
		} catch (ApiException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Supposing a person older than 18
	 */
	@Override
	public Map<Date, Float> getBMIFromCloudlet(String cloudletId) {
		Map<Date, Float> bmi = null;

		// Get Height (last one)
		Double height = new Double(0);
		// Get Weights (measurement objects, with date)
		List<Double> weightList = new ArrayList<Double>();
		List<OPENiObject> objects = getObjectsFromMyCloudlet( cloudletId, "Weight");
		List<OPENiObject> weightObjects = getObjectsByMeasurementType(objects,"Weight");

		int hasta = weightObjects.size();
		String resultado = "";
		Double f2 = null;
		for (int i = 0; i < hasta; i++) {
			OPENiObject openiobj = objects.get(i);
			// Get Data devuelve vacío
			String objectId = openiobj.getId();

			OPENiObject obj = getOPENiObjectById(cloudletId, objectId);

			Map mapa = obj.getData();			
			resultado = (String) mapa.get("result");
			Date fecha = obj.getDateCreated();
			if (resultado!=null && resultado.trim().length()>0){
				f2 = Double.parseDouble(resultado);
				weightList.add(f2);
				
			}
			
		}
		
		
		Double auxBMI = new Double(0);

		for (int i = 0; i < weightList.size(); i++) {
			auxBMI = calculateBMI(0, height, weightList.get(i));			
		}

		return bmi;
	}

	private Double calculateBMI(int age, Double height, Double double1) {
		Double bmi = null;
		// La altura viene en cm, pasarla a metros
		height = height / 100;

		bmi = double1 / (height * height);
		return bmi;
	}

	
	public List<String> defineMeasurementsList(){
		List<String> values = new ArrayList<String>();
		values.add("Height");
		values.add("Weight");
		//values.add("BMI"); //Always calculated
		values.add("Heart rate");
		//values.add("Systolic/Diastolic");
		
		return values;
	}
	
	private List<OPENiObject> getObjectsByMeasurementType(List<OPENiObject> objects,String measurementType){
		int hasta = objects.size();


		List<OPENiObject> toDraw = new ArrayList<OPENiObject>();
		String metric = getMetricByMeasurementType(measurementType);
		
		if(metric.length()>0){
			
			for (int i = 0; i < hasta; i++) {
				OPENiObject openiobj = objects.get(i);
				// Get Data devuelve vacío
				String objectId = openiobj.getId();
				String cloudletId = openiobj.getCloudlet();
				OPENiObject obj = getOPENiObjectById(cloudletId, objectId);
	
				Map mapa = obj.getData();
	
				if (mapa.containsKey("result") && mapa.get("metric").toString().equalsIgnoreCase(metric)) {
					toDraw.add(obj);
				}
			}
		}
		return toDraw;
		
		
	}
	
	private String getMetricByMeasurementType(String measurementType){
		String metric="";
		if (measurementType.equalsIgnoreCase("height")) {
			metric = "cm";
		}else if (measurementType.equalsIgnoreCase("weight")){
			metric = "kg";
		}else{
			metric = "mmHg";
		}
		
		return metric;
	}
	
	public Map<Date, Double> dataWhenNoCloudlet(String measurement){
		
		Map<Date,Double> values = new TreeMap<Date, Double>();
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm");	
		try{
			if (measurement.equalsIgnoreCase("height")){
				 values.put(fmt.parse("15/10/2014 12:00"),152.0);
				 values.put(fmt.parse("16/10/2014 12:00"),151.0);
				 values.put(fmt.parse("18/10/2014 12:00"),152.0);
				 values.put(fmt.parse("19/10/2014 12:00"),150.0);
			}else if (measurement.equalsIgnoreCase("weight")){
				 values.put(fmt.parse("15-10-2014 12:10"),55.0);
				 values.put(fmt.parse("16-10-2014 20:13"),54.2);
				 values.put(fmt.parse("18-10-2014 15:15"),54.5);
				 values.put(fmt.parse("19-10-2014 13:21"),54.0);
				 values.put(fmt.parse("30-10-2014 19:07"),54.1);
				 values.put(fmt.parse("02-11-2014 18:50"),53.3);
				 values.put(fmt.parse("17-11-2014 12:10"),53.0);
			}else{
				 values.put(fmt.parse("15/10/2014 12:00"),60.0);
				 values.put(fmt.parse("15/10/2014 12:05"),62.0);
				 values.put(fmt.parse("15/10/2014 12:15"),65.0);
				 values.put(fmt.parse("15/10/2014 12:30"),60.0);
			}
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
}
