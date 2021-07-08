package com.messtex.ui.main.view

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.messtex.R
import com.messtex.data.models.ContactFormData
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contact_form.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        bottomNavigationView.setupWithNavController(findNavController(R.id.fragmentContainerView))
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

        val navController = findNavController(R.id.fragmentContainerView)

        floatingScanButton.setOnClickListener {
            navController.navigate(R.id.codeReadingFragment)
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


        val repository = MainRepository()
        val factory = ViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        viewModel.meterValue.value = intent.getStringExtra("counterValue")?.toDouble()

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



}