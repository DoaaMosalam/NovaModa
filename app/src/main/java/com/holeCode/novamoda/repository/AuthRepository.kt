package com.holeCode.novamoda.repository

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.holeCode.novamoda.data.ResetPasswordResponse
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.pojo.LoginBody
import com.holeCode.novamoda.pojo.NewPasswordBody
import com.holeCode.novamoda.pojo.RegisterBody
import com.holeCode.novamoda.pojo.ResetPasswordBody
import com.holeCode.novamoda.util.APIConsumer
import com.holeCode.novamoda.util.RequestStatus
import com.holeCode.novamoda.util.SimplifiedMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepository(private val consumer: APIConsumer):AppCompatActivity() {
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

}