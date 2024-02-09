package com.holeCode.novamoda.di


import android.app.Application
import com.holeCode.novamoda.repository.AuthRepository
import com.holeCode.novamoda.util.APIConsumer
import com.holeCode.novamoda.util.Credential
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIService {
    @Provides
    @Singleton
    fun getService(): APIConsumer {
        val builder = Retrofit.Builder()
            .baseUrl(Credential.BASE_URL)
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        return retrofit.create(APIConsumer::class.java)

//        val loggingInterceptor = HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BODY)
//
//        // Client
//        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .build()
//        val builder = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//
//        val retrofit = builder.build()
//        return retrofit.create(APIConsumer::class.java)
    }
    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        // Client
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(16, TimeUnit.SECONDS)
            .writeTimeout(16, TimeUnit.SECONDS)
            .readTimeout(16, TimeUnit.SECONDS)
        client.addInterceptor(loggingInterceptor)
        return client.build()

    }
    @Provides
    @Singleton
    fun provideAuthRepository(apiConsumer: APIConsumer): AuthRepository {
        return AuthRepository(apiConsumer)
    }

}