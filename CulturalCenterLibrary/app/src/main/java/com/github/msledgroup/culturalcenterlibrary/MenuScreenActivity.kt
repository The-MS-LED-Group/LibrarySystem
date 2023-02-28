package com.github.msledgroup.culturalcenterlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_screen)

        val checkoutBtn: Button = findViewById(R.id.checkoutBtn)
        val scannerBtn: Button = findViewById(R.id.scannerBtn)

        checkoutBtn.setOnClickListener{
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivity(intent)
        }

        scannerBtn.setOnClickListener{
            val intent = Intent(this, ScanningActivity::class.java)
            intent.putExtra("type", "Barcode")
            startActivity(intent)
        }
    }
}