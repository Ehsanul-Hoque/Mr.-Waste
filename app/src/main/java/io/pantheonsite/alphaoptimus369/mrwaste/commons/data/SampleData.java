package io.pantheonsite.alphaoptimus369.mrwaste.commons.data;

import java.util.ArrayList;
import java.util.Arrays;

import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.ProductItem;


public class SampleData
{

    public static ArrayList < ProductItem > productsMarkedForSelling;
    public static ArrayList < ProductItem > productsPrices;

    static {
        productsMarkedForSelling = new ArrayList<>(Arrays.asList(
                new ProductItem(100, "Mask N95", 1.25, 2),
                new ProductItem(101, "Bottle, 1 ltr", 0.8, 5),
                new ProductItem(102, "Biodegradable mask", 1, 1)
        ));

        productsPrices = new ArrayList<>(Arrays.asList(
                new ProductItem(200, "PPE", 50.25, 1),
                new ProductItem(201, "Hand sanitizer bottle", 0.5, 1),
                new ProductItem(202, "Mask Kn95", 1.5, 1),
                new ProductItem(203, "Surgical mask", 0.2, 1),
                new ProductItem(204, "Gloves", 4, 1)
        ));
    }

}
