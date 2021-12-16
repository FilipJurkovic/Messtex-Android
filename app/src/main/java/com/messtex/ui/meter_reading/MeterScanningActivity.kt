package com.messtex.ui.meter_reading

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.picasso.OkHttp3Downloader
import com.messtex.R
import com.messtex.data.models.MeterReceivingData
import com.messtex.data.models.ViewModelData
import com.messtex.ui.main.view.MainActivity
import com.pixolus.meterreading.Metadata
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import com.pixolus.meterreading.MeterReadingResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_meter_reading.view.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*


class MeterScanningActivity : MeterReadingActivity() {

    private var passedData: ViewModelData? = null

    @SuppressLint("EmptyMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this, getLocale())
    }

    private fun getLocale(): String? {
        val sharedPref = getSharedPreferences("locale", Context.MODE_PRIVATE)
        return sharedPref.getString("language", "de")
    }

    private fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onCreateOverlayView(): View {

        passedData = intent.getSerializableExtra("ViewModel") as ViewModelData


        Log.d("ViewModel", passedData.toString())

        @SuppressLint("InflateParams") val overlayView: View =
            layoutInflater.inflate(R.layout.fragment_meter_reading, null)


        overlayView.flashlightButton.setOnClickListener {
            val newTorchState = !meterReadingFragment.isTorchOn
            meterReadingFragment.isTorchOn = newTorchState
        }

        overlayView.returnButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("meterScanningExited", true)
            intent.putExtra("ViewModel", passedData)
            startActivity(intent)
            finish()
        }

        val meterIndex: Int? = passedData?.meterIndex
        val imageUrl: String? =
            passedData?.userData?.meters?.get(meterIndex ?: 0)?.counterDescriptionImage
        val explanation: String? =
            passedData?.userData?.meters?.get(meterIndex ?: 0)?.counterDescriptionText

        overlayView.infoButton.setOnClickListener {
            showBottomModalSheet(this, imageUrl ?: "", explanation ?: "")
        }

        val counterValue: String = passedData?.meterData?.get(passedData!!.meterIndex)?.counterValue.toString().replace(",", ".")

        overlayView.manual_button.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("counterValue", if(counterValue != "null") counterValue else "")
            intent.putExtra("rawReadingString", "")
            intent.putExtra("cleanReadingString", "")
            intent.putExtra("readingResultStatus", "")
            intent.putExtra("ViewModel", passedData)
            startActivity(intent)
            finish()
        }

        val standardFormat = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date: Date = standardFormat.parse(Date().toString())

        val dateFormat: String = getString(R.string.date, formatter.format(date))
        overlayView.dateView.text = dateFormat

        val currentMeter: MeterReceivingData? = passedData?.userData?.meters?.get(meterIndex ?: 0)
        val counterNumber: String? = currentMeter?.counterNumber
        val counterRoom: String? = currentMeter?.counterRoom
        val counterType: String? = currentMeter?.counterTypeName

        overlayView.counterNumberView.text =
            getString(R.string.counter_number_explanation, counterRoom, counterType, counterNumber)

        return overlayView
    }


    override fun onCameraReady(meterReader: MeterReadingFragment) {
//        meterReader.isTorchOn = true
    }

    override fun onLayoutChanged(
        meterReadingFragment: MeterReadingFragment,
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayoutChanged(meterReadingFragment, changed, left, top, right, bottom)
    }

    /**
     * Intercept results to gain access to the camera image corresponding to the result.
     *
     * Compare the documentation of the MeterReadingActivity to understand the details of this
     * process and have a look at the Javadoc documentation of the MeterReadingListener
     *
     * @param meterReadingFragment instance of the fragment that found the readings
     * @param results              result data (the actual readings)
     * @param image                result image
     * @param metadata             metadata about the recognition process (timings, etc.)
     */
    override fun onScanReadings(
        meterReadingFragment: MeterReadingFragment,
        results: List<MeterReadingResult>,
        image: Bitmap,
        metadata: Metadata
    ) {
        Log.d("rawReadingString", results[0].rawReadingString())
        Log.d("cleanReadingString", String.format("%.2f", results[0].cleanReadingString().toDouble()))
        Log.d("readingResultStatus", results[0].status().name)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(
            "counterValue",
            String.format("%.2f", results[0].cleanReadingString().toDouble())
        )
        intent.putExtra("rawReadingString", results[0].rawReadingString())
        intent.putExtra("cleanReadingString", results[0].cleanReadingString())
        intent.putExtra("readingResultStatus", results[0].status().name)
        intent.putExtra("ViewModel", passedData)
        startActivity(intent)
        finish()
    }

    private fun showBottomModalSheet(context: Context, imageUrl: String, explanationText: String) {
        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.fragment_modal_bottom_sheet)
        val exitButton: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.infoExitButton)
        val image: ImageView? = bottomSheetDialog.findViewById<ImageView>(R.id.meterImage)
        val explanation: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.explanation)
        bottomSheetDialog.show()

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
                .build()
            chain.proceed(newRequest)
        }).build()

        val picasso: Picasso =
            Picasso.Builder(context).downloader(OkHttp3Downloader(client)).build()

        if (imageUrl != "") {
            picasso.load(imageUrl).into(image)
            explanation?.text = explanationText
        } else {
            image?.setImageResource(R.drawable.watermeter_illustration)
            explanation?.text = "This is dummy text!"
        }

        exitButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("meterScanningExited", true)
        intent.putExtra("ViewModel", passedData)
        startActivity(intent)
        finish()
    }


}
