package io.pantheonsite.alphaoptimus369.mrwaste.home_module.models;

import java.util.ArrayList;


public class WasteItem
{

    public long id;
    public String areaName;
    public int requestCount;
    public double longitude, latitude;
    public ArrayList < WasteRequestConsumerItem > wasteRequestConsumerItems;


    public WasteItem(long id, String areaName, int requestCount, double longitude, double latitude,
                     ArrayList<WasteRequestConsumerItem> wasteRequestConsumerItems)
    {
        this.id = id;
        this.areaName = areaName;
        this.requestCount = requestCount;
        this.longitude = longitude;
        this.latitude = latitude;
        this.wasteRequestConsumerItems = wasteRequestConsumerItems;
    }

}
