package com.messtex

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.messtex.ui.main.viewmodel.MainViewModel
import com.pixolus.meterreading.*
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.android.synthetic.main.fragment_meter_reading.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*
import java.text.SimpleDateFormat
import java.util.*


class MeterReadingFragment : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()
    companion object {
        var meterReadingValue: String = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val meterReadingView = meterreadingview
        meterReadingView.setMeterReadingViewListener(cameraListener)
        meterReadingView.setBarcodeReadingViewListener(cameraListener)
        meterReadingView.setCameraViewListener(cameraListener)


        flashlightButton.setOnClickListener {
            meterReadingView?.isTorchOn = !meterReadingView.isTorchOn
        }

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        dateView.text = String.format("Reading date: %s", formatter.parse(Date().toString()))

        return inflater.inflate(R.layout.fragment_meter_reading, container, false)
    }

    private fun setResult(meterValue: String){
        sharedViewModel.meterValue.value = meterValue.toDouble()
        findNavController().navigate(R.id.action_meterReadingFragment_to_manualInputFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backButton.setOnClickListener() {
            findNavController().navigateUp()
        }

        infoButton.setOnClickListener() {
            findNavController().navigateUp()
        }

        if (meterReadingValue != ""){
            setResult(meterReadingValue)
        }

    }

    private val cameraListener = CameraListener()

    private class CameraListener : MeterReadingViewListener,
        BarcodeReadingViewListener {

        override fun onCameraViewCameraError(meterReadingView: MeterReadingView) {
            val message: String
        }

        override fun onScanBarcodes(
            p0: MeterReadingView,
            p1: MutableList<BarcodeReadingResult>,
            p2: Bitmap,
            p3: Metadata
        ) {
        }

        override fun onCameraViewCameraReady(meterReadingView: MeterReadingView) {
            Log.i("Meter Reading View", "Camera is ready")
        }

        override fun onCameraViewLayoutChanged(
            meterReadingView: MeterReadingView,
            changed: Boolean,
            left: Int, top: Int, right: Int, bottom: Int
        ) {
            Log.i("Meter reading view", "Layout changed")
        }

        override fun onScanReadings(
            meterReadingView: MeterReadingView,
            results: List<MeterReadingResult>,
            image: Bitmap,
            metadata: Metadata
        ) {
            Log.i(
                "Reading result",
                "Did scan " + results.size + " readings"
            )
            if (results.size == 1) {
                val meterReading = results[0]
                meterReadingValue = meterReading.cleanReadingString()

            } else if (results.size == 2) {
                val meterReading = results[0]
                meterReadingValue = meterReading.cleanReadingString()
            }
        }

        override fun onFirstDetection(meterReadingView: MeterReadingView) {
            Log.i(
                "Meter reading View",
                "First detection of a meter"
            )
        }
    }
}