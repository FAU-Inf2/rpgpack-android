package de.fau.mad.sqlite.impl;

import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.fau.mad.sqlite.interfaces.IAllJoynReceiver;

public class AllJoynReceiver implements IAllJoynReceiver {

	@Override
	public Map<String, String> receive(String jsonString) {
		// convert
		Map<String, String> receivedDataMap = new TreeMap<String, String>();
		receivedDataMap = decodeSingleJson(jsonString);		
		return receivedDataMap;
	}

//	private String decodeSingleJson(String s) {
//		String resultString = "";
//		JSONParser parser = new JSONParser();
//		try {
//			Object obj = parser.parse(s);
//			JSONObject jsonObject = (JSONObject) obj;
//			String feature = (String) jsonObject.get("feature");
//			String value = (String) jsonObject.get("value");
//			resultString = feature + " : " + value;
//		} catch (ParseException pe) {
//			System.out.println(pe.getMessage());
//		}
//		return resultString;
//	}

	
	public Map<String, String> decodeSingleJson(String s) {
		Map<String, String> nameMap = new TreeMap<String, String>();

		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(s);
			JSONObject jsonObject = (JSONObject) obj;
			String id = (String) jsonObject.get("koboldentryid");
			String value = (String) jsonObject.get("value");
			//koboldentryid : received value
			nameMap.put(id, value);
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
		}
		return nameMap;
	}
	
}
