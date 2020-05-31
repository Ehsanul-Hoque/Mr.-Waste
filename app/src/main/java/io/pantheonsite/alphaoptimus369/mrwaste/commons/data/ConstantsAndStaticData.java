package io.pantheonsite.alphaoptimus369.mrwaste.commons.data;

import io.pantheonsite.alphaoptimus369.mrwaste.commons.models.UserItem;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.WasteItem;


public class ConstantsAndStaticData
{

    public static UserItem currentUser = null;
    public static WasteItem selectedWasteItem = null;

    public static final String NASA_DATA_FIND_BY_ORG_BASE_URL = "https://api.globe.gov/search/";
    public static final String FIREBASE_REST_API_BASE_URL = "https://fcm.googleapis.com/";

    public static final String PROTOCOL_AIR_TEMP_DAILY = "air_temp_dailies";
    public static final String PROTOCOL_HUMIDITY = "humidities";
    public static final String PROTOCOL_SKY_CONDITIONS = "sky_conditions";
    public static final String PROTOCOL_SOIL_TEMP_DAILY = "soil_temp_dailies";
    public static final String PROTOCOL_SURFACE_TEMP = "surface_temperatures";
    public static final String PROTOCOL_WATER_TEMP = "water_temperatures";

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 502;
    public static final int REQUEST_CODE_GPS = 485;
    public static final int REQUEST_CODE_CAMERA = 651;

    public static final String EXTRA_PREV_ACTIVITY_FINISHED = "PREV_ACTIVITY_FINISHED";
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_CONTACT_NO = "CONTACT_NO";
    public static final String EXTRA_USER_TYPE = "USER_TYPE";
    public static final String EXTRA_PASSWORD = "PASSWORD";

    public static final String LOG_TAG = "MY_LOGGER";

}
