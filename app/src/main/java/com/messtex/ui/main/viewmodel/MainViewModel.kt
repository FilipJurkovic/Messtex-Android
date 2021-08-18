package com.messtex.ui.main.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.messtex.R
import com.messtex.data.models.*
import com.messtex.data.repositories.mainRepository.MainRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val userData = MutableLiveData<UserData?>()
    val co2_data = MutableLiveData<CarbonData?>()
    var meterData = ArrayList<MeterReadingData>()
    val utilization_code = MutableLiveData<UtilizationData?>()
    val meterValue = MutableLiveData<String?>()
    val faq = MutableLiveData<FaqModel?>()



    var readingStepsProgress: Array<Boolean> = arrayOf()

    var meterIndex: Int = 0
    var isCameraAllowed: Boolean = false
    var sendCopy: Boolean = false
    var isAppActive: Boolean = false
    var language_code: String = ""
    var scanningSuccessful: Boolean = false


    suspend fun checkVerificationCode() {
        val response = repository.getUtilizationUnitData(utilization_code.value!!).body()!!
        MainScope().launch {
            userData.value = response
            for (i in userData.value!!.meters.indices){
                readingStepsProgress += false
            }
            readingStepsProgress += false
        }

    }

    suspend fun sendReadings(){
        val response = repository.takeMeterReadings(
            PostModelRecord(
                utilization_code.value!!.verificationCode,
                language_code,
                meterData.toTypedArray(),
                userData.value!!.firstName,
                userData.value!!.lastName,
                userData.value!!.email,
                userData.value!!.phone,
                sendCopy
            )
        ).body()

        MainScope().launch {
            co2_data.value = response
        }
    }

    suspend fun getFaq(): FaqModel? {
        return repository.getFAQs(FaqPostModel(language = language_code)).body()
    }

    suspend fun getCO2_Level(): Double? {
        return repository.getCO2Level().body()
    }

    fun getCounterImage(context: Context): Picasso{
        return repository.getCounterImage(context)
    }

    suspend fun sendContactForm(contactData: ContactFormData) {
        repository.takeContactForm(contactData)
    }

    fun fetchData(viewModel: ViewModelData) {
        userData.value = viewModel.userData
        co2_data.value = viewModel.co2_data
        meterData = viewModel.meterData ?: ArrayList<MeterReadingData>()
        utilization_code.value = viewModel.utilizationData
        meterValue.value = viewModel.meterValue
        faq.value = viewModel.faq

        readingStepsProgress = viewModel.readingStepsProgress

        meterIndex = viewModel.meterIndex
        isCameraAllowed = viewModel.isCameraAllowed
    }

    fun getMeterIcon(meterType: String): Int{
        when(meterType) {
            "WMZ" -> return R.drawable.ic_heat_meter
            "WWZ" -> return R.drawable.ic_water_meter
            "KMZ" -> return R.drawable.ic_heat_meter
            "KWZ" -> return R.drawable.ic_water_meter
            "RWZ" -> return R.drawable.ic_heat_alocator
        }
        return R.drawable.ic_heat_meter
    }
}