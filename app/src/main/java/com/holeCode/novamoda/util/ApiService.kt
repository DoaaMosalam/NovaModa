package com.holeCode.novamoda.util


import com.holeCode.novamoda.pojo.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

   //call request register response
  @POST("register")
  fun registerUser(@Body registerUser: User):Call<User>
}