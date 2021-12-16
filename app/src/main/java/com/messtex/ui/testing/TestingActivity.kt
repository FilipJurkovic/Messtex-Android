package com.messtex.ui.testing

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.messtex.R
import com.messtex.data.models.MeterConfigurationModel
import com.messtex.ui.main.view.MainActivity
import com.pixolus.meterreading.Metadata
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import com.pixolus.meterreading.MeterReadingResult
import kotlinx.android.synthetic.main.fragment_meter_reading.view.*
import java.util.*


class TestingActivity : MeterReadingActivity() {

    private var configuration: MeterConfigurationModel? = null
    private var testIndex: Int = 0
    private var testingResultsList: Array<Double> = emptyArray()
    private var resultsHeat: Array<Double> = arrayOf(6.090, 6.090, 6.090, 6.090, 6.090, 6.090)
    private var resultsWater: Array<Double> = arrayOf(6.090, 6.090, 6.090, 6.090, 6.090, 6.090)

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

        configuration = intent.getSerializableExtra("configuration") as MeterConfigurationModel

        @SuppressLint("InflateParams") val overlayView: View =
            layoutInflater.inflate(R.layout.activity_testing, null)


        showIntroDialog(this, ({}))


        overlayView.flashlightButton.setOnClickListener {
            val newTorchState = !meterReadingFragment.isTorchOn
            meterReadingFragment.isTorchOn = newTorchState
        }

        overlayView.returnButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("meterScanningExited", false)
            startActivity(intent)
            finish()
        }



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
        meterReadingFragment.meterReadingView.disableView()
        showMeterTestingDialog(this, results[0].cleanReadingString(), results[0].rawReadingString(), results[0].status().name)
    }


    private fun showIntroDialog(activity: Activity, onConfirm: () -> Unit) {
        val dialog: Dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.testing_intro)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cancelationButton: Button = dialog.findViewById<Button>(R.id.cancelButton)
        val confirmationButton: Button = dialog.findViewById<Button>(R.id.startButton)

        val counterType: TextView = dialog.findViewById<TextView>(R.id.counterType)
        val integerDigits: TextView = dialog.findViewById<TextView>(R.id.integerDigits)
        val fractionDigits: TextView = dialog.findViewById<TextView>(R.id.fractionDigits)
        val meterCount: TextView = dialog.findViewById<TextView>(R.id.meterCount)

        counterType.text = configuration?.meterAppearance
        integerDigits.text = if(configuration?.integerDigitsAuto!!) "AUTO" else configuration?.integerDigits.toString()
        fractionDigits.text = if(configuration?.fractionDigitsAuto!!) "AUTO" else configuration?.fractionDigits.toString()
        meterCount.text = if(configuration?.numberOfCountersAuto!!) "AUTO" else configuration?.numberOfCounters.toString()

        dialog.show()

        cancelationButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("meterScanningExited", false)
            startActivity(intent)
            finish()
        }

        confirmationButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showMeterTestingDialog(activity: Activity, meterCounterValue: String, meterRawValue: String, meterResultCode: String) {
        val dialog: Dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.testing_meter)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cancelationButton: Button = dialog.findViewById<Button>(R.id.retryButton)
        val confirmationButton: Button = dialog.findViewById<Button>(R.id.nextButton)

        val counterValue: TextView = dialog.findViewById<TextView>(R.id.counterValue)
        val rawValue: TextView = dialog.findViewById<TextView>(R.id.rawValue)
        val resultCode: TextView = dialog.findViewById<TextView>(R.id.resultCode)

        counterValue.text = meterCounterValue
        rawValue.text = meterRawValue
        resultCode.text = meterResultCode

        dialog.show()

        cancelationButton.setOnClickListener {
            dialog.dismiss()
            meterReadingFragment.meterReadingView.enableView()
        }

        confirmationButton.setOnClickListener {
            dialog.dismiss()
            meterReadingFragment.meterReadingView.enableView()
            testingResultsList.plus(meterCounterValue.toDouble())
            testIndex += 1

            Log.d("Test index", testIndex.toString())

            if (testIndex == 6){
                showFinalDialog(this,)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showFinalDialog(activity: Activity) {
        val dialog: Dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.testing_result)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val exitButton: Button = dialog.findViewById<Button>(R.id.nextButton)

        val overallScore: TextView = dialog.findViewById<TextView>(R.id.overallScore)
        val passValue: TextView = dialog.findViewById<TextView>(R.id.passValue)

        var score : Int = 0
        val resultsArray = if (configuration?.meterAppearance == "AUTO_DE_WATER_HOME") resultsWater else resultsHeat

        Log.d("Test index", resultsWater.toString())

        testingResultsList.forEachIndexed { index, element ->
            if(element == resultsArray[index]){
                score += 1
            }
        }

        overallScore.text = "$score / ${resultsArray.size}"
        passValue.text = if(score == resultsArray.size) "TRUE" else "FALSE"

        dialog.show()

        exitButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("meterScanningExited", false)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("meterScanningExited", false)
        startActivity(intent)
        finish()
    }


}