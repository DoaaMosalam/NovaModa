package com.holeCode.novamoda.common

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

var lang = "en"
var mode = "dark"
lateinit var authorization: String
val nf: NumberFormat = NumberFormat.getInstance(Locale("ar", "EG")) //or "nb","No" - for Norway

fun setLocal(activity: Activity, lang: String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val resources = activity.resources
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)

}

fun saveLang(application: Application, lang: String) {
    val sharedPreferences =
        application.getSharedPreferences("MyShared", AppCompatActivity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("lang", lang)
    editor.apply()
}

fun saveMode(application: Application, mode: String) {
    val sharedPreferences =
        application.getSharedPreferences("MyShared", AppCompatActivity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("mode", mode)
    editor.apply()
}

fun saveUser(
    application: Application,
    name: String?,
    email: String?,
    phone: String?,
    token: String?
) {
    // To store values in shared preferences:
    val sharedPreferences =
        application.getSharedPreferences("MyShared", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("name", name)
    editor.putString("email", email)
    editor.putString("phone", phone)
    editor.putString("token", token)
    editor.apply()
}