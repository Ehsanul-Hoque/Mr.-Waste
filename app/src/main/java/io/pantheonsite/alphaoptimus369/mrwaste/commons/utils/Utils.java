package io.pantheonsite.alphaoptimus369.mrwaste.commons.utils;

import android.content.Context;

import androidx.annotation.NonNull;

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

}
