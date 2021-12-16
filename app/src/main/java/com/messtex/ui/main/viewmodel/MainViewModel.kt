package com.messtex.ui.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.messtex.R
import com.messtex.data.models.*
import com.messtex.data.repositories.mainRepository.MainRepository
import com.pixolus.meterreading.MeterAppearance
import com.pixolus.meterreading.MeterReadingFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_manual_input.*
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
    var meterInitModelArray = ArrayList<MeterInitModel>()


    var readingStepsProgress: Array<Boolean> = arrayOf()

    var meterIndex: Int = 0
    var isCameraAllowed: Boolean = false
    var sendCopy: Boolean = true
    var receiveNewsletter: Boolean = true
    var receiveViaEmail: Boolean = true
    var isAppActive: Boolean = false
    var language_code: String = ""
    var scanningSuccessful: Boolean = false
    var pdfFile: String = ""
    var isVerificationCodeInvalid: Boolean = false


    suspend fun checkVerificationCode() {
        val response = repository.getUtilizationUnitData(utilization_code.value!!).body()!!
        MainScope().launch {
            userData.value = response
            isVerificationCodeInvalid = false
            if (response.firstName != null) {
                for (i in userData.value!!.meters!!.indices) {
                    readingStepsProgress += false

                    meterData.add(
                        MeterReadingData(
                            userData.value!!.meters?.get(i)!!.counterNumber,
                            userData.value!!.meters?.get(i)!!.counterType,
                            null,
                            "",
                            "",
                            "",
                            ""
                        ))

                    meterInitModelArray.add(
                        getMeterInitModel(
                            userData.value?.meters?.get(i)?.counterType ?: "WWZ"
                        )
                    )
                }

                Log.d("Meter type array", meterInitModelArray.toString())
                readingStepsProgress += false
            }
        }
    }

    suspend fun sendReadings() {
            val response = repository.takeMeterReadings(
                PostModelRecord(
                    utilization_code.value!!.verificationCode,
                    language_code,
                    meterData.toTypedArray(),
                    userData.value!!.firstName!!,
                    userData.value!!.lastName!!,
                    userData.value!!.email!!,
                    userData.value!!.phone!!,
                    sendCopy,
                    receiveViaEmail,
                    receiveNewsletter
                )
            ).body()!!

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

    fun getCounterImage(context: Context): Picasso {
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
        meterInitModelArray = viewModel.meterInitModelArray ?: ArrayList<MeterInitModel>()

        readingStepsProgress = viewModel.readingStepsProgress

        meterIndex = viewModel.meterIndex
        isCameraAllowed = viewModel.isCameraAllowed
    }

    private fun getMeterInitModel(meterType: String): MeterInitModel {
        when (meterType) {
            "WMZ" -> return MeterInitModel(
                MeterAppearance.MECHANICAL_BLACK_OR_LCD_EDL21,
                MeterReadingFragment.AUTOMATIC,
                MeterReadingFragment.AUTOMATIC,
                1
            )
            "WWZ" -> return MeterInitModel(
                MeterAppearance.AUTO_DE_WATER_HOME,
                MeterReadingFragment.AUTOMATIC,
                MeterReadingFragment.AUTOMATIC,
                1
            )
            "KMZ" -> return MeterInitModel(
                MeterAppearance.AUTO_DE_GAS_HOME,
                MeterReadingFragment.AUTOMATIC,
                MeterReadingFragment.AUTOMATIC,
                1
            )
            "KWZ" -> return MeterInitModel(
                MeterAppearance.AUTO_DE_WATER_HOME,
                MeterReadingFragment.AUTOMATIC,
                MeterReadingFragment.AUTOMATIC,
                1
            )
            "RWM" -> return MeterInitModel(
                MeterAppearance.LCD,
                0,
                MeterReadingFragment.AUTOMATIC,
                1)
        }
        return MeterInitModel(
            MeterAppearance.LCD,
            0,
            MeterReadingFragment.AUTOMATIC,
            1
        )
    }

    fun getCounterType(type: String?): MeterAppearance{
        when(type){
            "LCD_EDL21" -> return MeterAppearance.LCD_EDL21

            "LCD" -> return MeterAppearance.LCD

            "MECHANICAL_BLACK" -> return MeterAppearance.MECHANICAL_BLACK

            "MECHANICAL_WHITE" -> return MeterAppearance.MECHANICAL_WHITE

            "AUTO_DE_WATER_HOME" -> return MeterAppearance.AUTO_DE_WATER_HOME

            "AUTO_DE_GAS_HOME" -> return MeterAppearance.AUTO_DE_GAS_HOME
        }

        return MeterAppearance.MECHANICAL_BLACK_OR_LCD_EDL21
    }

    fun getMeterIcon(meterType: String): Int {
        when (meterType) {
            "WMZ" -> return R.drawable.ic_heat_meter
            "WWZ" -> return R.drawable.ic_water_meter
            "KMZ" -> return R.drawable.ic_heat_meter
            "KWZ" -> return R.drawable.ic_water_meter
            "RWM" -> return R.drawable.ic_heat_alocator
        }
        return R.drawable.ic_heat_meter
    }

    fun getPDFTitle(): String {
        when (pdfFile) {
            "Privacy_EN.pdf" -> return "Privacy policy"
            "Privacy_DE.pdf" -> return "Datenschutzerklärung"
            "Legal_EN.pdf" -> return "General terms of use"
            "Legal_DE.pdf" -> return "Allgemeine Nutzungsbedingungen"
            else -> return "Environment_DE.pdf"
        }
    }
}