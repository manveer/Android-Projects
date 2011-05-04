/**
 * 
 */
package com.bitmagic.farecalculator;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @author manveer
 * 
 */
public class CurrentLocationsOverlay extends ItemizedOverlay<OverlayItem>
{
	private OverlayItem mOverlays;
	private Context mContext;

	public CurrentLocationsOverlay(Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
	}

	public CurrentLocationsOverlay(Drawable defaultMarker, Context context)
	{
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	public void addOverlay(OverlayItem overlay)
	{
		mOverlays = overlay;
		populate();
	}

	@Override
	protected OverlayItem createItem(int arg0)
	{
		return mOverlays;
	}

	@Override
	public int size()
	{
		if(mOverlays != null)
			return 1;
		return 0;
	}

	@Override
	protected boolean onTap(int index)
	{
		OverlayItem item = mOverlays;
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
}
