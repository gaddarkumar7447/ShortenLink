package com.example.myapplication.api

import com.example.myapplication.model.ShortenLink
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("api.php")
    suspend fun getResponce(@Query("key") key : String, @Query("short") link : String) : Response<ShortenLink>

}