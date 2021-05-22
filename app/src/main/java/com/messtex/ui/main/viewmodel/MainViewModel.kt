package com.messtex.ui.main.viewmodel

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messtex.data.models.MeterData
import com.messtex.data.models.localdb.User
import com.messtex.data.repositories.mainRepository.MainRepository
import com.pixolus.meterreading.BuildConfig
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import com.pixolus.meterreading.MeterReadingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val userData = MutableLiveData<User>()
    val meterData = MutableLiveData<MeterData>()

    fun onSubmitted(user: User) {
        viewModelScope.launch(Dispatchers.IO)
        {
            if (true) {
                repository.appendUser(user)
            } else {

                repository.updateUser(user)
            }
            repository.editPostModelUserData(userData.value!!)
        }

    }


    fun onPixolousClicked(view: View) {
        val intent: Intent = Intent(view.context, MeterReadingActivity::class.java)
            .putExtra(MeterReadingActivity.EXTRA_INTEGER_DIGITS, MeterReadingFragment.AUTOMATIC)
            .putExtra(MeterReadingActivity.EXTRA_FRACTION_DIGITS, MeterReadingFragment.AUTOMATIC)
            .putExtra(MeterReadingActivity.EXTRA_NUMBER_OF_COUNTERS, MeterReadingFragment.AUTOMATIC)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_UNREADABLE_COUNTER, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_AFTER_LAST_DETECTION, 0)
            .putExtra(MeterReadingActivity.EXTRA_TIMEOUT, 0)
            .putExtra(MeterReadingActivity.EXTRA_ALLOWS_ROTATION, true)
            .putExtra(MeterReadingActivity.EXTRA_ZOOM, 1.3)
            .putExtra(MeterReadingActivity.EXTRA_IS_RESULTS_OVERLAY_VISIBLE, true)
            .putExtra(MeterReadingActivity.EXTRA_IS_DEBUG_OVERLAY_VISIBLE, BuildConfig.DEBUG)
        meterData.value = MeterData(
            meterData.value?.meterId,
            onPixolousResult(0, intent), //TODO: add real response code
            meterData.value?.meterType
        )
    }

    private fun onPixolousResult(resultCode: Int, intent: Intent): String? {
        if (resultCode == Activity.RESULT_OK) {

            // Get the reading results which are returned via the intent's extras.
            val readingResults: ArrayList<MeterReadingResult>? =
                intent.getParcelableArrayListExtra(MeterReadingActivity.RESULT_EXTRA_READINGS)
            // They are only available if the resultCode is RESULT_OK, so we check for null here.
            // They are only available if the resultCode is RESULT_OK, so we check for null here.
            if (readingResults != null) {
                return readingResults[0].cleanReadingString()
            }
        }

        return null
    }

    fun postApi() {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.editPostModelMeterData(meterData.value!!)
            repository.apiPost()
        }
    }

}