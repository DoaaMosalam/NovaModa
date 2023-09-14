package com.holeCode.novamoda.data

import com.google.gson.annotations.SerializedName
import com.holeCode.novamoda.pojo.User
import java.util.Objects

data class RegisterResponse(
    val status: Boolean?= null,
    val message: String? = null,
    val data: Any? =null

//    @field:SerializedName("data")
//    val data: Any,
//    @field:SerializedName("message")
//    val message: String,
//
//    @field:SerializedName("status")
//    val status: Boolean
)
