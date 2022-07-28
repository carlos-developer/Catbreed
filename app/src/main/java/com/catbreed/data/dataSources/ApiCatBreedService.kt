package com.catbreed.data.dataSources

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface ApiCatBreedService {

    @Headers("x-api-key: bda53789-d59e-46cd-9bc4-2936630fde39")
    @GET
    fun fetchAllCatsBreed(@Url url: String): Call<ResponseBody?>
}