package io.pantheonsite.alphaoptimus369.mrwaste.commons.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.ConstantsAndStaticData;


public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(ConstantsAndStaticData.LOG_TAG, "Message data payload: " + remoteMessage.getData());
            // MyNotificationManager.handleRemoteMessage(this, remoteMessage);

        } else
            Log.e(ConstantsAndStaticData.LOG_TAG, "No data with message data :(");
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }

}
