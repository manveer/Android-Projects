/**
 * 
 */
package com.playground.farecalculator.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.playground.farecalculator.fares.BengaluruAutoRickshawFare;
import com.playground.farecalculator.fares.DelhiAutoRickshawFare;
import com.playground.farecalculator.fares.DelhiBlackYellowTaxiACFare;
import com.playground.farecalculator.fares.DelhiBlackYellowTaxiFare;
import com.playground.farecalculator.fares.DelhiMeruCabFare;
import com.playground.farecalculator.fares.HyderabadMeruCabFare;
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
		populateCitiesToTransportMap(CitiesEnum.Bangalore, TransportsEnum.Meru, TransportsEnum.AutoRickshaw);
		populateCitiesToTransportMap(CitiesEnum.Delhi, TransportsEnum.AutoRickshaw, TransportsEnum.Meru, TransportsEnum.TaxiAC, TransportsEnum.TaxiNonAC);
		populateCitiesToTransportMap(CitiesEnum.Hyderabad, TransportsEnum.AutoRickshaw, TransportsEnum.Meru);
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
		fareArray[CitiesEnum.Bangalore.ordinal()][TransportsEnum.AutoRickshaw.ordinal()] = new BengaluruAutoRickshawFare();
		
		fareArray[CitiesEnum.Delhi.ordinal()][TransportsEnum.AutoRickshaw.ordinal()] = new DelhiAutoRickshawFare();
		fareArray[CitiesEnum.Delhi.ordinal()][TransportsEnum.Meru.ordinal()] = new DelhiMeruCabFare();
		fareArray[CitiesEnum.Delhi.ordinal()][TransportsEnum.TaxiAC.ordinal()] = new DelhiBlackYellowTaxiACFare();
		fareArray[CitiesEnum.Delhi.ordinal()][TransportsEnum.TaxiNonAC.ordinal()] = new DelhiBlackYellowTaxiFare();
		
		fareArray[CitiesEnum.Hyderabad.ordinal()][TransportsEnum.Meru.ordinal()] = new HyderabadMeruCabFare();
		fareArray[CitiesEnum.Hyderabad.ordinal()][TransportsEnum.TaxiNonAC.ordinal()] = new DelhiBlackYellowTaxiFare();
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
	
	/**
	 * Returns a string of all the possible modes of transport with fare for each of them for a given city
	 * @param citySelected City for which to calculate fare 
	 * @param waitTime wait time
	 * @param distances distance array containing minimum and maximum distance
	 * @return a <code>String</code> representing the fare for different modes of transport
	 */
	public String getFare(int citySelected, int waitTime, double distances[])
	{
		Arrays.sort(distances);
		StringBuilder sb = new StringBuilder();
		CitiesEnum city = CitiesEnum.values()[citySelected];
		for (TransportsEnum transport : TransportsEnum.values())
		{
			IFare fareCalculator = getFareCalculator(city, transport);
			if (fareCalculator != null)
			{
				double minFare = Math.ceil(fareCalculator.getFare(distances[0], waitTime));
				sb.append(transport.getDisplayString()).append(": Rs ").append(minFare);
				if (distances.length > 1)
				{
					double maxFare = Math.ceil(fareCalculator.getFare(distances[distances.length - 1], 0));
					sb.append(" to ").append(maxFare);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}