package com.indicapps.rotationcontrol;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import android.provider.Settings;

public class RotationControlActivity extends Activity {

	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}	
		
	public void onCheckboxClicked(View view) {
		CheckBox chk1 = (CheckBox)findViewById(R.id.checkBox1);
		switch (view.getId()) {
		case R.id.checkBox1:
			if (chk1.isChecked()) {
				buildNotification();
			}
			else {
				cancelAllNotifications();
			}
		}
	}
	
	private void cancelAllNotifications() {
		NotificationManager mNotificationManager = 
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
	}
	
	private boolean isRotationLocked() {
		if(android.provider.Settings.System.getInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
			return false;
		}
		else {
			return true;
		}
	}
	private int getIcon(boolean locked) {
		if (locked)
			return R.drawable.ic_action_screen_locked_to_portrait;
		else
			return R.drawable.ic_action_screen_rotation;
	}
	private CharSequence getContentText(boolean locked) {
		if (locked) {
			return ("Click to turn on screen rotation");
		}
		else {
			return ("Click to turn off screen rotation");
		}
	}
	private void buildNotification() {

		mNotificationIntent = new Intent(getApplicationContext(),
				RotationControlReceiver.class);
		mContentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
	
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
		//Notification.Builder notificationBuilder = new Notification.Builder(
				getApplicationContext())
		.setContentTitle("Rotation Control")
		.setSmallIcon(getIcon(isRotationLocked()))
		.setContentText(getContentText(isRotationLocked()))
		.setContentIntent(mContentIntent)
		.setWhen(0)
		//.setContent(mContentView)
		.setAutoCancel(false);
		//notificationBuilder.
		Notification notification = notificationBuilder.build();
		notification.flags |= Notification.FLAG_ONGOING_EVENT;  
		notification.flags |= Notification.FLAG_NO_CLEAR; 

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notification);
	}
	
	 void toggleRotationLock() {
		if(android.provider.Settings.System.getInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
			android.provider.Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
			Toast.makeText(RotationControlActivity.this, "Rotation OFF", Toast.LENGTH_SHORT).show();
	    }
	    else {
	    	android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
	    	Toast.makeText(RotationControlActivity.this, "Rotation ON", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
			Dialog dialog = new Dialog(this);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setTitle("Credits");
			dialog.setContentView(R.layout.credit);
			dialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
