package com.messtex.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messtex.data.models.*
import com.messtex.data.repositories.mainRepository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Error
import java.util.*

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val userData = MutableLiveData<UserData?>()
    val co2_data = MutableLiveData<CarbonData?>()
    var meterData = ArrayList<MeterData>()
    val utilization_code = MutableLiveData<UtilizationData?>()
    val meterValue = MutableLiveData<String?>()
    val faq = MutableLiveData<FaqModel?>()

    var readingSummary = utilization_code.value?.verificationCode?.let {
        PostModelRecord(
            it,
            meterData.toTypedArray(),
            userData.value!!.firstName,
            userData.value!!.secondName,
            userData.value!!.email,
            userData.value!!.phone,
            userData.value!!.street,
            userData.value!!.houseNumber,
            userData.value!!.postcode,
            userData.value!!.city,
            userData.value!!.floor,
            userData.value!!.sendCopy,
            userData.value!!.readingReason
        )
    }


    var heat_meter_step: Boolean = false
    var water_meter_step: Boolean = false
    var heat_allocator_step: Boolean = false
    var contact_step: Boolean = false
    var steps_finished: Boolean =
        heat_meter_step && water_meter_step && heat_allocator_step && contact_step


    var meterIndex: Int = 0
    var isCameraAllowed: Boolean = false


    suspend fun checkVerificationCode() {
        val response = repository.getUtilizationUnitData(utilization_code.value!!).body()!!
    }

    suspend fun sendReadings(): CarbonData? {
        return repository.takeMeterReadings(
            PostModelRecord(
                utilization_code.value!!.verificationCode,
                meterData.toTypedArray(),
                userData.value!!.firstName,
                userData.value!!.secondName,
                userData.value!!.email,
                userData.value!!.phone,
                userData.value!!.street,
                userData.value!!.houseNumber,
                userData.value!!.postcode,
                userData.value!!.city,
                userData.value!!.floor,
                userData.value!!.sendCopy,
                userData.value!!.readingReason
            )
        ).body()
    }

    suspend fun getFaq(): FaqModel? {
        return repository.getFAQs().body()
    }

    suspend fun getCO2_Level(): CarbonData {
        return CarbonData(123.4)
    }

    suspend fun sendContactForm(contactData: ContactFormData) {
        repository.takeContactForm(contactData)
    }

    fun fetchData(viewModel: ViewModelData) {
        userData.value = viewModel.userData
        co2_data.value = viewModel.co2_data
        if (viewModel.meterData == null) {
            meterData = ArrayList<MeterData>()
        } else {
            meterData = viewModel.meterData
        }
        utilization_code.value = viewModel.utilizationData
        meterValue.value = viewModel.meterValue
        faq.value = viewModel.faq

        heat_meter_step = viewModel.heat_meter_step
        water_meter_step = viewModel.water_meter_step
        heat_allocator_step = viewModel.heat_allocator_step
        contact_step = viewModel.contact_step
        steps_finished = viewModel.steps_finished

        meterIndex = viewModel.meterIndex
        isCameraAllowed = viewModel.isCameraAllowed
    }
}