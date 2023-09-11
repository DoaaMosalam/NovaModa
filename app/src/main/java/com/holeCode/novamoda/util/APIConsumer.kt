package com.holeCode.novamoda.util

import com.holeCode.novamoda.data.UniqueEmailValidateResponse
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.pojo.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
/*This class interface request API*/
interface APIConsumer {
    //call request register response
//    @POST("register")
//    fun registerUser(@Body registerUser: User): Call<User>
    @POST("validate-unique-email")
    suspend fun validateEmailAddress(@Body body:ValidateEmailBody):Response<UniqueEmailValidateResponse>

}