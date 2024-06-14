package com.holeCode.novamoda.common

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.holeCode.novamoda.R
import com.holeCode.novamoda.data.local.SharedPreferencesManager
import com.holeCode.novamoda.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var mNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController

        lang = getLang()
        setLocal(this, lang)
        mode = getMode()

    }
    private fun getLang(): String {
        val sharedPreferences =
            application.getSharedPreferences("MyShared", Context.MODE_PRIVATE)
        val lang = sharedPreferences.getString("lang", null)
        return if (lang == null) {
            saveLang(application, "en")
            "en"
        } else
            lang
    }

    private fun getMode(): String {
        val sharedPreferences =
            application.getSharedPreferences("MyShared", Context.MODE_PRIVATE)
        val mode = sharedPreferences.getString("mode", null)
        return if (mode == null) {
//            SharedPreferencesManager.getInstance(application.baseContext)
//                .saveMode(application,"light")

            saveMode(application, "light")
            "light"
        } else
            mode
    }
}

