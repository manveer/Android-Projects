/**
 * 
 */
package com.playground.farecalculator.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.playground.farecalculator.fares.IFare;
import com.playground.farecalculator.fares.MumbaiAutoRickshawFare;
import com.playground.farecalculator.fares.MumbaiBlackYellowTaxiFare;
import com.playground.farecalculator.fares.MumbaiMeruCabFare;
import com.playground.farecalculator.fares.PuneAutoRickshawFare;
import com.playground.farecalculator.fares.BengaluruMeruCabFare;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 *
 */
public class EntitiesManager
{
	private static EntitiesManager instance = new EntitiesManager();
	
	public static EntitiesManager getInstance()
	{
		return instance;
	}
	
	/** Map from names of city to transport options available for that city **/
	private Map<String, List<TransportsEnum>> citiesToTransportMap;
	private IFare fareArray[][];
	private EntitiesManager()
	{
		/** Any new addition to the City or Transport will need to add corresponding 
		 * IFare implementation in populateFareArray and a line below to 
		 * populateCitiesToTransportMap**/
		this.fareArray = new IFare[CitiesEnum.values().length][TransportsEnum.values().length];
		populateFareArray();
		
		this.citiesToTransportMap = new HashMap<String, List<TransportsEnum>>();
		populateCitiesToTransportMap(CitiesEnum.Mumbai, TransportsEnum.AutoRickshaw, TransportsEnum.TaxiNonAC, TransportsEnum.Meru);
		populateCitiesToTransportMap(CitiesEnum.Pune, TransportsEnum.AutoRickshaw);
		populateCitiesToTransportMap(CitiesEnum.Bangalore, TransportsEnum.Meru);
	}
	
	private void populateCitiesToTransportMap(CitiesEnum city, Object ... transports)
	{
		List<TransportsEnum> transportsList = new ArrayList<TransportsEnum>();
		for(Object obj : transports)
		{
			transportsList.add((TransportsEnum)obj);
		}
		citiesToTransportMap.put(city.name().toLowerCase(), transportsList);
	}
	
	private void populateFareArray()
	{
		// Mumbai -> All type of transports
		fareArray[CitiesEnum.Mumbai.ordinal()][TransportsEnum.AutoRickshaw.ordinal()] = new MumbaiAutoRickshawFare();
	
		fareArray[CitiesEnum.Mumbai.ordinal()][TransportsEnum.TaxiNonAC.ordinal()] = new MumbaiBlackYellowTaxiFare();
		fareArray[CitiesEnum.Mumbai.ordinal()][TransportsEnum.Meru.ordinal()] = new MumbaiMeruCabFare();
		
		fareArray[CitiesEnum.Pune.ordinal()][TransportsEnum.AutoRickshaw.ordinal()] = new PuneAutoRickshawFare();
		
		fareArray[CitiesEnum.Bangalore.ordinal()][TransportsEnum.Meru.ordinal()] = new BengaluruMeruCabFare();
	}
	
	public IFare getFareCalculator(CitiesEnum city, TransportsEnum transport)
	{
		return fareArray[city.ordinal()][transport.ordinal()];
	}
	
	public String[] getTransportsForCity(CitiesEnum city)
	{
		List<TransportsEnum> transports = citiesToTransportMap.get(city.name().toLowerCase());
		if(transports == null || transports.size() == 0)
			return null;
		String[] result = new String[transports.size()];
		int i = 0;
		for(TransportsEnum transport : transports)
			result[i++] = transport.getDisplayString();
		return result;
	}
	
	/**
	 * Returns <code>CitiesEnum</code> object corresponding to a given city name <code>String</code>
	 * @param cityName
	 * @return <code>CitiesEnum</code> or null if no match is found
	 */
	public CitiesEnum getCityEnum(String cityName)
	{
		if(cityName == null || "".equals(cityName))
			return null;
		cityName = cityName.toLowerCase();
		for(CitiesEnum city : CitiesEnum.values())
		{
			if(city.name().toLowerCase().equals(cityName))
				return city;
		}
		return null;
	}

	public CharSequence[] getCities()
	{
		CharSequence[] cities = new String[CitiesEnum.values().length];
		int i = 0;
		for(CitiesEnum city : CitiesEnum.values())
		{
			cities[i++] = city.name();
		}
		return cities;
	}
}