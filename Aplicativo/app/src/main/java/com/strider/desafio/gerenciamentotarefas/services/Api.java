package com.strider.desafio.gerenciamentotarefas.services;

import com.strider.desafio.gerenciamentotarefas.model.Task;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    @POST("task/list")
    Call<List<Task>> getTasks(@Body HashMap<String, Object> status);

    @PUT("task/update")
    Call<Task> updateTask(@Body Task task);

    @Multipart
    @POST("task/upload/{id}")
    Call<Task> uploadImage(@Part MultipartBody.Part file, @Path("id") Integer id);

}
