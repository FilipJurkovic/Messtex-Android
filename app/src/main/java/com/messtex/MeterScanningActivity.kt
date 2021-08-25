package com.messtex

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.picasso.OkHttp3Downloader
import com.messtex.data.models.ViewModelData
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.main.view.MainActivity
import com.messtex.ui.main.viewmodel.MainViewModel
import com.messtex.utils.ViewModelFactory
import com.pixolus.meterreading.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.android.synthetic.main.fragment_manual_input.*
import kotlinx.android.synthetic.main.fragment_meter_reading.*
import kotlinx.android.synthetic.main.fragment_meter_reading.view.*
import kotlinx.android.synthetic.main.fragment_modal_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_modal_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*
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

        overlayView.returnButton.setOnClickListener { onBackPressed() }

        val meterIndex: Int? = passedData?.meterIndex
        val imageUrl: String? = passedData?.userData?.meters?.get(meterIndex ?: 0)?.counterDescriptionImage
        val explanation: String? = passedData?.userData?.meters?.get(meterIndex ?: 0)?.counterDescriptionText

        overlayView.infoButton.setOnClickListener {
            showBottomModalSheet(this, imageUrl ?: "", explanation ?: "")
        }

        overlayView.manual_button.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("counterValue", "")
            intent.putExtra("ViewModel", passedData)
            startActivity(intent)
        }

        val standardFormat = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date : Date = standardFormat.parse(Date().toString())
        overlayView.dateView.text = String.format("Reading date: %s", formatter.format(date))

        val counterNumber : String? = passedData?.userData?.meters?.get(meterIndex ?: 0)?.counterNumber

        overlayView.counterNumberView.text = String.format("Please scan the counter:\nNr. %s", counterNumber)

        return overlayView
    }


    override fun onCameraReady(meterReader: MeterReadingFragment) {
//        val hasTorch = meterReader.hasTorch()
//        mTorchButton!!.visibility = if (hasTorch) View.VISIBLE else View.GONE
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
        Log.d("Meter result", String.format("%.2f", results[0].cleanReadingString().toDouble()))
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("counterValue", String.format("%.2f", results[0].cleanReadingString().toDouble()))
        intent.putExtra("ViewModel", passedData)
        startActivity(intent)
    }

    private fun showBottomModalSheet(context: Context, imageUrl: String, explanationText: String){
       val bottomSheetDialog : BottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.fragment_modal_bottom_sheet)
        val exitButton : TextView? = bottomSheetDialog.findViewById<TextView>(R.id.infoExitButton)
        val image: ImageView? = bottomSheetDialog.findViewById<ImageView>(R.id.meterImage)
        val explanation : TextView? = bottomSheetDialog.findViewById<TextView>(R.id.explanation)
        bottomSheetDialog.show()

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
                .build()
            chain.proceed(newRequest)
        }).build()

        val picasso: Picasso = Picasso.Builder(context).downloader(OkHttp3Downloader(client)).build()

        if(imageUrl != ""){
            picasso.load(imageUrl).into(image)
            explanation?.text = explanationText
        }
        else{
            image?.setImageResource(R.drawable.watermeter_illustration)
            explanation?.text = "This is dummy text!"
        }

        exitButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }


}
