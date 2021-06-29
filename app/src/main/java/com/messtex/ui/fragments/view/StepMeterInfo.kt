package com.messtex.ui.fragments.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.messtex.BuildConfig
import com.messtex.R
import com.messtex.data.models.MeterData
import com.messtex.ui.main.view.MainActivity
import com.messtex.ui.main.viewmodel.MainViewModel
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingResult
import com.pixolus.meterreading.MeterReadingView
import kotlinx.android.synthetic.main.step_meter_info_fragment.*


class StepMeterInfo : Fragment() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    private val PERMISSIONS_REQUEST_CAMERA = 42
    private var mStartActivityIntent: Intent? = null
    private var mStartActivityForResult = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.step_meter_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val meterTypeArray = resources.getStringArray(R.array.meter_types_array)
        meterTypeList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val data = sharedViewModel.meterData.value
                sharedViewModel.meterData.postValue(
                    MeterData(
                        data?.meterId,
                        data?.meterValue,
                        parent?.getItemAtPosition(position).toString()
                    )
                )
            }

        }

        sharedViewModel.meterData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                meterIdText.setText(it.meterId)
                meterReadingText.setText(it.meterValue)
                if (it.meterType != null) meterTypeList.setSelection(meterTypeArray.indexOf(it.meterType))
            }

        })


        openPixolousButton.setOnClickListener() {
            val intent: Intent = Intent(it.context, MeterReadingActivity::class.java)
                .putExtra(
                    MeterReadingActivity.EXTRA_INTEGER_DIGITS,
                    MeterReadingView.AUTOMATIC
                )
                .putExtra(
                    MeterReadingActivity.EXTRA_FRACTION_DIGITS,
                    MeterReadingView.AUTOMATIC
                )
                .putExtra(
                    MeterReadingActivity.EXTRA_NUMBER_OF_COUNTERS,
                    MeterReadingView.AUTOMATIC
                )
                .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_UNREADABLE_COUNTER, 0)
                .putExtra(MeterReadingActivity.EXTRA_TIMEOUT_AFTER_LAST_DETECTION, 0)
                .putExtra(MeterReadingActivity.EXTRA_TIMEOUT, 0)
                .putExtra(MeterReadingActivity.EXTRA_ALLOWS_ROTATION, true)
                .putExtra(MeterReadingActivity.EXTRA_ZOOM, 1.3)
                .putExtra(MeterReadingActivity.EXTRA_IS_RESULTS_OVERLAY_VISIBLE, true)
                .putExtra(
                    MeterReadingActivity.EXTRA_IS_DEBUG_OVERLAY_VISIBLE,
                    BuildConfig.DEBUG
                )
            startActivityWithPermissionRequest(intent, true)
            sharedViewModel.updateMeterValueData(onPixolousResult(0, intent))
        }
        postApi.setOnClickListener() {
            sharedViewModel.postApi()
//            findNavController().navigate(R.id.action_stepMeterInfo_to_stepSuccess)
        }


    }

    private fun startActivityWithPermissionRequest(
        intent: Intent,
        startActivityForResult: Boolean
    ) {
        this.mStartActivityIntent = intent
        mStartActivityForResult = startActivityForResult
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // use this if the usage of the permission is not clear
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {

                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.
                val mLayout: View = requireView().findViewById(R.id.content)
                if (mLayout != null) {
                    Snackbar.make(
                        mLayout,
                        "Camera permission is needed for automatic meter reading.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(android.R.string.ok, View.OnClickListener {
                            ActivityCompat.requestPermissions(
                                this.requireActivity(), arrayOf(Manifest.permission.CAMERA),
                                PERMISSIONS_REQUEST_CAMERA
                            )
                        })
                        .show()
                }
            } else {
                // Camera permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(
                    this.requireActivity(), arrayOf(Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST_CAMERA
                )
            }
            return
        }
        if (startActivityForResult) {
            startActivityForResult(intent, 0)
        } else {
            startActivity(intent)
        }
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
}
