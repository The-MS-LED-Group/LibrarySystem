package com.github.msledgroup.culturalcenterlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.common.util.concurrent.ListenableFuture

class BookScannerActivity : AppCompatActivity() {

    companion object{
        private const val PERMISSION_REQUEST_CAMERA = 0
    }

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var scnPreviewView: PreviewView? = null
    private lateinit var scnCodeAct: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_scanner)



    }
}