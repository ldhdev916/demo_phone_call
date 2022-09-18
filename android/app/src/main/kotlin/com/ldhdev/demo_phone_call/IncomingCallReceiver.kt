package com.ldhdev.demo_phone_call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

class IncomingCallReceiver : BroadcastReceiver() {

    companion object {
        private var view: View? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) ?: return

        // 테스트 용으로 OFFHOOK 상태에서만 동작하도록 함
        when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
            TelephonyManager.EXTRA_STATE_OFFHOOK -> context.createOverlay(phoneNumber)
            TelephonyManager.EXTRA_STATE_IDLE -> context.removeOverlay()
        }
    }

    private fun Context.removeOverlay() {
        view?.let {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeView(it)
            view = null
        }
    }

    private fun Context.executeEntrypoint(engine: FlutterEngine, phoneNumber: String) {
        val flutterLoader = FlutterInjector.instance().flutterLoader()
        if (!flutterLoader.initialized()) {
            flutterLoader.startInitialization(applicationContext)
        }

        flutterLoader.ensureInitializationComplete(applicationContext, null)

        val entrypoint = DartExecutor.DartEntrypoint(
            flutterLoader.findAppBundlePath(),
            "overlayMain"
        )

        engine.dartExecutor.executeDartEntrypoint(entrypoint, listOf(phoneNumber))
    }


    private fun Context.createOverlay(phoneNumber: String) {

        val engine = FlutterEngine(this)
        executeEntrypoint(engine, phoneNumber)

        engine.lifecycleChannel.appIsResumed()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            400,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP
            y = 500
        }


        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        view = FlutterView(applicationContext, FlutterTextureView(applicationContext)).apply {
            attachToFlutterEngine(engine)
            fitsSystemWindows = true
            isFocusable = true
            isFocusableInTouchMode = true
            setBackgroundColor(Color.TRANSPARENT)
        }

        windowManager.addView(view, params)
    }
}