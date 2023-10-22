package com.holeCode.novamoda.data

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse
    (
   @SerializedName("status") val status: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    val data: Any? = null,
)
