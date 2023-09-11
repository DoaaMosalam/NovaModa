package com.holeCode.novamoda.util


import com.holeCode.novamoda.pojo.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object APIService {
    const val BASE_URL= "https://student.valuxapps.com/api/"
    fun getService():APIConsumer{
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .build()
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())

            val retrofit = builder.build()
        return retrofit.create(APIConsumer::class.java)


    }
//     fun registerService(name: String, phone: String, email: String, password: String, image: String):Call<User> {
//        val client = OkHttpClient.Builder()
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60,TimeUnit.SECONDS)
//            .readTimeout(60,TimeUnit.SECONDS)
//            .build()
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val apiService = retrofit.create(APIConsumer::class.java)
//        val request = User(name, phone, email, password, image)
//        return apiService.registerUser(request)
//    }
}