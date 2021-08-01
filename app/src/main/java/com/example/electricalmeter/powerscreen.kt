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
import calculate.validate

class powerscreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_powerscreen)
        //supportActionBar?.hide()
        var calcbtn = findViewById<Button>(R.id.calcbtn)
        calcbtn.setOnClickListener{
            get_data()
        }
        var button_home = findViewById<Button>(R.id.button_home_pow)
        var button_back = findViewById<Button>(R.id.button_back_pow)

        button_home.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }

        button_back.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }
    }

    fun get_data() {
        var _power = findViewById<EditText>(R.id.inputvolt)
        var _imphour = findViewById<EditText>(R.id.inputamper)
        var _impcount = findViewById<EditText>(R.id.inputimphour)
        var power = _power.text.toString() // мощность нагрузки
        var imphour = _imphour.text.toString() // импульсов в час
        var impcount = _impcount.text.toString() // число импульсов
        val intentdata = Intent(this, InfoScreen::class.java)

        if(validate(power) || validate(imphour) || validate(impcount)){
            Toast.makeText(this, "Заполнить поля", Toast.LENGTH_LONG).show()
            return
        }

        // найти общее время для установленного кол-ва импульсов(сек.)
        var total_sec = calculate_time(power?.toFloat(), imphour?.toInt(), impcount?.toInt())
        // найти интервал между импульсами(сек.)
        var imp_sec = calculate_interval(power?.toFloat(), imphour?.toInt())

        intentdata.putExtra("name", "powerscreen")
        intentdata.putExtra("power", power)
        intentdata.putExtra("imphour", imphour)
        intentdata.putExtra("impcount", impcount)
        intentdata.putExtra("voltage", "не известно")
        intentdata.putExtra("amper", "не известно")
        intentdata.putExtra("interval", imp_sec.toString())
        intentdata.putExtra("time", total_sec.toString())
        startActivity(intentdata)
    }


}