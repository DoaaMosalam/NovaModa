package com.holeCode.novamoda.storage

import android.content.Context
import android.content.SharedPreferences
import com.holeCode.novamoda.pojo.RegisterBody

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

    fun saveUser(user: RegisterBody) {
//        sharedPreferences =  mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("image", user.image)
        editor.putString("name", user.name)
        editor.putString("phone", user.phone)
        editor.putString("email", user.email)
        editor.putString("password", user.password)
        editor.apply()
    }


    fun loadUser() {
//        val sharedPreferences= mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        sharedPreferences.getString("image", null)
        sharedPreferences.getString("name", null)
        sharedPreferences.getString("phone", null)
        sharedPreferences.getString("email", null)
        sharedPreferences.getString("password", null)
    }

    fun clear() {
//        val sharedPreferences = mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun setUserIsRegistered(isRegister: Boolean) {
//        val sharedPreferences = mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_USER_REGISTER_IN, isRegister)
        editor.apply()

    }

    fun isUserRegisterIn(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_REGISTER_IN, false)
    }

    //================================================================================================

//    private val sharedPreferences: SharedPreferences =
//        mCtx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
//    var token: String? = null
//        set(value) = sharedPreferences.edit().putString(TOKEN_VALUE, value).apply()
//            .also { field = value }
//        get() = field ?: sharedPreferences.getString(TOKEN_VALUE, null)
//    }
}