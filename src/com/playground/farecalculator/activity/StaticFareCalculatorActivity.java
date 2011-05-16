/**
 * 
 */
package com.playground.farecalculator.activity;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.playground.farecalculator.R;
import com.playground.farecalculator.entities.CitiesEnum;
import com.playground.farecalculator.entities.EntitiesManager;
import com.playground.farecalculator.entities.TransportsEnum;
import com.playground.farecalculator.fares.IFare;
import com.playground.farecalculator.googleAPIAdapter.DirectionAPIAdapter;
import com.playground.farecalculator.utils.Constants;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 * 
 */
public class StaticFareCalculatorActivity extends Activity implements OnClickListener
{
	private static final String MESSAGE_FOR_DIALOG = "msg";
	private EditText sourceAddressField;
	private EditText destinationAddressField;
	private Button getFareButton;
	private Button resetButton;
	private Button goBackButton;
	private final String LOG_TAG = "StaticFareCalculator";
	private int citySelected = -1;
	private final int DIALOG_SHOW_FARE = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_locations);
		// The activity is being created.

		Intent intent = getIntent();
		citySelected = intent.getIntExtra(Constants.CITY_SELECTED, -1);

		initViews();
		initListeners();
	}

	private void initViews()
	{
		sourceAddressField = (EditText) findViewById(R.id.EditTextSourceAddress);
		destinationAddressField = (EditText) findViewById(R.id.EditTextDestinationAddress);

		// initializing button and registering for click
		getFareButton = (Button) findViewById(R.id.ButtonGetFareBetweenLocations);
		getFareButton.setEnabled(true);

		resetButton = (Button) findViewById(R.id.ButtonResetBetweenLocations);
		resetButton.setEnabled(true);

		goBackButton = (Button) findViewById(R.id.ButtonGoBackFromLocationsToStartup);
		goBackButton.setEnabled(true);
	}

	private void initListeners()
	{
		getFareButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
		goBackButton.setOnClickListener(this);
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
			String message = calculateFare(sourceAddressField.getText().toString(), destinationAddressField.getText().toString());
			Bundle bundle = new Bundle();
			bundle.putString(MESSAGE_FOR_DIALOG, message);
			showDialog(DIALOG_SHOW_FARE, bundle);
			break;
		case R.id.ButtonResetBetweenLocations:
			sourceAddressField.setText("");
			destinationAddressField.setText("");
			break;
		case R.id.ButtonGoBackFromLocationsToStartup:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		switch (id)
		{
		case DIALOG_SHOW_FARE:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Estimated Fare");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item)
				{
					dialog.dismiss();
				}
			});
			builder.setCancelable(false);
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args)
	{
		switch (id)
		{
		case DIALOG_SHOW_FARE:
			String message = args.getString(MESSAGE_FOR_DIALOG);
			// ((AlertDialog)dialog).setMessage(message);
			TextView tv = new TextView(this);
			tv.setText(message);
			((AlertDialog) dialog).setView(tv);
			break;
		default:
		}
	}

	private String calculateFare(String source, String destination)
	{
		String result = null;
		double[] distances = null;
		try
		{
			distances = DirectionAPIAdapter.getDistanceBetweenPoints("Old Guest house IIT Bombay Powai Mumbai", "rcity mall ghatkopar");
		}
		catch (IOException e)
		{
			result = getResources().getString(R.string.ioExceptionWhileCallingGoogleDirectionAPIErrorString);
			Log.e(LOG_TAG, result, e);
		}
		catch (JSONException e)
		{
			result = getResources().getString(R.string.jsonParseExceptionWhileCallingGoogleDirectionAPIErrorString);
			Log.e(LOG_TAG, result, e);
		}
		if (distances != null && distances.length > 0)
		{
			StringBuilder sb = new StringBuilder();
			Arrays.sort(distances);
			EntitiesManager em = EntitiesManager.getInstance();
			CitiesEnum city = CitiesEnum.values()[this.citySelected];
			for (TransportsEnum transport : TransportsEnum.values())
			{
				IFare fareCalculator = em.getFareCalculator(city, transport);
				if (fareCalculator != null)
				{
					double minFare = Math.ceil(fareCalculator.getFare(distances[0], 0));
					sb.append(transport.getDisplayString()).append(": Rs ").append(minFare);
					if (distances.length > 1)
					{
						double maxFare = Math.ceil(fareCalculator.getFare(distances[distances.length - 1], 0));
						sb.append(" to ").append(maxFare);
					}
					sb.append("\n");
				}
			}
			result = sb.toString();
		}
		else
		{
			result = getResources().getString(R.string.noRouteErrorString);
		}
		Log.i(LOG_TAG, Arrays.toString(distances));
		Log.i(LOG_TAG, result);
		return result;
	}
}
