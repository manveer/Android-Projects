/**
 * 
 */
package com.playground.farecalculator.activity;


import com.playground.farecalculator.R;

import android.app.Activity;
import android.os.Bundle;
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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
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
			
			break;

		default:
			break;
		}
	}
}
