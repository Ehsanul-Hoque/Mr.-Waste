package io.pantheonsite.alphaoptimus369.mrwaste.graph_module.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NasaDataApiInterface
{

    @GET("v1/measurement/protocol/measureddate/organizationname/")
    Call < ResponseBody > getDataFromNasa(@Query("protocols") String protocol,
                                          @Query("startdate") String startdate,
                                          @Query("enddate") String enddate,
                                          @Query("organizationname") String organizationname,
                                          @Query("geojson") String geojson,
                                          @Query("sample") String sample);

}
