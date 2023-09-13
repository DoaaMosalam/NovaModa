package com.holeCode.novamoda.util

import com.holeCode.novamoda.data.UniqueEmailValidateResponse
import com.holeCode.novamoda.data.ValidateEmailBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/*This class interface request API*/
interface APIConsumer {
//    @POST("verify-email")
        @POST("users/validate-unique-email")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBody): Response<UniqueEmailValidateResponse>

    //call request register response
//    @POST("register")
//    fun registerUser(@Body registerUser: User): Call<User>

}