package com.example.ef_naomi_yumbato;

import com.example.ef_naomi_yumbato.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("users")
    Call<List<Users>> getUsers();
}
