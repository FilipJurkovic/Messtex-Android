package com.messtex.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.messtex.R
import com.messtex.data.models.localdb.UserDatabase
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.fragments.view.StepMeterInfo
import com.messtex.ui.fragments.view.StepUserInfo
import com.messtex.ui.fragments.viewmodel.StepUserInfoViewModel
import com.messtex.ui.main.adapter.MainActivityAdapter
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: ViewModelFactory by instance()

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

    }
}