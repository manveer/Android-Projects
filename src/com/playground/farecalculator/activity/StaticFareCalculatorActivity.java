/**
 * 
 */
package com.playground.farecalculator.activity;


import java.io.IOException;
import java.util.Arrays;

import org.json.JSONException;

import com.playground.farecalculator.R;
import com.playground.farecalculator.googleAPIAdapter.DirectionAPIAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author manveer
 * 
 */
public class StaticFareCalculatorActivity extends Activity implements OnClickListener 
{
	private EditText sourceAddressField;
	private EditText destinationAddressField;
	private Button getFareButton;
	private final String LOG_TAG = "StaticFareCalculator";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_locations);
		// The activity is being created.
		
		sourceAddressField = (EditText)findViewById(R.id.EditTextSourceAddress);
		destinationAddressField = (EditText)findViewById(R.id.EditTextDestinationAddress);
		
		// initializing button and registering for click
		getFareButton = (Button) findViewById(R.id.ButtonGetFareBetweenLocations);
		getFareButton.setEnabled(true);
		getFareButton.setOnClickListener(this);
		
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		// The activity is about to become visible.
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// The activity is about to be destroyed.
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.ButtonGetFareBetweenLocations:
			calculateFare(sourceAddressField.getText().toString(), destinationAddressField.getText().toString());
			break;

		default:
			break;
		}
	}
	
	private void calculateFare(String source, String destination)
	{
		double[] distances = null;
		try
		{
			distances = DirectionAPIAdapter.getDistanceBetweenPoints("Old Guest house IIT Bombay Powai Mumbai", "rcity mall ghatkopar");
		}
		catch (IOException e)
		{
			Log.e(LOG_TAG, "IOException occurred while getting distance between source and destination",e);
		}
		catch (JSONException e)
		{
			Log.e(LOG_TAG, "Unable to parse the response",e);
		}
		Log.e(LOG_TAG,Arrays.toString(distances));
	}
}
