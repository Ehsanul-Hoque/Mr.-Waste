package io.pantheonsite.alphaoptimus369.mrwaste.graph_module.models;

import androidx.annotation.NonNull;

import java.util.Calendar;


public class HumidityDataItem
{

    public long id;
    public float dewpoint;
    public float relativeHumidityPercent;
    public Calendar measuredDateCalendar;


    public HumidityDataItem(long id, float dewpoint, float relativeHumidityPercent,
                            Calendar measuredDateCalendar)
    {
        this.id = id;
        this.dewpoint = dewpoint;
        this.relativeHumidityPercent = relativeHumidityPercent;
        this.measuredDateCalendar = measuredDateCalendar;
    }


    @NonNull
    @Override
    public String toString() {
        return "HumidityDataItem{" +
                "\nid=" + id +
                ",\ndewpoint=" + dewpoint +
                ",\nrelativeHumidityPercent=" + relativeHumidityPercent +
                ",\nmeasuredDateCalendar=" + measuredDateCalendar +
                "\n}";
    }

}
