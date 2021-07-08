package com.messtex.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messtex.data.models.*
import com.messtex.data.repositories.mainRepository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val userData = MutableLiveData<UserData>()
    val co2_data = MutableLiveData<CarbonData>()
    var meterData = ArrayList<MeterData>()
    val utilization_code = MutableLiveData<UtilizationData>()
    val meterValue = MutableLiveData<Double>()
    val faq = MutableLiveData<FaqModel>()

    private var readingSummary= utilization_code.value?.verificationCode?.let {
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


    var heat_meter_step : Boolean = false
    var water_meter_step : Boolean = false
    var heat_allocator_step : Boolean = false
    var contact_step : Boolean = false
    val steps_finished : Boolean = heat_meter_step && water_meter_step && heat_allocator_step && contact_step


    var meterIndex : Int = 0
    var isCameraAllowed : Boolean = false

    suspend fun checkVerificationCode(): UserData{
        return repository.getUtilizationUnitData(utilization_code.value!!).body()!!
    }

    suspend fun sendReadings(): CarbonData{
        return readingSummary?.let { repository.takeMeterReadings(it).body() }!!
    }

    suspend fun getFaq(){
        faq.value = repository.getFAQs().body()!!
    }

    suspend fun getCO2_Level(): CarbonData{
        return CarbonData(123.4)
    }

    suspend fun sendContactForm(contactData: ContactFormData){
        repository.takeContactForm(contactData)
    }
}