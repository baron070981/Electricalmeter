@file:Suppress("DEPRECATION")

package com.example.electricalmeter

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import calculate.calculate_interval
import calculate.calculate_time
import calculate.get_power
import calculate.validate

class VoltAmperScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_volt_amper_screen)
        //supportActionBar?.hide()

        var button_home = findViewById<Button>(R.id.button_home_vas)
        var button_back = findViewById<Button>(R.id.button_back_vas)

        button_home.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }

        button_back.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }

        var calcbtn = findViewById<Button>(R.id.calcbtn)
        calcbtn.setOnClickListener{
            get_data()
        }
    }

    fun get_data() {
        var _volt = findViewById<EditText>(R.id.inputvolt)
        var _amper = findViewById<EditText>(R.id.inputamper)
        var _imphour = findViewById<EditText>(R.id.inputimphour)
        var _impcount = findViewById<EditText>(R.id.inputimp)
        var volt = _volt.text.toString()
        var amper = _amper.text.toString()
        var imphour = _imphour.text.toString()
        var impcount = _impcount.text.toString()

        if (validate(volt) || validate(amper) || validate(imphour) || validate(impcount) ){
            Toast.makeText(this, "Заполнить поля", Toast.LENGTH_LONG).show()
            return
        }

        val intentdata = Intent(this, InfoScreen::class.java)
        var power = get_power(volt.toFloat(), amper.toFloat()) /1000
        var total_sec = calculate_time(power?.toFloat(), imphour?.toInt(), impcount?.toInt())
        var imp_sec = calculate_interval(power?.toFloat(), imphour?.toInt())

        intentdata.putExtra("name", "voltamper")
        intentdata.putExtra("power", power.toString())
        intentdata.putExtra("imphour", imphour)
        intentdata.putExtra("impcount", impcount)
        intentdata.putExtra("voltage", volt)
        intentdata.putExtra("amper", amper)
        intentdata.putExtra("interval", imp_sec.toString())
        intentdata.putExtra("time", total_sec.toString())
        startActivity(intentdata)

    }
}