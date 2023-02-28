package com.github.msledgroup.culturalcenterlibrary

import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<Button>(R.id.loginButton)
        val txtMessage = findViewById<TextView>(R.id.txtMessage)
        val qrBtn = findViewById<ImageButton>(R.id.qrImageBtn)
        val editText = findViewById<EditText>(R.id.txtEdit)

        loginBtn?.setOnClickListener {
            Toast.makeText(this, "logging in", Toast.LENGTH_SHORT).show()
            if(editText.text.isNotEmpty()){
                //TODO: add Firebase here instead of text.isEmpty()
                //TODO: check for valid user/password entered
                //TODO: try/catch block for AuthO in FireBase
                //Already implemented QRcode validation/need testing.
                val intent = Intent(this, MenuScreenActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Failed, username and password doesn't match", Toast.LENGTH_LONG).show()
            }
        }
        qrBtn?.setOnClickListener{
            val intent = Intent(this, ScanningActivity::class.java)
            intent.putExtra("type", "QRcode")
            startActivity(intent)
        }

    }
}