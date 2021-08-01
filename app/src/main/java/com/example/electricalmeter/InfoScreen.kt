@file:Suppress("DEPRECATION")

package com.example.electricalmeter

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import calculate.format_seconds


class InfoScreen : Activity() {
    lateinit var impcount: String
    lateinit var intervalimp: String
    lateinit var timev: String
    lateinit var intent_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_FULLSCREEN)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_info_screen)
        //supportActionBar?.hide()
        get_data()

        var button_home = findViewById<Button>(R.id.button_home_is)
        var button_back = findViewById<Button>(R.id.button_back_is)
        var camera_button = findViewById<Button>(R.id.camera_button)

        camera_button.setOnClickListener {
            to_camera()
        }

        button_home.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }

        button_back.setOnClickListener{
            if (intent_name == "voltamper"){
                var backintent = Intent(this, VoltAmperScreen::class.java)
                startActivity(backintent)
            }else if(intent_name == "powerscreen"){
                var backintent = Intent(this, powerscreen::class.java)
                startActivity(backintent)
            }
        }
    }


    fun get_data(): String {

        val voltage_value = findViewById<TextView>(R.id.voltage_value)
        val amper_value = findViewById<TextView>(R.id.amper_value)
        val power_value = findViewById<TextView>(R.id.power_value)
        val imphour_value = findViewById<TextView>(R.id.impkwh_value)
        val impcount_value = findViewById<TextView>(R.id.impcount_value) //
        val interval_value = findViewById<TextView>(R.id.interval_value) //
        val time_value = findViewById<TextView>(R.id.time_value) //

        intent_name = intent.getStringExtra("name").toString()
        var power = intent.getStringExtra("power")
        var imphour = intent.getStringExtra("imphour") // импульсов в час
        impcount = intent.getStringExtra("impcount").toString() // кол-во импульсов
        var voltage = intent.getStringExtra("voltage")
        var amper = intent.getStringExtra("amper")
        intervalimp = intent.getStringExtra("interval").toString() // интервал между импульсами
        timev = intent.getStringExtra("time").toString() // общее время



        voltage_value.text = voltage
        amper_value.text = amper
        power_value.text = power
        imphour_value.text = imphour
        impcount_value.text = impcount


        var (m, s) = format_seconds(timev?.toInt())
        time_value.text = "${m.toString()}мин ${s.toString()}сек"
        var (m1, s1) = format_seconds(intervalimp?.toInt())
        interval_value.text = "${m1.toString()}мин ${s1.toString()}сек"
        return intent_name.toString()
    }

    fun to_camera(){
        val intent_camera = Intent(this, CameraActivity::class.java)
        intent_camera.putExtra("impcount", impcount)
        intent_camera.putExtra("time", timev)
        intent_camera.putExtra("interval", intervalimp)
        intent_camera.putExtra("name", intent_name)
        startActivity(intent_camera)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putString("name", intent_name)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        intent_name = savedInstanceState?.getString("name").toString()
    }

}

