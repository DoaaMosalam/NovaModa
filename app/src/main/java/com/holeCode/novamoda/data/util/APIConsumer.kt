package com.holeCode.novamoda.data.util

import com.holeCode.novamoda.data.request.LoginResponse
import com.holeCode.novamoda.data.request.RegisterResponse
import com.holeCode.novamoda.data.request.UniqueEmailValidateResponse
import com.holeCode.novamoda.data.request.ValidateEmailBody
import com.holeCode.novamoda.domain.model.LoginBody
import com.holeCode.novamoda.domain.model.RegisterBody
import retrofit2.Response
import retrofit2.http.Body
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

}