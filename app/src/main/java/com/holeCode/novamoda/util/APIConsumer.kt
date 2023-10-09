package com.holeCode.novamoda.util

import com.holeCode.novamoda.data.LoginResponse
import com.holeCode.novamoda.data.RegisterResponse
import com.holeCode.novamoda.data.ResetPasswordBody
import com.holeCode.novamoda.data.ResetPasswordResponse
import com.holeCode.novamoda.data.UniqueEmailValidateResponse
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.pojo.LoginBody
import com.holeCode.novamoda.pojo.RegisterBody
import retrofit2.Call
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
    ): Call<ResetPasswordResponse>



    // call request reset-password
//    @POST("reset-password")
//    suspend fun resetPassword(@Body resetPassword: ResetPasswordBody): Response<ResetPasswordResponse>

//    @FormUrlEncoded
//    @POST("reset-password")
//    fun sendVerificationCode(
//        @Field("email") email: String
//    ): Response<ResetPasswordBody>
//
//    @FormUrlEncoded
//    @POST("reset-password")
//    fun newPassword(
//        @Field("verification_code") verificationCode: String,
//        @Field("new_password") newPassword: String
//    ): Response<ResetPasswordBody>


}