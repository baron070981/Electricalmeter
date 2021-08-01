package com.example.electricalmeter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class CameraActivity : Activity() {

    var STATE: Boolean = false
    var SECOND_COUNTER_STATE: Boolean = false
    val counttime = CounterTime()
    val PERMISSION_CAMERA_CODE = 1
    val PERMISSIONS_ARRAY = arrayOf(Manifest.permission.CAMERA)
    var PERMISSION_STATE = false
    val over_limit_indicator by lazy{ findViewById<TextView>(R.id.limit_impulse_id) }
    val start_button by lazy{ findViewById<Button>(R.id.start_timer_button)}

    lateinit var camera: CameraService
    lateinit var button_back: Button
    lateinit var button_home: Button
    var fulltime: Int? = null
    var intervalimp: Int? = null
    var intent_name: String? = null


    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN)
        setContentView(R.layout.activity_camera)

        var ORIENT = this.resources.configuration.orientation // получение ориентации

        val texture = findViewById<TextureView>(R.id.cameraview_id)
        val main_timer = findViewById<Chronometer>(R.id.timer_id)
        val over_limit_timer = findViewById<Chronometer>(R.id.limit_timer_id)

        over_limit_indicator.setText(R.string.indicator_false)
        main_timer.setText(R.string.timer_00)
        over_limit_timer.setText(R.string.timer_00)
        start_button.setBackgroundResource(R.mipmap.play_button)

        fulltime = intent.getStringExtra("time")?.toInt()
        intervalimp = intent.getStringExtra("interval")?.toInt()
        intent_name = intent.getStringExtra("name").toString()

        cameradata.set_texture(texture)
        counttime.set_time(fulltime!!) //
        counttime.set_interval_pulses(intervalimp!!) //

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        camera = CameraService(cameraManager, "0", this)
        //
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_ARRAY, PERMISSION_CAMERA_CODE)
            PERMISSION_STATE = true
        } else {
            PERMISSION_STATE = true
        }
        if (!PERMISSION_STATE) {
            return
        }
        camera.open_camera()
        camera.getRotateOrientation()

        over_limit_timer.setOnChronometerTickListener {
            over_limit_timer.format = "%s"
        }

        main_timer.setOnChronometerTickListener {
            main_timer.format = "%s"
            if(!SECOND_COUNTER_STATE) {
                counttime.add_second()
            }
            if(counttime.check_interval()) {
                over_limit_indicator.setText(R.string.indicator_true)
            }
            if(counttime.check_time() && !STATE){
                Toast.makeText(this, "CHECK TIME", Toast.LENGTH_LONG)
                STATE = true
                SECOND_COUNTER_STATE = true
                counttime.reset_second()
                main_timer.setBackgroundResource(R.color.limit_color)
                over_limit_timer.base = SystemClock.elapsedRealtime()
                over_limit_timer.start()
            }
        }

        start_timer(start_button, main_timer, over_limit_timer)

        if (ORIENT == Configuration.ORIENTATION_PORTRAIT) {
            button_home = findViewById<Button>(R.id.home_button_cam)
            button_back = findViewById<Button>(R.id.back_button_cam)
        } else if(ORIENT == Configuration.ORIENTATION_LANDSCAPE) {
            button_home = findViewById<Button>(R.id.home_button_cam_land)
            button_back = findViewById<Button>(R.id.back_button_cam_land)
        }

        to_home(button_home)
        to_back(button_back)

    }

    fun send_data(intentdata: Intent){
        intentdata.putExtra("time", fulltime.toString())
        intentdata.putExtra("interval", intervalimp.toString())
        intentdata.putExtra("name", intent_name.toString())
        startActivity(intentdata)
    }


    fun to_home(button: Button){
        button.setOnClickListener{
            var homeintent = Intent(this, MainActivity::class.java)
            startActivity(homeintent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun to_back(button: Button){
        button.setOnClickListener{
            var homeintent = Intent(this, InfoScreen::class.java)
            send_data(homeintent)
            startActivity(homeintent)
        }
    }

    fun start_timer(button: Button, chrono: Chronometer, _chrono_limit: Chronometer){
        button .setOnClickListener {
            if(counttime.state()){
                chrono.base = SystemClock.elapsedRealtime()
                chrono.start()
                chrono.setText(R.string.timer_00)
                chrono.setBackgroundResource(R.color.empty_bg)
                button.setBackgroundResource(R.mipmap.stop_button)
                counttime.set_state()
                counttime.reset_second(0)
                over_limit_indicator.setText(R.string.indicator_false)
            }else{
                SECOND_COUNTER_STATE = false
                STATE = false
                counttime.set_state()
                counttime.reset_second()
                over_limit_indicator.setText(R.string.indicator_false)
                button.setBackgroundResource(R.mipmap.play_button)
                chrono.stop()
                _chrono_limit.stop()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStop() {
        camera.close()
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPause() {
        camera.close()
        super.onPause()
    }


}