package com.holeCode.novamoda.repository

import com.holeCode.novamoda.data.UniqueEmailValidateResponse
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.util.APIConsumer
import com.holeCode.novamoda.util.RequestStatus
import com.holeCode.novamoda.util.SimplifiedMessage
import kotlinx.coroutines.flow.flow


class AuthRepository(private val consumer:APIConsumer) {
//    : Flow<RequestStatus<UniqueEmailValidateResponse>> =
    fun validateEmailAddress(body: ValidateEmailBody)=flow{
        emit(RequestStatus.Waiting)
        val response = consumer.validateEmailAddress(body)
        if (response.isSuccessful){
            emit((RequestStatus.Success(response.body()!!)))
        }else{
            emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
        }
    }
}