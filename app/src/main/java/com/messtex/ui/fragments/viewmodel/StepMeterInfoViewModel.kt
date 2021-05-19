package com.messtex.ui.fragments.viewmodel

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messtex.data.models.MeterData
import com.messtex.data.models.PostModel
import com.messtex.data.repositories.mainRepository.MainRepository
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import com.pixolus.meterreading.MeterReadingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StepMeterInfoViewModel(private val repository: MainRepository) : ViewModel() {
    val meterData = MutableLiveData<MeterData>()

    fun onPixolousClicked(view: View){
        val intent = Intent(view.context, MeterReadingActivity::class.java)
            .putExtra(MeterReadingActivity.EXTRA_INTEGER_DIGITS, MeterReadingFragment.AUTOMATIC)
            .putExtra(MeterReadingActivity.EXTRA_FRACTION_DIGITS, MeterReadingFragment.AUTOMATIC)
            .putExtra(MeterReadingActivity.EXTRA_NUMBER_OF_COUNTERS, MeterReadingFragment.AUTOMATIC)
        onPixlousResult()
    }

    fun onPixolousResult(requestCode: Int, resultCode: Int, intent: Intent){
        if(resultCode == Activity.RESULT_OK){
            intent.getParcelableArrayListExtra<MeterReadingResult>(MeterReadingActivity.RESULT_EXTRA_READINGS).
        }
    }

    fun postApi(){
        viewModelScope.launch(Dispatchers.IO)
        {
            val postModel: PostModel = PostModel()
            repository.apiPost(postModel)
        }
    }
}