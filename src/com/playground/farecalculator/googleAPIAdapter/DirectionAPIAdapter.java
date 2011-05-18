/**
 * 
 */
package com.playground.farecalculator.googleAPIAdapter;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.playground.farecalculator.utils.URLUtils;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 *
 */
public class DirectionAPIAdapter
{
	public static final String GOOGLE_MAPS_DIRECTIONS_API = "http://maps.googleapis.com/maps/api/directions/json?origin=%1$s&destination=%2$s&alternatives=true&sensor=true&units=metric";
	
	public static final String STATUS_PARAM = "status";
	public static final String ROUTES_PARAM = "routes";
	public static final String ROUTES_LEGS_PARAM = "legs";
	public static final String ROUTES_LEGS_DISTANCE_PARAM = "distance";
	public static final String ROUTES_LEGS_DISTANCE_VALUE_PARAM = "value";
	
	public static final String SUCCESS_RESPONSE = "OK";
	
	public static double[] getDistanceBetweenPoints(String source, String destination) throws IOException, JSONException
	{
		double[] result = null;
		
		String url = String.format(GOOGLE_MAPS_DIRECTIONS_API, URLEncoder.encode(source), URLEncoder.encode(destination));
		JSONObject jsonResult = new JSONObject(URLUtils.getUrl(url));
		String status = jsonResult.getString(STATUS_PARAM);
		if(SUCCESS_RESPONSE.equals(status))
		{
			JSONArray routes = jsonResult.getJSONArray(ROUTES_PARAM);
			result = new double[routes.length()];
			for(int i = 0 ; i < routes.length(); i++)
			{
				JSONObject route = routes.getJSONObject(i);
				double distance = 0;
				JSONArray legs = route.getJSONArray(ROUTES_LEGS_PARAM);
				for(int j = 0; j < legs.length(); j++)
				{
					JSONObject leg = legs.getJSONObject(j);
					JSONObject distanceObject =  leg.getJSONObject(ROUTES_LEGS_DISTANCE_PARAM);
					distance += distanceObject.getDouble(ROUTES_LEGS_DISTANCE_VALUE_PARAM);
				}
				result[i] = distance;
			}
		}
		return result;
	}
	
	public static void main(String args[]) throws IOException, JSONException
	{
		getDistanceBetweenPoints("Old Guest house IIT Bombay Powai Mumbai", "rcity mall ghatkopar");
	}
}
