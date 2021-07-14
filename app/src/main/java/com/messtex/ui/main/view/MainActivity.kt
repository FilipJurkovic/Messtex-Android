package com.messtex.ui.main.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.messtex.R
import com.messtex.data.models.ViewModelData
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contact_form.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

        GlobalScope.launch(Dispatchers.IO) {
                try {
                    viewModel.faq.postValue(viewModel.getFaq())
                    Log.d("response", viewModel.faq.value!!.faqs[0].answer)
                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                }
            }

        if (!checkPermission(this.applicationContext, Manifest.permission.CAMERA)){
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
        }
        viewModel.isCameraAllowed = checkPermission(this.applicationContext, Manifest.permission.CAMERA)

        val navController = findNavController(R.id.fragmentContainerView)
        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(R.anim.nav_default_enter_anim).setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim).setPopExitAnim(R.anim.nav_default_pop_exit_anim)

        floatingScanButton.setOnClickListener {
            navController.navigate(R.id.codeReadingFragment, null, navBuilder.build())
        }



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.home2 || destination.id == R.id.moreFragment2) {
                bottomAppBar.visibility = View.VISIBLE
                floatingScanButton.visibility = View.VISIBLE

            } else {
                bottomAppBar.visibility = View.GONE
                floatingScanButton.visibility = View.GONE
            }
        }


        if (intent.getStringExtra("counterValue") != null){
            viewModel.fetchData(intent.getSerializableExtra("ViewModel") as ViewModelData)

            viewModel.meterValue.value = intent.getStringExtra("counterValue")?.replace(",", ".")
            Log.d("Transformed reading", viewModel.meterValue.value.toString())
            navController.navigate(R.id.manualInputFragment, null, navBuilder.build())
        }



//        lifecycleScope.launch {
//            viewModel.getFaq()
//            viewModel.getCO2_Level()
//        }

//        val updateInterval: Long = 20000
//        val handler = Handler()
//        val repeatingCode = Runnable {
//            kotlin.run {
//                lifecycleScope.launch {
//            viewModel.getCO2_Level()
//        }
//
//                }
//        }
//        handler.postDelayed(repeatingCode, updateInterval);


    }

    fun checkPermission(context: Context, permissionArray: String): Boolean{
        return ContextCompat.checkSelfPermission(context, permissionArray) == PackageManager.PERMISSION_GRANTED
    }


}