package com.ddscanner.booking.rest;



import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RestClient {

    private static DDScannerRestService ddscannerServiceInstance;
    private static GoogleMapsApiRestService googleMapsApiServiceInstance;

    public static DDScannerRestService getDdscannerServiceInstance() {
        if (ddscannerServiceInstance == null) {
            Interceptor interceptor = chain -> {

                Request request = chain.request();
                request = request.newBuilder()
                        .build();
                Response response = chain.proceed(request);
                return response;
            };

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.interceptors().add(interceptor);
            builder.retryOnConnectionFailure(true);
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(1, TimeUnit.MINUTES);
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.interceptors().add(logging);

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DDScannerBookingApplication.getInstance().getString(R.string.server_api_address))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            ddscannerServiceInstance = retrofit.create(DDScannerRestService.class);
        }
        return ddscannerServiceInstance;
    }


}
