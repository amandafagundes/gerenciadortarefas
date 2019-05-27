package com.strider.desafio.gerenciamentotarefas.Util;

import com.strider.desafio.gerenciamentotarefas.services.Api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitSettings {

    private final Retrofit retrofit;

    public RetrofitSettings() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.5:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public Api getApi() {
        return this.retrofit.create(Api.class);
    }
}
