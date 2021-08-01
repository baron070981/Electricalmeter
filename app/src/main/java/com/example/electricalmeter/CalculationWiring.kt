@file:Suppress("DEPRECATION")

package com.example.electricalmeter

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_VISIBLE
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import calculate.validate

class CalculationWiring : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_calculation_wiring)
        var resbtn = findViewById<Button>(R.id.reswiring)
        resbtn.setOnClickListener{
            get_data_from_input()
        }
        var button_home = findViewById<Button>(R.id.button_home)
        var button_back = findViewById<Button>(R.id.button_back)

        button_home.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }

        button_back.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }
    }

    fun get_data_from_input() {
        var _power = findViewById<EditText>(R.id.powerwire)
        var _lenghtwire = findViewById<EditText>(R.id.lengthwire)

        var power = _power.text.toString()
        var lenwire = _lenghtwire.text.toString()

        if(validate(power) || validate(lenwire)){
            Toast.makeText(this, "Заполнить поля", Toast.LENGTH_LONG).show()
            return
        }
        val intent_wire = Intent(this, WiringInfo::class.java)

        intent_wire.putExtra("power", power)
        intent_wire.putExtra("lenght", lenwire)
        startActivity(intent_wire)
    }

}