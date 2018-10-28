package com.hour24.ygy.service;


import com.hour24.ygy.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("search/users")
    Call<UserModel> getUserList(
            @Query("q") String q,
            @Query("sort") String sort,
            @Query("order") String order
    );

}
