package io.pantheonsite.alphaoptimus369.mrwaste.graph_module.models;

import java.util.Calendar;


public class TemperatureDataItem
{

    public long id;
    public float temperature;
    public float minTemperature, maxTemperature;
    public Calendar measuredDateCalendar;


    public TemperatureDataItem(long id, float temperature, float minTemperature,
                               float maxTemperature, Calendar measuredDateCalendar)
    {
        this.id = id;
        this.temperature = temperature;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.measuredDateCalendar = measuredDateCalendar;
    }


    @Override
    public String toString() {
        return "TemperatureDataItem{" +
                "id=" + id +
                ",\ntemperature=" + temperature +
                ",\nminTemperature=" + minTemperature +
                ",\nmaxTemperature=" + maxTemperature +
                ",\nmeasuredDateCalendar=" + measuredDateCalendar +
                "\n}";
    }
}
