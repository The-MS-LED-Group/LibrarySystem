package com.github.msledgroup.culturalcenterlibrary


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<Button>(R.id.loginButton)
        val txtMessage = findViewById<TextView>(R.id.txtMessage)
        val qrBtn = findViewById<ImageButton>(R.id.qrImageBtn)

        var timesClicked = 0
        loginBtn?.setOnClickListener {

            Toast.makeText(this, "Hey, I do nothing right now", Toast.LENGTH_SHORT).show()
        }
        qrBtn?.setOnClickListener{
            val intent = Intent(this, QRLoginActivity::class.java)
            startActivity(intent)
        }

    }
}