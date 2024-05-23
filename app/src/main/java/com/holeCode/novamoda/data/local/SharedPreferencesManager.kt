package com.holeCode.novamoda.data.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity


class SharedPreferencesManager(mCtx: Context) {

    companion object {
        private const val TOKEN = "TOKEN"
        private const val TOKEN_VALUE = "TOKEN_VALUE"
        private const val IS_USER_REGISTER_IN = "isUserRegisterIn"
        private var instance: SharedPreferencesManager? = null

        @Synchronized
        fun getInstance(context: Context): SharedPreferencesManager =
            instance ?: synchronized(this) {
                SharedPreferencesManager(context).apply {
                    instance = this
                }
            }
    }

    private var sharedPreferences: SharedPreferences =
        mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

    fun saveUser(
        application: Application,
        name: String?,
        email: String?,
        phone: String?,
        password: String?
    ) {
        val sharedPreferences = application.getSharedPreferences(TOKEN,Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit().apply {
            putString("name", name)
            putString("phone", phone)
            putString("email", email)
            putString("password", password)
        }
        editor.apply()
    }

    fun saveMode(application: Application, mode: String) {
        val sharedPreferences =
            application.getSharedPreferences(TOKEN, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("mode", mode)
        editor.apply()
    }

    fun getUser(
        application: Application,
        email: String?,
        password: String?
    ) {

        sharedPreferences.getString(email, null)
        sharedPreferences.getString(password, null)

    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun setUserIsRegistered(isRegister: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_USER_REGISTER_IN, isRegister)
        editor.apply()
    }

    fun isUserRegisterIn(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_REGISTER_IN, false)
    }

}

//================================================================================================

//    private val sharedPreferences: SharedPreferences =
//        mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
//    var token: String? = null
//        set(value) = sharedPreferences.edit().putString(TOKEN_VALUE, value).apply()
//            .also { field = value }
//        get() = field ?: sharedPreferences.getString(TOKEN_VALUE, null)
