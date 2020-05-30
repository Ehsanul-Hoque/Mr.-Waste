package io.pantheonsite.alphaoptimus369.mrwaste.commons.utils;

import java.util.Locale;


public class Utils
{

    public static String getTotalPriceString(double singleItemPriceInDollar, int itemCount)
    {
        return String.format(
                Locale.getDefault(),
                "%.2f$ x %d = %.2f$",
                singleItemPriceInDollar,
                itemCount,
                singleItemPriceInDollar * itemCount
        );
    }

}
