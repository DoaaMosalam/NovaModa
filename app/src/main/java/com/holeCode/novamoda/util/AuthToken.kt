package com.holeCode.novamoda.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.holeCode.novamoda.pojo.User

class AuthToken private constructor(mCtx: Context) {
    companion object {
        private const val TOKEN = "TOKEN"
        private const val TOKEN_VALUE = "TOKEN_VALUE"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AuthToken? = null
        fun getInstance(context: Context): AuthToken =
            instance ?: synchronized(this) { AuthToken(context).apply { instance = this } }

    }

    private val sharedPreferences: SharedPreferences =
        mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
    var token: String? = null
        set(value) = sharedPreferences.edit().putString(TOKEN_VALUE, value).apply()
            .also { field = value }
        get() = field ?: sharedPreferences.getString(TOKEN_VALUE, null)
}