package io.pantheonsite.alphaoptimus369.mrwaste.graph_module.views;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.ConstantsAndStaticData;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.Utils;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityMrWasteGraphBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.LayoutGraphBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.graph_module.enums.ContentState;
import io.pantheonsite.alphaoptimus369.mrwaste.graph_module.models.HumidityDataItem;
import io.pantheonsite.alphaoptimus369.mrwaste.graph_module.models.TemperatureDataItem;


public class MrWasteGraphActivity extends BaseActivity
{

    private ActivityMrWasteGraphBinding binding;

    private ArrayList < HumidityDataItem > humidityDataItems = new ArrayList<>();
    private ArrayList < TemperatureDataItem > airTempDataItems = new ArrayList<>();
    private ArrayList < TemperatureDataItem > waterTempDataItems = new ArrayList<>();
    private ArrayList < TemperatureDataItem > surfaceTempDataItems = new ArrayList<>();
    private ArrayList < TemperatureDataItem > soilTempDataItems = new ArrayList<>();


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

                                    String humiditiesDewpoint = properties.getString("humiditiesDewpoint");
                                    String humiditiesRelativeHumidityPercent = properties.getString("humiditiesRelativeHumidityPercent");

                                    humidityDataItems.add(new HumidityDataItem(
                                            i,
                                            Utils.getValidFloat(humiditiesDewpoint, 0),
                                            Utils.getValidFloat(humiditiesRelativeHumidityPercent, 0),
                                            currentMeasuredCalendar
                                    ));

                                    xAxisLabels.add(new SimpleDateFormat("dd MM", Locale.ENGLISH).format(measuredAt));
                                }
                            }

                            binding.layoutHumidity.setContentState(ContentState.DATA_AVAILABLE);
                            processHumidityData(xAxisLabels);

                        } catch (Exception e) {
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

        // Get air temperature data
        getTemperatureData(
                airTempDataItems,
                binding.layoutAirTemperature,
                ConstantsAndStaticData.PROTOCOL_AIR_TEMP_DAILY,
                startTime,
                endTime,
                "airtempdailiesMeasuredAt",
                "airtempdailiesCurrentTemp",
                "airtempdailiesMinimumTemp",
                "airtempdailiesMaximumTemp"
        );

        // Get water temperature data
        getTemperatureData(
                waterTempDataItems,
                binding.layoutWaterTemperature,
                ConstantsAndStaticData.PROTOCOL_WATER_TEMP,
                startTime,
                endTime,
                "watertemperaturesMeasuredAt",
                "watertemperaturesWaterTempC",
                "watertemperaturesWaterTempC",
                "watertemperaturesWaterTempC"
        );

        // Get surface temperature data
        getTemperatureData(
                surfaceTempDataItems,
                binding.layoutSurfaceTemperature,
                ConstantsAndStaticData.PROTOCOL_SURFACE_TEMP,
                startTime,
                endTime,
                "surfacetemperaturesMeasuredAt",
                "surfacetemperaturesAverageSurfaceTemperatureC",
                "surfacetemperaturesAverageSurfaceTemperatureC",
                "surfacetemperaturesAverageSurfaceTemperatureC"
        );

        // Get soil temperature data
        getTemperatureData(
                soilTempDataItems,
                binding.layoutSoilTemperature,
                ConstantsAndStaticData.PROTOCOL_SOIL_TEMP_DAILY,
                startTime,
                endTime,
                "soiltempdailiesMeasuredAt",
                "soiltempdailiesAverageTempC",
                "soiltempdailiesMinimumTempC",
                "soiltempdailiesMaximumTempC"
        );

        // Get sky conditions
        binding.textViewSkyConditions.setText(R.string.no_data_available);
        binding.textViewSkyConditions.setTextColor(Color.DKGRAY);
        AndroidNetworking.get(ConstantsAndStaticData.NASA_DATA_FIND_BY_ORG_BASE_URL)
                .addPathParameter("protocols", ConstantsAndStaticData.PROTOCOL_SKY_CONDITIONS)
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
                            JSONArray features = response.getJSONArray("features");

                            if (features.length() <= 0) {
                                binding.textViewSkyConditions.setText(R.string.no_data_available);
                                binding.textViewSkyConditions.setTextColor(Color.DKGRAY);
                                return;
                            }

                            JSONObject properties = features.getJSONObject(0)
                                    .getJSONObject("properties");

                            String cloudCover = properties.getString("skyconditionsCloudCover");

                            binding.textViewSkyConditions.setText(cloudCover);
                            binding.textViewSkyConditions.setTextColor(ContextCompat.getColor(MrWasteGraphActivity.this, R.color.colorPrimaryText));

                        } catch (Exception e) {
                            Log.e(ConstantsAndStaticData.LOG_TAG, "onResponse: ", e);

                            binding.textViewSkyConditions.setText(R.string.no_data_available);
                            binding.textViewSkyConditions.setTextColor(Color.DKGRAY);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(ConstantsAndStaticData.LOG_TAG,
                                "onError: " + anError.getErrorDetail(),
                                anError.getCause()
                        );

                        binding.textViewSkyConditions.setText(R.string.no_data_available);
                        binding.textViewSkyConditions.setTextColor(Color.DKGRAY);
                    }
                });
    }

    private void getTemperatureData(@NonNull ArrayList < TemperatureDataItem > tempDataItems,
                                    @NonNull LayoutGraphBinding layoutGraphBinding,
                                    @NonNull String protocol,
                                    @NonNull String startTime, @NonNull String endTime,
                                    @NonNull String measuredTimeKey,
                                    @NonNull String temperatureKey,
                                    @NonNull String minTemperatureKey,
                                    @NonNull String maxTemperatureKey)
    {
        tempDataItems.clear();
        layoutGraphBinding.setContentState(ContentState.LOADING);
        AndroidNetworking.get(ConstantsAndStaticData.NASA_DATA_FIND_BY_ORG_BASE_URL)
                .addPathParameter("protocols", protocol)
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
                                layoutGraphBinding.setContentState(ContentState.NO_DATA_AVAILABLE);
                                return;
                            }

                            for (int i = 0, len = features.length(); i < len; ++i) {
                                JSONObject properties = features.getJSONObject(i)
                                        .getJSONObject("properties");

                                Date measuredAt = simpleDateFormat.parse(properties.getString(measuredTimeKey));

                                if (measuredAt != null) {
                                    Calendar previousMeasuredCalendar;
                                    Calendar currentMeasuredCalendar = Calendar.getInstance();
                                    currentMeasuredCalendar.setTime(measuredAt);

                                    if (tempDataItems.isEmpty())
                                        previousMeasuredCalendar = null;
                                    else
                                        previousMeasuredCalendar = tempDataItems.get(tempDataItems.size() - 1).measuredDateCalendar;

                                    if ((previousMeasuredCalendar != null)
                                            && (previousMeasuredCalendar.get(Calendar.DAY_OF_YEAR)
                                            == currentMeasuredCalendar.get(Calendar.DAY_OF_YEAR))) {
                                        continue;
                                    }

                                    String temperature = properties.getString(temperatureKey);
                                    String minTemperature = properties.getString(minTemperatureKey);
                                    String maxTemperature = properties.getString(maxTemperatureKey);

                                    float currentTemp = Utils.getValidFloat(temperature, 0);

                                    tempDataItems.add(new TemperatureDataItem(
                                            i,
                                            currentTemp,
                                            Utils.getValidFloat(maxTemperature, currentTemp),
                                            Utils.getValidFloat(minTemperature, currentTemp),
                                            currentMeasuredCalendar
                                    ));

                                    xAxisLabels.add(new SimpleDateFormat("dd MM", Locale.ENGLISH).format(measuredAt));
                                }
                            }

                            layoutGraphBinding.setContentState(ContentState.DATA_AVAILABLE);
                            processTemperatureData(tempDataItems, xAxisLabels, layoutGraphBinding);

                        } catch (Exception e) {
                            Log.e(ConstantsAndStaticData.LOG_TAG, "onResponse: ", e);
                            layoutGraphBinding.setContentState(ContentState.NO_DATA_AVAILABLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(ConstantsAndStaticData.LOG_TAG,
                                "onError: " + anError.getErrorDetail(),
                                anError.getCause()
                        );

                        layoutGraphBinding.setContentState(ContentState.NO_DATA_AVAILABLE);
                    }
                });
    }

    private void processHumidityData(@NonNull ArrayList < String > xAxisLabels)
    {
        //Log.d(ConstantsAndStaticData.LOG_TAG, "processHumidityData: " + humidityDataItem.toString());

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

    private void processTemperatureData(@NonNull ArrayList < TemperatureDataItem > tempItems,
                                        @NonNull ArrayList < String > xAxisLabels,
                                        @NonNull LayoutGraphBinding layoutGraphBinding)
    {
        ArrayList < BarEntry > temperatureEntries = new ArrayList<>();
        // ArrayList < BarEntry > minTemperatureEntries = new ArrayList<>();
        // ArrayList < BarEntry > maxTemperatureEntries = new ArrayList<>();

        for (int i = 0, len = tempItems.size(); i < len; ++i) {
            TemperatureDataItem item = tempItems.get(i);
            temperatureEntries.add(new BarEntry(i, item.temperature));
            // minTemperatureEntries.add(new BarEntry(i, item.temperature));
            // maxTemperatureEntries.add(new BarEntry(i, item.temperature));
        }

        BarDataSet barDataSetTemperature = new BarDataSet(temperatureEntries, getString(R.string.average_temperature));
        // BarDataSet lineDataSetMinTemp = new BarDataSet(minTemperatureEntries, getString(R.string.min_temperature));
        // BarDataSet lineDataSetMaxTemp = new BarDataSet(maxTemperatureEntries, getString(R.string.max_temperature));

        BarData barDataTemperature = new BarData(barDataSetTemperature);
        layoutGraphBinding.chart.setData(barDataTemperature);
        layoutGraphBinding.chart.setFitBars(true);

        XAxis xAxis = layoutGraphBinding.chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xAxisLabels.get((int) value);
            }
        });

        layoutGraphBinding.chart.invalidate();
    }

}
