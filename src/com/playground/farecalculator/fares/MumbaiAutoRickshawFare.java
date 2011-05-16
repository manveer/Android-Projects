/**
 * 
 */
package com.playground.farecalculator.fares;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 *
 */
public class MumbaiAutoRickshawFare implements IFare
{
	public static final int INITIAL_TICKS = 10;
	public static final int DISTANCE_FOR_FIRST_TICK = 1600;
	public static final int DISTANCE_FOR_EVERY_SUBSEQUENT_TICK = 200;
	@Override
	public double getFare(double distanceTravelled, int waitTime)
	{
		int ticks = INITIAL_TICKS;
		if(distanceTravelled > DISTANCE_FOR_FIRST_TICK)
		{
			ticks += (int)Math.ceil(((double)distanceTravelled - DISTANCE_FOR_FIRST_TICK)/DISTANCE_FOR_EVERY_SUBSEQUENT_TICK); // 1 tick for every 200 meters after 1.5 km
			ticks += ((waitTime/1000)/60); 
		}
		else // totalDistance is less than 1500 m , for every 150 m one tick is there
		{
			int calculatedTicks = ((waitTime/1000)/60); // ticks for wait time, 1 tick for every 1 minute
			calculatedTicks += (int)Math.ceil(distanceTravelled/150); // ticks for every 150 metre
			if(calculatedTicks > ticks)
				ticks = calculatedTicks;
		}
		return (ticks*1.3 -2);
	}
}
