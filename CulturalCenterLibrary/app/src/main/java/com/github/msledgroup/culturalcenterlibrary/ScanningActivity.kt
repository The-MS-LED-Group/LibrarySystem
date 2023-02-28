package com.github.msledgroup.culturalcenterlibrary

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import io.github.g0dkar.qrcode.QRCode
import java.util.concurrent.ExecutionException

class ScanningActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 0
    }

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var previewView: PreviewView? = null
    private lateinit var scanAct: String
    private var scanBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanning)
        previewView = findViewById(R.id.scanningView)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        scanBtn = findViewById(R.id.scanbtn)
        scanBtn?.setOnClickListener {
            @Override
            fun onClick(){

            }

        }
        requestCamera()

    }

    private fun requestCamera(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            Toast.makeText(this, "results ${grantResults[0]}", Toast.LENGTH_LONG).show()
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startCamera() {
        cameraProviderFuture?.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture!!.get()
                bindCameraPreview(cameraProvider)
            } catch (e: ExecutionException) {
                Toast.makeText(this, "Error starting camera " + e.message, Toast.LENGTH_SHORT)
                    .show()
            } catch (e: InterruptedException) {
                Toast.makeText(this, "Error starting camera " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        previewView?.implementationMode = PreviewView.ImplementationMode.PERFORMANCE
        val preview: Preview = Preview.Builder().setTargetRotation(Surface.ROTATION_90)
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val type = intent.getStringExtra("type")
        preview.setSurfaceProvider(previewView?.surfaceProvider)

        var imageAnalysis: ImageAnalysis? = null

        if(type.equals("QRcode")) {
            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(this),
                QRScanner(object : QRCodeFoundListener {
                    override fun onQRCodeFound(qrCode: String?) {
                        scanAct = qrCode!!
                        scanBtn?.visibility = View.VISIBLE
                    }

                    override fun qrCodeNotFound(qrCode: String?) {
                        Toast.makeText(this@ScanningActivity, "Not found: $qrCode", Toast.LENGTH_SHORT).show()
                    }
                })
            )
        }
        else if(type.equals("Barcode")){
            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(this),
                BarCodeScanner(object : BarcodeListener {
                    override fun onBarcodeFound(barCode: String?) {
                        scanAct = barCode!!
                        scanBtn?.visibility = View.VISIBLE
                    }
                    override fun barcodeNotFound(barCode: String?) {
                        Toast.makeText(this@ScanningActivity, "Not Found: $barCode", Toast.LENGTH_SHORT).show()
                    }
                })
            )
        }
        else{
            Toast.makeText(this, "Image Analysis null", Toast.LENGTH_SHORT).show()
        }
        if(imageAnalysis != null){
            val camera: Camera = cameraProvider.bindToLifecycle(
                (this as LifecycleOwner),
                cameraSelector,
                imageAnalysis,
                preview
            )
            camera.cameraControl
        }
        else
            Toast.makeText(this, "No Image Analysis", Toast.LENGTH_SHORT).show()
    }
}