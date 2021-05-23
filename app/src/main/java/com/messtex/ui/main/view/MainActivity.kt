package com.messtex.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.messtex.R
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.utils.ViewModelFactory


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = MainRepository()
        val factory = ViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }
}