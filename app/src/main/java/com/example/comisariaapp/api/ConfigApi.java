package com.example.comisariaapp.api;

import com.example.comisariaapp.utils.DateDeserializer;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigApi {
    private static final String baseUrlE = "http://10.0.2.2:9090", baseUrlD = "http://192.168.1.103:9090";
    private static Retrofit retrofit;
    private static String token = "";

    private static UsuarioApi usuarioApi;
    private static DistritoApi distritoApi;
    private static VinculoParteDenunciadaApi vpdApi;
    private static TipoDenunciaApi tdApi;
    private static InformacionAdicionalApi infoAdicApi;
    private static TipoTramiteApi tTApi;
    private static TramiteApi tApi;
    private static EstadoCivilApi ecApi;
    private static DenunciaApi dApi;

    static {
        initClient();
    }

    private static void initClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlD)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
    }

    public static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        StethoInterceptor stetho = new StethoInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //Interceptor authInterceptor = chain -> chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build());


        builder.addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                //.addInterceptor(authInterceptor)
                .addNetworkInterceptor(stetho);

        return builder.build();
    }

    public static void setToken(String value) {
        token = value;
        initClient();
    }

    public static UsuarioApi getUsuarioApi() {
        if (usuarioApi == null) {
            usuarioApi = retrofit.create(UsuarioApi.class);
        }
        return usuarioApi;
    }

    public static DistritoApi getDistritoApi() {
        if (distritoApi == null) {
            distritoApi = retrofit.create(DistritoApi.class);
        }
        return distritoApi;
    }

    public static VinculoParteDenunciadaApi getVpdApi() {
        if (vpdApi == null) {
            vpdApi = retrofit.create(VinculoParteDenunciadaApi.class);
        }
        return vpdApi;
    }

    public static TipoDenunciaApi getTipoDenunciaApi() {
        if (tdApi == null) {
            tdApi = retrofit.create(TipoDenunciaApi.class);
        }
        return tdApi;
    }

    public static InformacionAdicionalApi getInformacionAdicionalApi() {
        if (infoAdicApi == null) {
            infoAdicApi = retrofit.create(InformacionAdicionalApi.class);
        }
        return infoAdicApi;
    }

    public static TipoTramiteApi getTTApi() {
        if (tTApi == null) {
            tTApi = retrofit.create(TipoTramiteApi.class);
        }
        return tTApi;
    }

    public static TramiteApi getTramiteApi() {
        if (tApi == null) {
            tApi = retrofit.create(TramiteApi.class);
        }
        return tApi;
    }

    public static EstadoCivilApi getEstadoCivilApi() {
        if (ecApi == null) {
            ecApi = retrofit.create(EstadoCivilApi.class);
        }
        return ecApi;
    }

    public static DenunciaApi getDenunciaApi() {
        if (dApi == null) {
            dApi = retrofit.create(DenunciaApi.class);
        }
        return dApi;
    }
}

