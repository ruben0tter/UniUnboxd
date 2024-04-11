package com.example.uniunboxd.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.uniunboxd.R;
import com.example.uniunboxd.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * The NotificationService class extends FirebaseMessagingService to handle the creation and management of notifications.
 * It includes methods for handling new tokens, receiving messages, and retrieving the device token.
 */
public class NotificationService extends FirebaseMessagingService {

    /**
     * Called when a new token is generated.
     * The token is logged and stored in shared preferences.
     * @param s The new token.
     */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("DeviceToken", s);
        getSharedPreferences("myPrefs", MODE_PRIVATE).edit().putString("deviceToken", s).apply();
    }

    /**
     * Called when a message is received.
     * A notification is created and displayed.
     * @param remoteMessage The received message.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification_logo)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    /**
     * Retrieves the device token from shared preferences.
     * @param c The application context.
     * @return The device token, or null if it does not exist.
     */
    public static String getDeviceToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return prefs.getString("deviceToken", null);
    }
}
