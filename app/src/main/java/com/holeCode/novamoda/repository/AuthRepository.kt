package com.holeCode.novamoda.repository

import android.util.Log
import com.holeCode.novamoda.data.ResetPasswordBody
import com.holeCode.novamoda.data.ResetPasswordResponse
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.pojo.LoginBody
import com.holeCode.novamoda.pojo.RegisterBody
import com.holeCode.novamoda.util.APIConsumer
import com.holeCode.novamoda.util.RequestStatus
import com.holeCode.novamoda.util.SimplifiedMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepository(private val consumer: APIConsumer){
    fun validateEmailAddress(body: ValidateEmailBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateEmailAddress(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

    fun registerUser(body: RegisterBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.registerUser(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

    fun loginUser(body: LoginBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.loginUser(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }
//    fun resetPassword(body: ResetPasswordBody) = flow {
//        emit(RequestStatus.Waiting)
//        val response = consumer.resetPassword(body)
//        if (response.isSuccessful) {
//            emit((RequestStatus.Success(response.body()!!)))
//        } else {
//            emit(
//                RequestStatus.Error(
//                    SimplifiedMessage.get(
//                        response.errorBody()!!.byteStream().reader().readText()
//                    )
//                )
//            )
//        }
//    }





    fun resetPassword(body:ResetPasswordBody) {
        GlobalScope.launch (Dispatchers.IO){
            val response = consumer.resetPassword(body)
                response.enqueue(object : Callback<ResetPasswordResponse> {
                    override fun onResponse(
                        call: Call<ResetPasswordResponse>,
                        response: Response<ResetPasswordResponse>
                    ) {
                        if (response.isSuccessful){
                            RequestStatus.Success(response.body()!!)
                        }
                         else {
                                RequestStatus.Error(
                                    SimplifiedMessage.get(
                                        response.errorBody()!!.byteStream().reader().readText()
                                    )
                                )
                        }

                    }

                    override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                        Log.d("TAG", "onFailure: ")
                    }

                })
        }
    }

}