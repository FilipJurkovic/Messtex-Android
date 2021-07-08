package com.messtex.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.messtex.R
import com.pixolus.meterreading.Metadata
import com.pixolus.meterreading.MeterReadingActivity
import com.pixolus.meterreading.MeterReadingFragment
import com.pixolus.meterreading.MeterReadingResult
import java.util.*


class PixolousReadingActivity : MeterReadingActivity() {
    // UI elements
    private var mTorchButton: Button? = null

    @SuppressLint("EmptyMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // We do not create the layout here, because the basic layout is provided by the super class
        // We inflate our own layout in onCreateOverlayView()
    }

    override fun onCreateOverlayView(): View {
        // We should not invoke super here.

        /* Load layout from resources.
         * We pass null for the rootView parameter, although the Android Studio code analyzer warns
         * about this and recommends against it. The root view parameter is used to set the layout
         * parameters of the root view of the inflated layout. If we pass null, the layout parameters
         * of the root view of the inflated layout are ignored. However, the documentation of
         * onCreateOverlayView() of the MeterReading SDK states that the returned view will be added
         * to its parent view using MATCH_PARENT parameters for width and height. Therefore, we just
         * use the MATCH_PARENT setting on the root view of the layout to be inflated when designing
         * the layout. This allows us to get a realistic preview of the overlay in the UI designer.
         */
        @SuppressLint("InflateParams") val overlayView: View =
            layoutInflater.inflate(R.layout.activity_pixolous_reading, null)

        // Get and initialize views

        // We must not query the torch state here (before onCameraViewCameraReady()) has been called, but we
        // can already install event handlers and perform other initialization tasks that do not
        // depend on the torch state and availability methods. Everything else is deferred to the
        // implementation of onCameraViewCameraReady().
        mTorchButton?.setOnClickListener() { // Toggle the torch state
            val newTorchState = !meterReadingFragment.isTorchOn
            meterReadingFragment.isTorchOn = newTorchState

        }
        val cancelButton = overlayView.findViewById<Button>(R.id.btnCancel)
        cancelButton.setOnClickListener { onBackPressed() }
        return overlayView
    }

    /**
     * This method is called when the camera was successfully obtained by the MeterReadingFragment.
     *
     * Since on Android, we can only understand about camera capabilities, after we have established
     * access to the camera, this is the point where an app knows if the devices has a camera flash/
     * torch or not.
     *
     * Compare the Javadoc documentation of the MeterReadingListener interface.
     *
     * @param meterReader the actual meter reading fragment that obtained the camera
     */
    override fun onCameraReady(meterReader: MeterReadingFragment) {

        // Torch related methods of the fragment may not be invoked before onCameraViewCameraReady() has been
        // called, so we set up the visibility of the torch button here instead of in onCreateOverlayView().
        val hasTorch = meterReader.hasTorch()
        mTorchButton!!.visibility = if (hasTorch) View.VISIBLE else View.GONE

    }

    /**
     * This method is called when the layout changed.
     *
     * Its main reason for existence is that you can adapt the region of interest on the
     * MeterReadingFragment here. If you don't do anything special, the MeterReadingFragment will
     * adjust the ROI to the visible part of the camera image.
     *
     * Compare the Javadoc documentation of the MeterReadingListener interface.
     *
     * @param meterReadingFragment  The MeterReadingFragment that originated the call.
     * @param changed               Did the layout change?
     * @param left                  The x coordinate of the left side of the view.
     * @param top                   The y coordinate of the top side of the view.
     * @param right                 The x coordinate of the right side of the view.
     * @param bottom                The y coordinate of the bottom side of the view.
     */
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

        // we could do something with 'image' here, for example, save it into an apps database or
        // write it to the file system

//        File file = new File(getApplicationContext().getCacheDir(), "result.png");
//        try {
//            // create file on disk
//            file.createNewFile();
//
//            // convert bitmap to byte array
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//
//            // write the bytes in file
//            FileOutputStream stream = new FileOutputStream(file);
//            stream.write(bitmapdata);
//            stream.flush();
//            stream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // this is basically the implementation that we are overriding:
        returnResults(RESULT_OK, ArrayList(results))
    }

    /**
     * Simple helper method that adapts the torch button text to the current torch state.
     */

}
