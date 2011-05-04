/**
 * 
 */
package com.bitmagic.farecalculator;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.util.Log;

/**
 * @author manveer
 * 
 */
public class MotionDetector
{
	private SensorManager sm;
	private Sensor gravitySensor;
	private Sensor magneticSensor;
	private boolean isMoving;

	/** Keeps the current acceleration, including gravity, of the device **/
	private float accels[] = null;
	/** Keeps the magnetic force of the device **/
	private float mags[] = null;

	private static final int DELAY_IN_MICRO_SECONDS = SensorManager.SENSOR_DELAY_GAME;
	private static final double ACCEL_THRESHOLD = 0.2;
	private float currentAcccel;

	/** Called when the activity is first created. */
	public MotionDetector(Context context)
	{
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		SensorEventListener customEventListener = new CustomSensorEventListener();

		List<Sensor> sensorsList = sm.getSensorList(Sensor.TYPE_ALL);
		if (sensorsList != null && sensorsList.size() > 0)
		{
			for (Sensor sensor : sensorsList)
			{
				switch (sensor.getType())
				{
				case Sensor.TYPE_ACCELEROMETER:
					gravitySensor = sensor;
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					magneticSensor = sensor;
					break;
				default:
					break;
				}
			}
		}
		sm.registerListener(customEventListener, gravitySensor, DELAY_IN_MICRO_SECONDS);
		sm.registerListener(customEventListener, magneticSensor, DELAY_IN_MICRO_SECONDS);
	}

	public float getCurrentAccel()
	{
		return this.currentAcccel;
	}

	public boolean isMoving()
	{
		return this.isMoving;
	}

	private class CustomSensorEventListener implements SensorEventListener
	{

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if (event.accuracy != SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
				return;
			boolean isChanged = false;
			switch (event.sensor.getType())
			{
			case Sensor.TYPE_MAGNETIC_FIELD:
				mags = event.values.clone();
				isChanged = true;
				break;
			case Sensor.TYPE_ACCELEROMETER:
				accels = event.values.clone();
				isChanged = true;
				break;
			default:
				break;
			}
			if (mags != null && accels != null && isChanged)
			{
				float[] R = new float[16];

				float tilt_data[] = new float[3];
				// Calculate the rotation matrix, and use that to get the
				// orientation:
				if (SensorManager.getRotationMatrix(R, null, accels, mags))
					SensorManager.getOrientation(R, tilt_data);

				float[] accelerationWithoutGravity = new float[3];
				float[] invR = new float[16];
				Matrix.invertM(invR, 0, R, 0);
				for (int i = 0; i < 3; i++)
					accelerationWithoutGravity[i] = accels[i] - (SensorManager.GRAVITY_EARTH * invR[4 * i + 2]);

				currentAcccel = (float) Math.sqrt(accelerationWithoutGravity[0] * accelerationWithoutGravity[0]
						+ accelerationWithoutGravity[1] * accelerationWithoutGravity[1] + accelerationWithoutGravity[2]
						* accelerationWithoutGravity[2]);

				//Log.e("MotionDetector","CurrentAcceleration " + currentAcccel);
				if (currentAcccel > ACCEL_THRESHOLD)
					isMoving = true;
				else
					isMoving = false;
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
			// TODO Auto-generated method stub

		}
	}
}
