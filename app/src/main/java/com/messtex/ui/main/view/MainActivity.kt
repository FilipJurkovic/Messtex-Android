package com.messtex.ui.main.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.messtex.R
import com.messtex.data.models.CarbonData
import com.messtex.data.models.FaqModel
import com.messtex.data.models.MeterReadingData
import com.messtex.data.models.ViewModelData
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_manual_input.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        bottomNavigationView.setupWithNavController(findNavController(R.id.fragmentContainerView))
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false


        val repository = MainRepository()
        val factory = ViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        viewModel.language_code = getLocale(this.applicationContext) ?: "en"
        viewModel.faq.postValue(intent.getSerializableExtra("FAQ") as FaqModel?)


        GlobalScope.launch(Dispatchers.IO) {
            try {
                viewModel.faq.postValue(viewModel.getFaq())
                Log.d("FAQ", viewModel.faq.value.toString())
                viewModel.co2_data.postValue(CarbonData(viewModel.getCO2_Level() ?: 0.0))
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }

        val updateInterval: Long = 5000
        val handler = Handler()
        val repeatingCode = Runnable {
            kotlin.run {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        viewModel.co2_data.postValue(CarbonData(viewModel.getCO2_Level() ?: 0.0))
                    } catch (e: Exception) {
                        Log.d("Error", e.toString())
                    }
                }
            }
        }
        handler.postDelayed(repeatingCode, updateInterval)

        if (!checkPermission(this.applicationContext, Manifest.permission.CAMERA)) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
        }
        viewModel.isCameraAllowed =
            checkPermission(this.applicationContext, Manifest.permission.CAMERA)

        val navController = findNavController(R.id.fragmentContainerView)
        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)

        floatingScanButton.setOnClickListener {
            navController.navigate(R.id.codeReadingFragment, null, navBuilder.build())
        }



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.home2 || destination.id == R.id.moreFragment2) {
                bottomAppBar.visibility = View.VISIBLE
                floatingScanButton.visibility = View.VISIBLE

            } else {
                bottomAppBar.visibility = View.GONE
                floatingScanButton.visibility = View.GONE
            }
        }

        if (intent.getBooleanExtra("meterScanningExited", false)) {
            viewModel.fetchData(intent.getSerializableExtra("ViewModel") as ViewModelData)
            Log.d("Transformed reading", viewModel.meterValue.value.toString())
            navController.navigate(R.id.readingStepsFragment, null, navBuilder.build())
        }

        if (intent.getStringExtra("counterValue") != null) {
            viewModel.fetchData(intent.getSerializableExtra("ViewModel") as ViewModelData)

            viewModel.meterValue.value = intent.getStringExtra("counterValue")?.replace(",", ".")
            viewModel.meterData[viewModel.meterIndex] = MeterReadingData(
                viewModel.userData.value!!.meters?.get(viewModel.meterIndex)!!.counterNumber,
                viewModel.userData.value!!.meters?.get(viewModel.meterIndex)!!.counterType,
                viewModel.meterValue.value!!.toDouble(),
                intent.getStringExtra("rawReadingString")!!,
                intent.getStringExtra("cleanReadingString")!!,
                intent.getStringExtra("readingResultStatus")!!,
                ""
                )
            Log.d("Transformed reading", viewModel.meterValue.value.toString())
            navController.navigate(R.id.manualInputFragment, null, navBuilder.build())
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun checkPermission(context: Context, permissionArray: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permissionArray
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocale(context: Context?): String? {
        val sharedPref = context?.getSharedPreferences("locale", Context.MODE_PRIVATE)
        return sharedPref?.getString("language", "de")
    }

    private fun updateBaseContextLocale(context: Context?): Context? {
        val language: String = getLocale(context) ?: "de"
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)
    }

    private fun updateResourcesLocale(context: Context?, locale: Locale): Context? {
        val configuration: Configuration = Configuration(context?.resources?.configuration)
        configuration.setLocale(locale)
        return context?.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context?, locale: Locale): Context? {
        val resources = context?.resources
        val configuration = resources?.configuration
        configuration?.locale = locale
        resources?.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

}