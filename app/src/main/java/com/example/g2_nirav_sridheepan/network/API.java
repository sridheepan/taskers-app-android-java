package com.example.g2_nirav_sridheepan.network;

import com.example.g2_nirav_sridheepan.models.TaskerContainer;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    //base URL must end with /
    //String BASE_URL = "https://62898d025da6ddfd5d593f1e.mockapi.io/get-taskers/";
    String BASE_URL = "https://mocki.io/v1/";


    //HTTP request - Get
    @GET("./9589064a-24f6-45b9-ad2e-f1e41b4b0b4f")
    Call<TaskerContainer> getTasker();
}
