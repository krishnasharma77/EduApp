package com.adts.app.network

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Logictrix on 22-Oct-21.
 */
object ApiClient {

    private val BASE_URL = "http://52.14.159.181:8086/api/"
//    private val BASE_URL = "http://18.223.109.114:8087/api/"
    var apiRestInterfaces: ApiInterface? = null

    val client: ApiInterface?
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.MINUTES)
                .connectTimeout(100, TimeUnit.MINUTES)
            if (BuildConfig.DEBUG) okHttpClient.addInterceptor(interceptor)
            if (apiRestInterfaces == null) {
                val client = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build()
                apiRestInterfaces =
                    client.create(ApiInterface::class.java)
            }
            return apiRestInterfaces
        }
}