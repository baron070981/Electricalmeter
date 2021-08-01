package com.example.electricalmeter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Matrix
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.view.Surface
import android.view.TextureView
import androidx.annotation.RequiresApi
import java.util.*


class CameraData {
    var cameraManager: CameraManager? = null
    var cameraDevice: CameraDevice? = null
    var texture: TextureView? = null
    var ID_BACK_CAM: String = "0"
    var ID_FRONT_CAM: String = "1"
    val NO_CAMERA: String = "-1"
    var captureSession: CameraCaptureSession? = null
    var backgroundHandler: Handler? = null
    val STATE_PREVIEW = 0
    var state = STATE_PREVIEW
    lateinit var activity: Activity

    fun set_texture(view: TextureView){
        this.texture = view
    }

}

val cameradata = CameraData()


class CameraService(cameraManager: CameraManager, cameraId: String="0", activity: Activity){

    var cameraId:String
    init {
        cameradata.cameraManager = cameraManager
        this.cameraId = cameraId
        cameradata.activity = activity
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun get_camera_idlist(): Pair<String, String> {
        if (cameradata.cameraManager == null){
            return Pair(cameradata.NO_CAMERA, cameradata.NO_CAMERA)
        }
        var all_cameras = cameradata.cameraManager!!.cameraIdList
        for(camId in all_cameras){
            var cam_char = cameradata.cameraManager!!.getCameraCharacteristics(camId)
            var CAM = cam_char.get(CameraCharacteristics.LENS_FACING)
            if(CAM == CameraCharacteristics.LENS_FACING_BACK){
                cameradata.ID_BACK_CAM = camId
            }else if(CAM == CameraCharacteristics.LENS_FACING_FRONT){
                cameradata.ID_FRONT_CAM = camId
            }
        }
        return Pair(cameradata.ID_BACK_CAM, cameradata.ID_FRONT_CAM)
    }


    fun is_open(): Boolean{
        if(cameradata.cameraDevice == null){
            return false
        }
        return true
    }

    fun getRotateOrientation(): Int {
        val rotate: Int = cameradata.activity.getWindowManager().getDefaultDisplay().getRotation()
        return when (rotate) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 270
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 90
            else -> 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    fun open_camera(){
        cameradata.cameraManager?.openCamera(this.cameraId, stateCallback, cameradata.backgroundHandler)
    }

    var stateCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object: CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameradata.cameraDevice = camera
            createCameraPreviewSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            close()
        }

    }

    fun transformTexture(textureView: TextureView) {
        var displayRotation = getRotateOrientation()

        val adjustment = Matrix()

        var centerX: Float
        var centerY: Float
        centerX = textureView.width.toFloat()/ 2f
        centerY = textureView.height.toFloat() / 2f

        var scalex: Float = 1.0f
        var scaley: Float = 1.0f
        if(displayRotation == Surface.ROTATION_0){
            scalex = textureView.width.toFloat() / textureView.height.toFloat() * 2f
            scaley = textureView.height.toFloat() / textureView.width.toFloat() / 2f
        }else {
            scalex = textureView.width.toFloat() / textureView.height.toFloat()
            scaley = textureView.height.toFloat() / textureView.width.toFloat()
        }

        adjustment.postRotate(displayRotation.toFloat(), centerX, centerY)
        adjustment.postScale(scalex, scaley, centerX, centerY)

        textureView.setTransform(adjustment)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun createCameraPreviewSession(){
        transformTexture(cameradata.texture!!)
        var texture = cameradata.texture?.surfaceTexture
        var surface = Surface(texture)


        try {
            var builder = cameradata.cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            builder!!.addTarget(surface)
            cameradata.cameraDevice?.createCaptureSession(Arrays.asList(surface), object: CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameradata.captureSession = session
                    try{
                        cameradata.captureSession!!.setRepeatingRequest(builder.build(),null,null)
                    }catch (e: Exception){
                        return
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    TODO("Not yet implemented")
                }

            }, null)
        }catch (e: Exception){
            return
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun close(){
        if(cameradata.cameraDevice != null){
            cameradata.cameraDevice!!.close()
            cameradata.cameraDevice = null
        }
    }

}

class CounterTime{

    var counter_imp: Int = 0 // счетчик импульсов
    private var counter_second: Int = 1 // счетчик секунд
    private var interval_pulses: Int = 0 // время между импульсами
    private var timev = 0 // общее время для заданого числа импульсов
    private var STATE: Boolean = true

    // изменение состояния
    fun set_state(): Boolean{
        STATE = !STATE
        return STATE
    }

    // установка значения времени для всего кол-ва импульсов
    fun set_time(t: Int){
        timev = t
    }

    // получене сотояния
    fun state(): Boolean{
        return STATE
    }

    // установка значения интервала между импульсами
    fun set_interval_pulses(interval_pulses: Int){
        this.interval_pulses = interval_pulses
    }

    // сброс счетчика секунд
    fun reset_second(reset: Int = 1){
        counter_second = reset
    }

    // сброс счетчика импульсов
    fun reset_counter_imp(reset: Int = 0){
        counter_imp = reset
    }

    // сравнение прошедшего времени с интервалом между импульсами
    fun check_interval(): Boolean{
        if(counter_second >= interval_pulses
        ){
            return true
        }
        return false
    }

    // проверка прошедшего времени
    fun check_time(): Boolean{
        if(counter_second >= timev){
            return true
        }
        return false
    }

    // прибавление секунды к счетчику секунд
    fun add_second(num: Int= 1){
        counter_second += num
    }

    // получить значение счетчика импульсов
    fun get_count_imp(): String{
        return counter_imp.toString()
    }

    // получить значение счетчика секунд
    fun get_count_second(): String{
        return counter_second.toString()
    }

}
















