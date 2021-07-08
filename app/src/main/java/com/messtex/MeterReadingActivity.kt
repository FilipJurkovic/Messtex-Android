package com.messtex

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.messtex.ui.main.view.MainActivity
import com.pixolus.meterreading.*
import kotlinx.android.synthetic.main.fragment_code_reading.*
import kotlinx.android.synthetic.main.fragment_meter_reading.*
import kotlinx.android.synthetic.main.fragment_reading_steps.*
import java.util.*


class MeterReadingActivity : Activity(), CameraViewListener,
    MeterReadingViewListener {
    private var statusText: TextView? = null
    private var meterReadingView: MeterReadingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_meter_reading)
        meterReadingView = findViewById(R.id.meterreadingview)
        meterReadingView?.meterAppearance = MeterAppearance.AUTO_DE_GAS_HOME
        meterReadingView?.setCameraViewListener(this)
        meterReadingView?.setMeterReadingViewListener(this)


    }

    override fun onResume() {
        super.onResume()
        meterReadingView!!.enableView()
    }

    override fun onPause() {
        meterReadingView!!.disableView()
        super.onPause()
    }

    override fun onScanReadings(
        meterReadingView: MeterReadingView,
        list: List<MeterReadingResult>,
        bitmap: Bitmap,
        metadata: Metadata
    ) {
        Log.i("Meter result", list[0].toString())
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("counterValue", list[0].toString())
        startActivity(intent)
    }

    override fun onFirstDetection(meterReadingView: MeterReadingView) {}
    override fun onCameraViewCameraReady(meterReadingView: MeterReadingView) {}
    override fun onCameraViewLayoutChanged(
        meterReadingView: MeterReadingView,
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
    }

    override fun onCameraViewCameraError(meterReadingView: MeterReadingView) {
    }
}