package com.gprs.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "542";
    public Context context = this;
    String strendPointId;
    public static final Strategy STRATEGY = Strategy.P2P_POINT_TO_POINT;
    public static final String SERVICE_ID="120001";
    private Ringtone ringtone;
    MediaPlayer player;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, victimalert.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Geo-fencing ON")
                .setContentText("You are protected with geo-fencing")
                .setSmallIcon(R.drawable.collective_intelligence_icon_use)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onCreate() {
        Nearby.getConnectionsClient(context).stopAllEndpoints();
        Nearby.getConnectionsClient(context).stopAdvertising();
        Nearby.getConnectionsClient(context).stopDiscovery();
        startAdvertising();
        startDiscovery();

        Toast.makeText(this, "Scanning in background", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onDestroy() {
        if(player!=null)
            player.stop();

        Nearby.getConnectionsClient(context).stopAllEndpoints();
        Nearby.getConnectionsClient(context).stopAdvertising();
        Nearby.getConnectionsClient(context).stopDiscovery();

        Toast.makeText(this, "Stopped scanning", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }
    private void startAdvertising () {

        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(this).startAdvertising("Shriram G", SERVICE_ID, new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull final String endPointId, @NonNull ConnectionInfo connectionInfo) {


                Nearby.getConnectionsClient(context).acceptConnection(endPointId, mPayloadCallback);
            }
            @Override
            public void onConnectionResult(@NonNull String endPointId, @NonNull ConnectionResolution connectionResolution) {
                switch (connectionResolution.getStatus().getStatusCode()) {
                    case ConnectionsStatusCodes.STATUS_OK:
                        // We're connected! Can now start sending and receiving data.
                        strendPointId = endPointId;
                        sendPayLoad(strendPointId);
                        break;
                    case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                        // The connection was rejected by one or both sides.
                        break;
                    case ConnectionsStatusCodes.STATUS_ERROR:
                        // The connection broke before it was able to be accepted.
                        break;
                    default:
                        // Unknown status code
                }
            }
            @Override
            public void onDisconnected(@NonNull String s) {
                strendPointId = null;
            }
        }, advertisingOptions);
    }

    private void sendPayLoad(final String endPointId) {
        final SharedPreferences pref;
        final SharedPreferences.Editor editor;

        pref = getApplicationContext().getSharedPreferences("user", 0);
        Payload bytesPayload = Payload.fromBytes(('0'+pref.getString("status","")).getBytes());

        Nearby.getConnectionsClient(context).sendPayload(endPointId, bytesPayload);
        bytesPayload = Payload.fromBytes(('1'+pref.getString("role","")).getBytes());

        Nearby.getConnectionsClient(context).sendPayload(endPointId, bytesPayload);

    }
    private void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(context).
                startDiscovery(SERVICE_ID, new EndpointDiscoveryCallback() {
                    @Override
                    public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                        Nearby.getConnectionsClient(context).
                                requestConnection("Shriram G", endpointId, new ConnectionLifecycleCallback() {
                                    @Override
                                    public void onConnectionInitiated(@NonNull final String endpointId, @NonNull ConnectionInfo connectionInfo) {

                                        Nearby.getConnectionsClient(context).acceptConnection(endpointId, mPayloadCallback);

                                    }
                                    @Override
                                    public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
                                        switch (connectionResolution.getStatus().getStatusCode()) {
                                            case ConnectionsStatusCodes.STATUS_OK:
                                                strendPointId = s;
                                                sendPayLoad(strendPointId);
                                                break;
                                            case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                                                // The connection was rejected by one or both sides.
                                                break;
                                            case ConnectionsStatusCodes.STATUS_ERROR:
                                                // The connection broke before it was able to be accepted.
                                                break;
                                            default:
                                                // Unknown status code
                                        }
                                    }
                                    @Override
                                    public void onDisconnected(@NonNull String s) {
                                    }
                                });
                    }
                    @Override
                    public void onEndpointLost(@NonNull String s) {
                        // disconnected
                    }
                }, discoveryOptions);
    }

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            final   byte[] receivedBytes = payload.asBytes();
            String strReceived=new String(receivedBytes);

            if(strReceived.charAt(0)=='0'){
                if(strReceived.substring(1).equals("victim")) {
                    player = MediaPlayer.create(context,
                            Settings.System.DEFAULT_RINGTONE_URI);
                    player.start();
                    new notificationHelper(context).createNotification("Found new victim", "Stay away ! Stay safe !");
                    Intent dialogIntent = new Intent(context, foundvictim.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(5000);
                    }

                }
            }
            else {
                new notificationHelper(context).createNotification("Found new",strReceived.substring(1)+" found in your surrounding");
            }

        }
        @Override
        public void onPayloadTransferUpdate(@NonNull String s,
                                            @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                // Do something with is here...
            }
        }
    };

}
