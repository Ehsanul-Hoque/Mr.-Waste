package io.pantheonsite.alphaoptimus369.mrwaste.commons.utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

import io.pantheonsite.alphaoptimus369.mrwaste.R;


public class Utils
{

    public static String getTotalPriceString(double singleItemPriceInDollar, int itemCount)
    {
        return String.format(
                Locale.ENGLISH,
                "%.2f$ x %d = %.2f$",
                singleItemPriceInDollar,
                itemCount,
                singleItemPriceInDollar * itemCount
        );
    }

    public static String getTotalRequestsString(@NonNull Context context, int requestCount)
    {
        return String.format(
                Locale.ENGLISH,
                "%d %s",
                requestCount,
                context.getResources().getQuantityString(R.plurals.requests, requestCount)
        );
    }

    public static float getValidFloat(@Nullable String text, float defaultValue)
    {
        if (TextUtils.isEmpty(text))
            return defaultValue;

        try {
            return Float.parseFloat(text);

        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
