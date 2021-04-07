package com.example.comisariaapp.api.reniec;

import com.example.comisariaapp.repository.ReniecBuscarPersonaRepository;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReniecConfigApi {
    public static String baseUrl = "https://apiperu.dev";
    private static Retrofit retrofit;
    private static String token = "de4dae4a0b74ac93cccea3d26885529e03da8572cf4f337c5ef2f35ac4f4d62f";
    private static BuscarPersonaApi api;

    static {
        initClient();
    }

    private static void initClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
    }

    public static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        StethoInterceptor stetho = new StethoInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        Interceptor authInterceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                return chain.proceed(request);
            }
        };


        builder.addInterceptor(logging)
                .addInterceptor(authInterceptor)
                .addNetworkInterceptor(stetho);

        return builder.build();
    }

    public static BuscarPersonaApi getBuscarPersonaApi() {
        if (api == null) {
            api = retrofit.create(BuscarPersonaApi.class);
        }
        return api;
    }
}
