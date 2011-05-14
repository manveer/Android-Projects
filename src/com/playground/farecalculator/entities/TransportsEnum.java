/**
 * 
 */
package com.playground.farecalculator.entities;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 * 
 */
public enum TransportsEnum
{
	AutoRickshaw("Auto Rick Shaw"), 
	TaxiNonAC("Black and Yellow Taxi - Non AC"), 
	TaxiAC("Black and Yellow Taxi - AC"), 
	Meru("Meru cab");

	private String displayString;

	private TransportsEnum(String dp)
	{
		this.displayString = dp;
	}

	public String getDisplayString()
	{
		return this.displayString;
	}
}
