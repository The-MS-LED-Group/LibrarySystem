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
        val searchBtn: Button = findViewById(R.id.searchBtn)

        checkoutBtn.setOnClickListener{
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivity(intent)
        }

        scannerBtn.setOnClickListener{
            val intent = Intent(this, ScanningActivity::class.java)
            intent.putExtra("type", "Barcode")
            startActivity(intent)
        }

        searchBtn.setOnClickListener{
            val intent = Intent(this, listActivity::class.java)
            startActivity(intent)
        }
    }
}