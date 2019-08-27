package com.codemobiles.mobilephone.network;

import com.codemobiles.cmscb.models.YoutubeResponse
import com.codemobiles.mobilephone.models.MobileBean
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("api/mobiles/")
    fun getMobileDetail(): Call<List<MobileBean>>



    companion object Factory {
        private val BASE_URL = "http://scb-test-mobile.herokuapp.com/"

        private var retrofit: Retrofit? = null



        fun getClient(): ApiInterface {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!.create(ApiInterface::class.java)
        }
    }
    }


