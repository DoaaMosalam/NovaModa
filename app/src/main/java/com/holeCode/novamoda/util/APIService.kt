package com.holeCode.novamoda.util


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIService {
    private const val BASE_URL = "https://student.valuxapps.com/api/"
    fun getService(): APIConsumer {
        // API response interceptor
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        // Client
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        return retrofit.create(APIConsumer::class.java)
    }
}