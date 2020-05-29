package io.pantheonsite.alphaoptimus369.mrwaste.commons.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views.LogInActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views.SignUpActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.Constants;


public class ActivityStarter
{

    public static void startLogInActivity(@NonNull Context context, boolean finishPreviousActivity)
    {
        Intent intent = new Intent(context, LogInActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);

        intent.putExtras(data);
        context.startActivity(intent);
    }

    public static void startSignUpActivity(@NonNull Context context, boolean finishPreviousActivity)
    {
        Intent intent = new Intent(context, SignUpActivity.class);

        Bundle data = new Bundle();
        data.putBoolean(Constants.EXTRA_PREV_ACTIVITY_FINISHED, finishPreviousActivity);

        intent.putExtras(data);
        context.startActivity(intent);
    }

}
