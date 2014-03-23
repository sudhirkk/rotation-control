/**
 * 
 */
package com.indicapps.rotationcontrol;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Sudhir
 *
 */
public class RotationControlReceiver extends BroadcastReceiver  {
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	private static final int MY_NOTIFICATION_ID = 1;
	@Override
	public void onReceive(Context context, Intent intent) {
		toggleRotationLock(context);
	}
	
	void toggleRotationLock(Context context) {
		if(android.provider.Settings.System.getInt(context.getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
			android.provider.Settings.System.putInt(context.getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
			Toast.makeText(context, "Rotation OFF", Toast.LENGTH_SHORT).show();
			cancelAllNotifications(context);
			sendNewNotification(context, true);
			Log.i("DummyActivity", "Rotation OFF");
		}
		else {
			android.provider.Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
			Toast.makeText(context, "Rotation ON", Toast.LENGTH_SHORT).show();
			cancelAllNotifications(context);
			sendNewNotification(context, false);
			Log.i("DummyActivity", "Rotation ON");
		}
	}	

	private void cancelAllNotifications(Context context) {
		NotificationManager mNotificationManager = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(MY_NOTIFICATION_ID);
	}

	private int getIcon(boolean isLocked) {
		if (isLocked) {
			return R.drawable.ic_action_screen_locked_to_portrait;
		}
		else {
			return R.drawable.ic_action_screen_rotation;
		}
	}
	private CharSequence getContextText(boolean locked) {
		if (locked) {
			return ("Click to turn on screen rotation");
		}
		else {
			return ("Click to turn off screen rotation");
		}
	}
	 void sendNewNotification(Context context, boolean locked) {
		 
		 	Bitmap icon = BitmapFactory.decodeResource(context.getResources(), getIcon(locked));
			mNotificationIntent = new Intent(context.getApplicationContext(),
					RotationControlReceiver.class);
			mContentIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0,
					mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
					context.getApplicationContext())
			.setContentTitle("Screen Rotation")
			.setSmallIcon(getIcon(locked))
			.setContentText(getContextText(locked))
			.setContentIntent(mContentIntent)
			.setWhen(0)
			//.setContent(mContentView)
			.setAutoCancel(false);
			
			Notification notification = notificationBuilder.build();
			notification.flags |= Notification.FLAG_ONGOING_EVENT;  
			notification.flags |= Notification.FLAG_NO_CLEAR; 

			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(MY_NOTIFICATION_ID,
					notification);
	 }
}
