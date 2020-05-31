package io.pantheonsite.alphaoptimus369.mrwaste.graph_module.views;

import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.ConstantsAndStaticData;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityMrWasteGraphBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.graph_module.enums.ContentState;
import io.pantheonsite.alphaoptimus369.mrwaste.graph_module.models.HumidityDataItem;


public class MrWasteGraphActivity extends BaseActivity
{

    private ActivityMrWasteGraphBinding binding;

    private ArrayList < HumidityDataItem > humidityDataItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mr_waste_graph);
        binding.setLifecycleOwner(this);

        initComponents();
        getDataFromNasaApi();
    }

    private void initComponents()
    {
    }

    private void getDataFromNasaApi()
    {
        Calendar tenDaysPrevCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        tenDaysPrevCalendar.add(Calendar.DAY_OF_YEAR, -10);
        String startTime = simpleDateFormat.format(tenDaysPrevCalendar.getTime());
        String endTime = simpleDateFormat.format(new Date());

        // Get humidity data
        humidityDataItems.clear();
        binding.layoutHumidity.setContentState(ContentState.LOADING);
        AndroidNetworking.get(ConstantsAndStaticData.NASA_DATA_FIND_BY_ORG_BASE_URL)
                .addPathParameter("protocols", ConstantsAndStaticData.PROTOCOL_HUMIDITY)
                .addQueryParameter("startdate", startTime)
                .addQueryParameter("enddate", endTime)
                .addQueryParameter("organizationname", ConstantsAndStaticData.selectedWasteItem.areaName)
                .addQueryParameter("geojson", "TRUE")
                .addQueryParameter("sample", "FALSE")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList < String > xAxisLabels = new ArrayList<>();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
                            JSONArray features = response.getJSONArray("features");

                            if (features.length() <= 0) {
                                binding.layoutHumidity.setContentState(ContentState.NO_DATA_AVAILABLE);
                                return;
                            }

                            for (int i = 0, len = features.length(); i < len; ++i) {
                                JSONObject properties = features.getJSONObject(i)
                                        .getJSONObject("properties");

                                Date measuredAt = simpleDateFormat.parse(properties.getString("humiditiesMeasuredAt"));

                                if (measuredAt != null) {
                                    Calendar previousMeasuredCalendar;
                                    Calendar currentMeasuredCalendar = Calendar.getInstance();
                                    currentMeasuredCalendar.setTime(measuredAt);

                                    if (humidityDataItems.isEmpty())
                                        previousMeasuredCalendar = null;
                                    else
                                        previousMeasuredCalendar = humidityDataItems.get(humidityDataItems.size() - 1).measuredDateCalendar;

                                    if ((previousMeasuredCalendar != null)
                                            && (previousMeasuredCalendar.get(Calendar.DAY_OF_YEAR)
                                            == currentMeasuredCalendar.get(Calendar.DAY_OF_YEAR))) {
                                        continue;
                                    }

                                    humidityDataItems.add(new HumidityDataItem(
                                            i,
                                            Float.parseFloat(properties.getString("humiditiesDewpoint")),
                                            Float.parseFloat(properties.getString("humiditiesRelativeHumidityPercent")),
                                            currentMeasuredCalendar
                                    ));

                                    xAxisLabels.add(new SimpleDateFormat("dd MM", Locale.ENGLISH).format(measuredAt));
                                }
                            }

                            binding.layoutHumidity.setContentState(ContentState.DATA_AVAILABLE);
                            processHumidityData(xAxisLabels);

                        } catch (JSONException | ParseException | NumberFormatException e) {
                            Log.e(ConstantsAndStaticData.LOG_TAG, "onResponse: ", e);
                            binding.layoutHumidity.setContentState(ContentState.NO_DATA_AVAILABLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(ConstantsAndStaticData.LOG_TAG,
                                "onError: " + anError.getErrorDetail(),
                                anError.getCause()
                        );

                        binding.layoutHumidity.setContentState(ContentState.NO_DATA_AVAILABLE);
                    }
                });
    }

    private void processHumidityData(ArrayList<String> xAxisLabels)
    {
        //Log.d(ConstantsAndStaticData.LOG_TAG, "processHumidityData: " + humidityDataItems.toString());

        ArrayList < BarEntry > dewpointEntries = new ArrayList<>();
        ArrayList < BarEntry > relativeHumidityEntries = new ArrayList<>();

        for (int i = 0, len = humidityDataItems.size(); i < len; ++i) {
            HumidityDataItem item = humidityDataItems.get(i);

            dewpointEntries.add(new BarEntry(i, item.dewpoint));
            relativeHumidityEntries.add(new BarEntry(i, item.relativeHumidityPercent));
        }

        BarDataSet barDataSetDewpoint = new BarDataSet(dewpointEntries, getString(R.string.dewpoint));
        BarDataSet barDataSetRelativeHumidity = new BarDataSet(relativeHumidityEntries, getString(R.string.relative_humidity));

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData barDataDewpoint = new BarData(barDataSetDewpoint, barDataSetRelativeHumidity);
//        BarData barDataRelativeHumidity = new BarData(barDataSetRelativeHumidity);
        barDataDewpoint.setBarWidth(barWidth);
        binding.layoutHumidity.chart.setData(barDataDewpoint);
        binding.layoutHumidity.chart.groupBars(0, groupSpace, barSpace);
        binding.layoutHumidity.chart.setFitBars(true);

        XAxis xAxis = binding.layoutHumidity.chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xAxisLabels.get((int) value);
            }
        });

        binding.layoutHumidity.chart.invalidate();
    }

}
