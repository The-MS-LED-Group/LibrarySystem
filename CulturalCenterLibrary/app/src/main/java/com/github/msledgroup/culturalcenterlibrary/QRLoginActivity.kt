package com.github.msledgroup.culturalcenterlibrary

import android.content.pm.PackageManager

import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import java.util.concurrent.ExecutionException


class QRLoginActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 0
    }

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var previewView: PreviewView? = null
    private lateinit var qrCodeAct: String
    private var qrCodeBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrlogin)
        previewView = findViewById(R.id.preView_Cam)

        qrCodeBtn = findViewById(R.id.qrCode_btn)
        qrCodeBtn?.visibility = View.INVISIBLE
        qrCodeBtn?.setOnClickListener {
            @Override
            fun onClick() {
                Toast.makeText(applicationContext, qrCodeAct, Toast.LENGTH_SHORT).show()
                Log.i(QRLoginActivity::class.java.simpleName, "QR Code Found: $qrCodeAct")
            }
        }
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        requestCamera()
    }

    private fun requestCamera(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA.toString()),
                    PERMISSION_REQUEST_CAMERA)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
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

        preview.setSurfaceProvider(previewView?.surfaceProvider)
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            QRScanner(object: QRCodeFoundListener {
                override fun onQRCodeFound(qrCode: String?) {
                    qrCodeAct = qrCode!!
                    qrCodeBtn?.visibility = View.VISIBLE
                }

                override fun qrCodeNotFound() {
                    qrCodeBtn?.visibility = View.INVISIBLE
                }
            })
        )
        val camera: Camera = cameraProvider.bindToLifecycle(
            (this as LifecycleOwner),
            cameraSelector,
            imageAnalysis,
            preview
        )
        camera.cameraControl

    }
}