package com.codemobiles.cmscb.network;

import com.codemobiles.cmscb.models.JsonPlace
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface2 {

    @GET("/posts")
    fun getJson():Call<List<JsonPlace>>

    companion object Factory {

        private val BASE_URL = "https://jsonplaceholder.typicode.com/"

        private var retrofit: Retrofit? = null



        fun getClient(): ApiInterface2 {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!.create(ApiInterface2::class.java)
        }
    }
    }


