/**
 * 
 */
package com.playground.farecalculator.fares;

import com.playground.farecalculator.utils.CommonUtils;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 *
 */
public class BengaluruMeruCabFare implements IFare
{
	public static final int INITIAL_TICKS = 40;
	public static final double DISTANCE_FOR_FIRST_TICK = 4000;
	public static final double DISTANCE_FOR_EVERY_SUBSEQUENT_TICK = (100);
	public static final int WAIT_TIME_FOR_TICK = 90;
	public static final int DISTANCE_FOR_TICK_BEFORE_FIRST_TICK = 100;
	public static final int WAIT_TIME_FOR_TICK_BEFORE_FIRST_TICK = 90;
	public static final int WAIT_TIME_CONCESSION = 20*60; //20 minutes
	public static final double TICKS_MULTIPLICATION_FACTOR = 1.5;
	public static final double CONSTANT_FACTOR = 0;
	public static final double NIGHT_CHARGE_CONSTANT_MULTIPLICATION_FACTOR = 1;
	public static final double NIGHT_CHARGE_CONSTANT_ADDITION_FACTOR = 0;
	public static final int NIGHT_CHARGE_START_HOUR = 0;
	public static final int NIGHT_CHARGE_END_HOUR = 5;
	public static final int NIGHT_CHARGE_START_MINUTE = 0;
	public static final int NIGHT_CHARGE_END_MINUTE = 0;
	
	@Override
	public double getFare(double distanceTravelled, int waitTime)
	{
		int ticks = INITIAL_TICKS;
		waitTime = waitTime/1000; // converting from milli seconds to seconds
		if(distanceTravelled > DISTANCE_FOR_FIRST_TICK)
		{
			ticks += (int)Math.ceil(((double)distanceTravelled - DISTANCE_FOR_FIRST_TICK)/DISTANCE_FOR_EVERY_SUBSEQUENT_TICK); // 1 tick for every 200 meters after 1.5 km
			if( waitTime > WAIT_TIME_CONCESSION)
				ticks += ((waitTime-WAIT_TIME_CONCESSION)/WAIT_TIME_FOR_TICK); 
		}
		else // totalDistance is less than DISTANCE_FOR_FIRST_TICK, for every DISTANCE_FOR_TICK_BEFORE_FIRST_TICK one tick is there
		{
			int calculatedTicks = 0;
			if(waitTime > WAIT_TIME_CONCESSION)
				calculatedTicks = ((waitTime - WAIT_TIME_CONCESSION)/WAIT_TIME_FOR_TICK_BEFORE_FIRST_TICK); // ticks for wait time, 1 tick for every 1 minute
			calculatedTicks += (int)Math.ceil(distanceTravelled/DISTANCE_FOR_TICK_BEFORE_FIRST_TICK); // ticks for every 150 metre
			if(calculatedTicks > ticks)
				ticks = calculatedTicks;
		}
		double fare = (ticks*TICKS_MULTIPLICATION_FACTOR + CONSTANT_FACTOR); 
		if(CommonUtils.isNightFareApplicable(NIGHT_CHARGE_START_HOUR, NIGHT_CHARGE_END_HOUR, NIGHT_CHARGE_START_MINUTE, NIGHT_CHARGE_END_MINUTE))
			fare = NIGHT_CHARGE_CONSTANT_MULTIPLICATION_FACTOR * fare + NIGHT_CHARGE_CONSTANT_ADDITION_FACTOR;
		return fare;
	}
}
