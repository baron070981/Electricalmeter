@file:Suppress("UnusedImport", "DEPRECATION")

package com.example.electricalmeter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log

import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class MainActivity : Activity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_main)
        // supportActionBar?.hide()

        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val exitbtn = findViewById<Button>(R.id.exitbtn)
        val wiringbtn = findViewById<Button>(R.id.wiring)

        button2.setOnClickListener {
            power_calc_screen(button2)
        }
        button3.setOnClickListener {
            voltamper_calc_screen(button3)
        }
        exitbtn.setOnClickListener {
            finishAffinity()
        }
        wiringbtn.setOnClickListener{
            wiring_calc()
        }
    }

    fun power_calc_screen(view: View?) {
        val power_intent = Intent(this, powerscreen::class.java)
        startActivity(power_intent)
    }

    fun voltamper_calc_screen(view: View?) {
        val va_intent = Intent(this, VoltAmperScreen::class.java)
        startActivity(va_intent)
    }

    fun wiring_calc() {
        val wire_intent = Intent(this, CalculationWiring::class.java)
        startActivity(wire_intent)
    }

}