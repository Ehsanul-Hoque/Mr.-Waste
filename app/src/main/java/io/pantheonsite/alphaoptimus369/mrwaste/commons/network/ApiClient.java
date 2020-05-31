package io.pantheonsite.alphaoptimus369.mrwaste.commons.network;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.ConstantsAndStaticData;
import io.pantheonsite.alphaoptimus369.mrwaste.graph_module.interfaces.NasaDataApiInterface;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient
{

    private static final String CACHE_CONTROL = "Cache-Control";
    private static final int DISK_CACHE_SIZE = 10 * 1024 * 1024;    // 10MB
    private static Retrofit retrofit = null;


    @NonNull
    private static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(5, TimeUnit.MINUTES);
            clientBuilder.readTimeout(5, TimeUnit.MINUTES);
            clientBuilder.writeTimeout(5, TimeUnit.MINUTES);
            clientBuilder.addNetworkInterceptor(provideCacheInterceptor());

            retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsAndStaticData.NASA_DATA_FIND_BY_ORG_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();
        }

        return retrofit;
    }

    @NonNull
    public static NasaDataApiInterface getApiInterface() {
        return getRetrofitClient().create(NasaDataApiInterface.class);
    }

    @NonNull
    public static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(2, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

}
