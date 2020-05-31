package io.pantheonsite.alphaoptimus369.mrwaste.commons.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.Random;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views.SignUpActivity;


public class MyNotificationManager
{

    public static void showNotification(@NonNull Context applicationContext)
    {
        Intent intent = new Intent(applicationContext, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                applicationContext,
                Math.abs(new Random().nextInt()) /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                applicationContext, "mr_waste_channel");

        notificationBuilder.setSmallIcon(R.drawable.icon_notification);

        notificationBuilder.setLargeIcon(
                BitmapFactory.decodeResource(
                        applicationContext.getResources(),
                        R.drawable.icon_notification
                )
        );

        String title = "New waste collection request!",
                description = "Someone from 51/B Shahjahan Road, Dhaka has requested for waste collection.";

        notificationBuilder
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));

        NotificationManager notificationManager = (NotificationManager)
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "mr_waste_channel",
                    "mr_waste_channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }

        if (notificationManager != null) {
            notificationManager.notify(
                    "sample_noti" /* Tag of notification */,
                    5 /* ID of notification */,
                    notificationBuilder.build());
        }
    }

}
