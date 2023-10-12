package com.holeCode.novamoda.util

import com.holeCode.novamoda.data.LoginResponse
import com.holeCode.novamoda.data.RegisterResponse
import com.holeCode.novamoda.pojo.ResetPasswordBody
import com.holeCode.novamoda.data.ResetPasswordResponse
import com.holeCode.novamoda.data.UniqueEmailValidateResponse
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.pojo.LoginBody
import com.holeCode.novamoda.pojo.RegisterBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/*This class interface request API*/
interface APIConsumer {
    @POST("users/validate-unique-email")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBody): Response<UniqueEmailValidateResponse>

    //call request register response
    @POST("register")
    suspend fun registerUser(@Body registerUser: RegisterBody): Response<RegisterResponse>

    //call request login response
    @POST("login")
    suspend fun loginUser(@Body loginUser: LoginBody): Response<LoginResponse>

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Field("email") resetPasswordBody: ResetPasswordBody
    ): Response<ResetPasswordResponse>



    // call request reset-password
//    @POST("reset-password")
//    suspend fun resetPassword(@Body resetPassword: ResetPasswordBody): Response<ResetPasswordResponse>


}