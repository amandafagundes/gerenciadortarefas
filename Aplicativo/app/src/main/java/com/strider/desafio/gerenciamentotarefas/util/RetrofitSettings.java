package com.strider.desafio.gerenciamentotarefas.util;

import com.strider.desafio.gerenciamentotarefas.services.Api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitSettings {

    private static Retrofit retrofit;
    public static String API_BASE_URL = "";

    public RetrofitSettings() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public static void changeApiBaseUrl(String newAdress){
        API_BASE_URL = newAdress;
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public Api getApi() {
        return this.retrofit.create(Api.class);
    }
}
