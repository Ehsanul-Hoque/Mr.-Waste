package io.pantheonsite.alphaoptimus369.mrwaste.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.ar_module.AugmentedImageActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views.LogInActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views.SignUpActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.Constants;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.views.ConsumerHomeActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.views.MrWasteHomeActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.maps_module.views.MapsActivity;


public class ActivityStarter
{

    public static void startLogInActivity(@NonNull Context context, boolean finishPreviousActivity)
    {
        Intent intent = new Intent(context, LogInActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);

        intent
                .putExtras(data)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        if (finishPreviousActivity && (context instanceof Activity)) {
            ((Activity) context).finish();
        }
    }

    public static void startSignUpActivity(@NonNull Context context, boolean finishPreviousActivity)
    {
        Intent intent = new Intent(context, SignUpActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);

        intent
                .putExtras(data)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        if (finishPreviousActivity && (context instanceof Activity)) {
            ((Activity) context).finish();
        }
    }

    public static void startMapsActivity(@NonNull Context context, boolean finishPreviousActivity)
    {
        Intent intent = new Intent(context, MapsActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);

        intent.putExtras(data);
        context.startActivity(intent);

        if (finishPreviousActivity && (context instanceof Activity)) {
            ((Activity) context).finish();
        }
    }

    public static void startMapsActivityForSignUp(@NonNull Context context,
                                                  boolean finishPreviousActivity,
                                                  @NonNull String email,
                                                  @NonNull String phoneNo,
                                                  @NonNull String userType,
                                                  @NonNull String password)
    {
        Intent intent = new Intent(context, MapsActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);
        data.putString(Constants.EXTRA_EMAIL, email);
        data.putString(Constants.EXTRA_CONTACT_NO, phoneNo);
        data.putString(Constants.EXTRA_USER_TYPE, userType);
        data.putString(Constants.EXTRA_PASSWORD, password);

        intent.putExtras(data);
        context.startActivity(intent);

        if (finishPreviousActivity && (context instanceof Activity)) {
            ((Activity) context).finish();
        }
    }

    public static void startHomeActivity(@NonNull Context context, boolean finishPreviousActivity)
    {
        Intent intent;

        if (Constants.currentUser != null
                && Constants.currentUser.userType.equals(context.getString(R.string.consumer)))
            intent = new Intent(context, ConsumerHomeActivity.class);
        else
            intent = new Intent(context, MrWasteHomeActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);

        intent
                .putExtras(data)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        if (finishPreviousActivity && (context instanceof Activity)) {
            ((Activity) context).finish();
        }
    }

    public static void startAugmentedImageActivity(@NonNull Context context, boolean finishPreviousActivity)
    {
        Intent intent = new Intent(context, AugmentedImageActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);

        intent
                .putExtras(data)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        if (finishPreviousActivity && (context instanceof Activity)) {
            ((Activity) context).finish();
        }
    }

}
