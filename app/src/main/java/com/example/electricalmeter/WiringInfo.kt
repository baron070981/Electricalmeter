@file:Suppress("DEPRECATION")

package com.example.electricalmeter

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import calculate.Alumin
import calculate.Copper

class WiringInfo : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_wiring_info)

        var button_home = findViewById<Button>(R.id.button_home_wi)
        var button_back = findViewById<Button>(R.id.button_back_wi)

        button_home.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }

        button_back.setOnClickListener{
            var homeintent = Intent(this, CalculationWiring::class.java)
            startActivity(homeintent)
        }

        get_data()
    }

    fun get_data(){
        var _power = intent.getStringExtra("power")
        var _lenght = intent.getStringExtra("lenght")

        val power_val = findViewById<TextView>(R.id.power_val)
        val lenght_val = findViewById<TextView>(R.id.lenght_val)
        val amper_val = findViewById<TextView>(R.id.amper_val)

        val cop_res_val = findViewById<TextView>(R.id.cop_res_val)
        val cop_cross_val = findViewById<TextView>(R.id.cop_cross_val)
        val max_amp_cop_val = findViewById<TextView>(R.id.max_amp_cop_val)
        val cop_delta_val = findViewById<TextView>(R.id.cop_delta_val)
        val cop_percent_val = findViewById<TextView>(R.id.cop_percent_val)

        val al_resist_val = findViewById<TextView>(R.id.al_resist_val)
        val al_cross_val = findViewById<TextView>(R.id.al_cross_val)
        val al_max_amp_val = findViewById<TextView>(R.id.al_max_amp_val)
        val al_delta_val = findViewById<TextView>(R.id.al_delta_val)
        val al_percent_val = findViewById<TextView>(R.id.al_percent_val)

        var cross_val: String = "не определено"
        var resistance: String = "--"
        var delta: String = "--"
        var percent: String = "--"
        var max_amp: String = "--"

        val powerkwt = _power?.toDouble()
        val power = powerkwt!! * 1000.0
        val lenght = _lenght?.toDouble()

        val copper: Copper = Copper()
        var amperage = copper.get_amperage(power!!)
        var copper_cross = copper.get_cross(amperage, lenght!!)
        if(copper_cross != 0.0){
            var resistance_copper = copper.get_resistance(lenght, copper_cross)
            var (copper_delta, copper_percent) = copper.get_deltaU(amperage, resistance_copper)
            var max_amper = copper.get_max_amperage(copper_cross)
            cross_val = copper_cross.toString()
            resistance = resistance_copper.toFloat().toString()
            delta = copper_delta.toFloat().toString()
            percent = copper_percent.toFloat().toString()
            max_amp = max_amper.toFloat().toString()
        }

        power_val.text = power.toString()
        lenght_val.text = lenght.toString()
        amper_val.text = amperage.toFloat().toString()
        cop_res_val.text = resistance
        cop_cross_val.text = cross_val
        cop_delta_val.text = delta
        cop_percent_val.text = percent
        max_amp_cop_val.text = max_amp

        cross_val = "не определено"
        resistance = "--"
        delta = "--"
        percent = "--"
        max_amp = "--"

        val alum: Alumin = Alumin()
        var al_cross = alum.get_cross(amperage, lenght!!)
        if(al_cross != 0.0){
            var resistance_al = alum.get_resistance(lenght, al_cross)
            var (al_delta, al_percent) = alum.get_deltaU(amperage, resistance_al)
            var max_amper = alum.get_max_amperage(al_cross)
            cross_val = al_cross.toString()
            resistance = resistance_al.toFloat().toString()
            delta = al_delta.toFloat().toString()
            percent = al_percent.toFloat().toString()
            max_amp = max_amper.toFloat().toString()
        }

        al_resist_val.text = resistance
        al_cross_val.text = cross_val
        al_max_amp_val.text = max_amp
        al_delta_val.text = delta
        al_percent_val.text = percent


    }



}