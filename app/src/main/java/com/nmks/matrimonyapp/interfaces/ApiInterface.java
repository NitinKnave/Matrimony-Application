package com.nmks.matrimonyapp.interfaces;

import com.nmks.matrimonyapp.entities.RandomUserListData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    //https://randomuser.me/api/?results=10

    @GET("?results=10")
    Call<RandomUserListData> getUsersList();

    @GET("?")
    Call<RandomUserListData> getUsersDList(@Query("results") int no);





}
