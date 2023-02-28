package com.github.msledgroup.culturalcenterlibrary

import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Camera
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.Result
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CameraSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject


class BarcodeScannerActivity : AppCompatActivity() {
    var barcodeView: DecoratedBarcodeView? = null
    var beepManager: BeepManager? = null
    var lastText: String? = null
    private var result: Result? = null
    var barcodeAPIs: BarcodeApiLookup = BarcodeApiLookup()
    var codeQue: MutableList<JSONObject?> = MutableList(1){null}


    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text.isEmpty() || result.text == lastText) {
                // Prevent duplicate scans
                return
            }
            lastText = result.text
            barcodeView!!.setStatusText(result.text)
            beepManager!!.playBeepSoundAndVibrate()

            val book: JSONObject? = barcodeAPIs.ISBNapiCall(result.text)

            Toast.makeText(this@BarcodeScannerActivity, "book: ${book?.keys().toString()}", Toast.LENGTH_SHORT).show()

            //Added preview of scanned barcode
            val imageView: ImageView = findViewById(R.id.barcodePreview)
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Camera doesn't have permission", Toast.LENGTH_SHORT).show()

        }
        setContentView(R.layout.activity_barcode_scanner)
        this.barcodeView = findViewById(R.id.barcode_scanner)
        val formats: Collection<BarcodeFormat> =
            listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39,
                BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.CODE_128,
                BarcodeFormat.UPC_EAN_EXTENSION
            )
        this.barcodeView!!.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        this.barcodeView!!.initializeFromIntent(intent)
        this.barcodeView!!.decodeContinuous(callback)
        this.beepManager = BeepManager(this)
    }

    override fun onResume() {
        super.onResume()
        barcodeView!!.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView!!.pause()
    }

    fun pause(view: View?) {
        barcodeView!!.pause()
    }

    fun resume(view: View?) {
        Toast.makeText(this, "$result.", Toast.LENGTH_SHORT).show()
        barcodeView!!.resume()
    }

    fun triggerScan(view: View?) {
        barcodeView!!.decodeSingle(callback)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}