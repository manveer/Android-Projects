/**
 * 
 */
package com.playground.farecalculator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.playground.farecalculator.R;
import com.playground.farecalculator.entities.CitiesEnum;
import com.playground.farecalculator.entities.EntitiesManager;
import com.playground.farecalculator.utils.Constants;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 * 
 */
public class StartupActivity extends Activity implements OnClickListener, OnItemSelectedListener
{
	private Spinner citySpinner;
	private Button startJourneyButton;
	private Button estimateFareButton;
	private int citySelectedIndex;
	
	private static final int DIALOG_START_JOURNEY = 0;
	
	private static final int STATIC_FARE_CALCULATOR = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.citySelectedIndex = -1;
		setContentView(R.layout.start);
		// The activity is being created.

		initViews();
		setListeners();
	}

	private void initViews()
	{
		citySpinner = (Spinner) findViewById(R.id.spinnerCity);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, EntitiesManager
				.getInstance().getCities());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		citySpinner.setAdapter(adapter);

		startJourneyButton = (Button) findViewById(R.id.btnStartJourney);
		startJourneyButton.setEnabled(true);

		estimateFareButton = (Button) findViewById(R.id.btnEstimateFare);
		estimateFareButton.setEnabled(true);
	}

	private void setListeners()
	{
		citySpinner.setOnItemSelectedListener(this);
		startJourneyButton.setOnClickListener(this);
		estimateFareButton.setOnClickListener(this);
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

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		this.citySelectedIndex = pos;
		//Toast.makeText(parent.getContext(), "The city is " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
	}

	public void onNothingSelected(AdapterView parent)
	{
		// Do nothing.
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == STATIC_FARE_CALCULATOR)
		{
		}
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btnEstimateFare:
			// start the estimate fare activity now
			startStaticFareCalculatorActivity(this.citySelectedIndex);
			break;
		case R.id.btnStartJourney:
			showDialog(DIALOG_START_JOURNEY);
			break;
		default:
			break;
		}
	}
	
	private void startStaticFareCalculatorActivity(int citySelected)
	{
		Intent intent = new Intent(this, StaticFareCalculatorActivity.class);
		intent.putExtra(Constants.CITY_SELECTED, citySelected);
		startActivityForResult(intent, STATIC_FARE_CALCULATOR);
	}

	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		CitiesEnum city = CitiesEnum.values()[citySelectedIndex];
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] transports = EntitiesManager.getInstance().getTransportsForCity(city);
		if (transports == null || transports.length == 0)
		{
			builder.setMessage("Sorry, no mode of transport available for " + city.name());
			dialog = builder.create();
			builder.setCancelable(false);
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();
				}
			});
		}
		else
		{
			switch (id)
			{
			case DIALOG_START_JOURNEY:
				builder.setTitle("How are you travelling?");
				builder.setItems(transports, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item)
					{
						// using item, start the journey activity
					}
				});
				builder.setCancelable(false);
				dialog = builder.create();
				break;
			default:
				dialog = null;
			}
		}
		return dialog;
	}
}

