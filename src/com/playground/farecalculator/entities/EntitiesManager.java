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
import com.playground.farecalculator.fares.PuneMeruCabFare;

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
		this.fareArray = new IFare[CitiesEnum.values().length][TransportsEnum.values().length];
		populateFareClass();
		
		this.citiesToTransportMap = new HashMap<String, List<TransportsEnum>>();
		populateMap(CitiesEnum.Mumbai, TransportsEnum.AutoRickshaw, TransportsEnum.TaxiAC, TransportsEnum.TaxiNonAC, TransportsEnum.Meru);
		populateMap(CitiesEnum.Pune, TransportsEnum.AutoRickshaw, TransportsEnum.Meru);
	}
	
	private void populateMap(CitiesEnum city, Object ... transports)
	{
		List<TransportsEnum> transportsList = new ArrayList<TransportsEnum>();
		for(Object obj : transports)
		{
			transportsList.add((TransportsEnum)obj);
		}
		citiesToTransportMap.put(city.name().toLowerCase(), transportsList);
	}
	
	private void populateFareClass()
	{
		// Mumbai -> All type of transports
		fareArray[CitiesEnum.Mumbai.ordinal()][TransportsEnum.AutoRickshaw.ordinal()] = new MumbaiAutoRickshawFare();
		fareArray[CitiesEnum.Mumbai.ordinal()][TransportsEnum.TaxiNonAC.ordinal()] = new MumbaiBlackYellowTaxiFare();
		fareArray[CitiesEnum.Mumbai.ordinal()][TransportsEnum.TaxiAC.ordinal()] = new MumbaiBlackYellowTaxiFare();
		fareArray[CitiesEnum.Mumbai.ordinal()][TransportsEnum.Meru.ordinal()] = new MumbaiMeruCabFare();
		
		fareArray[CitiesEnum.Pune.ordinal()][TransportsEnum.AutoRickshaw.ordinal()] = new PuneAutoRickshawFare();
		fareArray[CitiesEnum.Pune.ordinal()][TransportsEnum.Meru.ordinal()] = new PuneMeruCabFare();
	}
	
	public IFare getFareCalculator(CitiesEnum city, TransportsEnum transport)
	{
		return fareArray[city.ordinal()][transport.ordinal()];
	}
	
	public List<TransportsEnum> getTransportsForCity(CitiesEnum city)
	{
		return citiesToTransportMap.get(city);
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
}