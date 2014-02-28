package it.feio.android.omninotes.widget;


import it.feio.android.omninotes.DetailActivity;
import it.feio.android.omninotes.ListActivity;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.utils.Constants;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;

public abstract class WidgetProvider extends AppWidgetProvider {
	public static String EXTRA_WORD = "it.feio.android.omninotes.widget.WORD";
	public static String TOAST_ACTION = "it.feio.android.omninotes.widget.NOTE";
	public static String EXTRA_ITEM = "it.feio.android.omninotes.widget.EXTRA_FIELD";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}
	

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// Get all ids
		ComponentName thisWidget = new ComponentName(context, getClass());
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int appWidgetId : allWidgetIds) {
			Log.d(Constants.TAG, "WidgetProvider onUpdate() widget " + appWidgetId);
			// Get the layout for and attach an on-click listener to views			
			setLayout(context, appWidgetManager, appWidgetId);
		}
	    super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	
	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		Log.d(Constants.TAG, "Widget size changed");
		setLayout(context, appWidgetManager, appWidgetId);
	}
	

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setLayout(Context context, AppWidgetManager appWidgetManager, int widgetId) {

		// Create an Intent to launch DetailActivity
		Intent intentDetail = new Intent(context, DetailActivity.class);
		PendingIntent pendingIntentDetail = PendingIntent.getActivity(context, 0, intentDetail,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		// Create an Intent to launch ListActivity
		Intent intentList = new Intent(context, ListActivity.class);
		PendingIntent pendingIntentList = PendingIntent.getActivity(context, 0, intentList, 0);

		// Create an Intent to launch DetailActivity to take a photo
		Intent intentDetailPhoto = new Intent(context, DetailActivity.class);
		intentDetailPhoto.setAction(Intent.ACTION_PICK);
		PendingIntent pendingIntentDetailPhoto = PendingIntent.getActivity(context, 0, intentDetailPhoto,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		// Check various dimensions aspect of widget to choose between layouts
		boolean isSmall = false;
		boolean isSingleLine = true;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			Bundle options = appWidgetManager.getAppWidgetOptions(widgetId);
			// Width check
			isSmall = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) < 110;
			// Height check
			isSingleLine = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) < 110;
		} 
		
		// Creation of a map to associate PendingIntent(s) to views
		SparseArray<PendingIntent> map = new SparseArray<PendingIntent>();
		map.put(R.id.list, pendingIntentList);
		map.put(R.id.add, pendingIntentDetail);
		map.put(R.id.camera, pendingIntentDetailPhoto);
		
		RemoteViews views = getRemoteViews(context, widgetId, isSmall, isSingleLine, map);
				
		// Tell the AppWidgetManager to perform an update on the current app
		// widget
		appWidgetManager.updateAppWidget(widgetId, views);
	}
	
	
	abstract protected RemoteViews getRemoteViews(Context context, int widgetId, boolean isSmall, boolean isSingleLine, SparseArray<PendingIntent> pendingIntentsMap);

}