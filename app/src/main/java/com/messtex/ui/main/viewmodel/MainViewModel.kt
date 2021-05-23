package com.messtex.ui.main.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.messtex.R
import com.messtex.data.models.MeterData
import com.messtex.data.models.PostModelRecord
import com.messtex.data.models.UserData
import com.messtex.data.repositories.mainRepository.MainRepository
import com.pixolus.meterreading.BuildConfig
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import com.pixolus.meterreading.MeterReadingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val userData = MutableLiveData<UserData>()
    val meterData = MutableLiveData<MeterData>()


    fun updateMeterValueData(meterValue: String?) {

        meterData.value = MeterData(
            meterData.value?.meterId,
            meterValue,
            meterData.value?.meterType
        )
    }

    fun postApi() {
        viewModelScope.launch(Dispatchers.IO)
        {
            Log.i("API REQUEST",repository.apiPost(
                PostModelRecord(
                    userData.value?.firstName,
                    userData.value?.secondName,
                    userData.value?.street,
                    userData.value?.houseNumber,
                    userData.value?.postcode,
                    userData.value?.city,
                    meterData.value?.meterId,
                    meterData.value?.meterValue,
                    meterData.value?.meterType
                )
            ).toString())
        }
    }

}