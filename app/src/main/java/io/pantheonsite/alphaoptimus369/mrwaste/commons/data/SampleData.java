package io.pantheonsite.alphaoptimus369.mrwaste.commons.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.ProductItem;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.WasteItem;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.WasteRequestConsumerItem;


public class SampleData
{

    public static ArrayList < ProductItem > productsMarkedForSelling;
    public static ArrayList < ProductItem > productsPrices;
    public static ArrayList < WasteItem > wasteItems;

    public static String[] sampleEmails = new String[] {
            "ab12@gmail.com", "cd34@gmail.com", "ef56@gmail.com", "gh78@gmail.com",
            "ij90@gmail.com", "kl12@gmail.com", "mn34@gmail.com", "op56@gmail.com",
            "qr78@gmail.com", "st90@gmail.com", "uv12@gmail.com", "wx34@gmail.com",
            "yz56@gmail.com"
    };

    public static String[] samplePhones = new String[] {
            "15567208810", "15591589108", "87646946946", "51035166541",
            "51651032166", "68103541613", "81035416516", "68103546568",
            "23446354324", "24364452436", "98104180919", "85460318918"
    };


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

        wasteItems = new ArrayList<>(Arrays.asList(
                new WasteItem(
                        1001,
                        "Anthony Carnevale Elementary School, USA",
                        5,
                        41.8,
                        -71.5,
                        getRandomWasteRequestConsumerItems(5)
                ),
                new WasteItem(
                        1002,
                        "Nathan Bishop Middle School, USA",
                        5,
                        41.8,
                        -71.4,
                        getRandomWasteRequestConsumerItems(2)
                ),
                new WasteItem(
                        1003,
                        "Webster Avenue Elementary School, USA",
                        5,
                        41.8,
                        -71.4,
                        getRandomWasteRequestConsumerItems(1)
                )
        ));
    }

    private static ArrayList < WasteRequestConsumerItem > getRandomWasteRequestConsumerItems(int count)
    {
        ArrayList < WasteRequestConsumerItem > wasteRequestConsumerItems = new ArrayList<>();

        for (int i = 0; i < count; ++i) {
            wasteRequestConsumerItems.add(
                    new WasteRequestConsumerItem(
                            i,
                            sampleEmails[new Random().nextInt(sampleEmails.length)],
                            samplePhones[new Random().nextInt(samplePhones.length)],
                            "Anthony Carnevale Elementary School, USA"
                    )
            );
        }

        return wasteRequestConsumerItems;
    }

}
