package com.adts.app

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adts.app.activity.Login
import com.adts.app.custom.ExceptionHandler
import com.adts.app.databinding.ActivitySplashScreenBinding
import com.adts.app.network.MyApp
import com.google.firebase.messaging.FirebaseMessaging

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var b: AlertDialog.Builder
    private val PERMISSION_REQUEST_CODE = 200
    private var isRunning = false
    private var thread: Thread? = null
    private var isLogin = false;
    private val myApp = MyApp(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        startSplash()

//startActivity(Intent(this, AllResources::class.java))
//        finish()
    }


    private fun permission() {
        if (!checkPermission()) {
            requestPermission()
        } else {
            doFinish()
//            getData()
        }
    }

    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        val result1 =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        val result2 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        val result3 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CALL_PHONE)
        val result4 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_CONTACTS)
        val result5 =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val result6 =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val result7 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED && result6 == PackageManager.PERMISSION_GRANTED && result7 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
            ), PERMISSION_REQUEST_CODE
        )
    }


    private fun doFinish() {
        val isLogin = myApp.getSharedPrefBoolean(MyApp.isLogin)

        if (isLogin) {
            val `in` = Intent(this, MainActivity::class.java)
            startActivity(`in`)
            finish()
        } else {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }

//    private fun getData() {
//        try {
//            var linkS: String? = ""
//            if (intent.data.toString().contains("?")) {
//                Link = intent.data.toString().substring(intent.data.toString().indexOf("?"))
//                linkS = intent.data.toString().replace(Link, "")
//            } else {
//                linkS = if (intent.data == null) {
//                    intent.toString()
//                } else {
//                    intent.data.toString()
//                }
//            }
//            FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(Uri.parse(linkS))
//                .addOnSuccessListener(
//                    this
//                ) { pendingDynamicLinkData -> // Get deep link from result (may be null if no link is found)
//                    var deepLink: Uri? = null
//                    if (pendingDynamicLinkData != null) {
//                        deepLink = pendingDynamicLinkData.link
//                    }
//                    if (deepLink != null) {
//                        postId = deepLink.getQueryParameter("postId")!!
//
//                    }
//                    doFinish()
//                }
//                .addOnFailureListener(
//                    this
//                ) { doFinish() }
//        } catch (e: Exception) {
//            doFinish()
//            e.printStackTrace()
//        }
//    }


    /**
     * Start splash.
     */
    private fun startSplash() {
        thread = Thread {
            isRunning = true
            try {
                val start = System.currentTimeMillis()
                val left = 2000 - (System.currentTimeMillis() - start)
                if (left > 100) Thread.sleep(left)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                val crash = intent
                    .getStringExtra(ExceptionHandler.CRASH_REPORT)
                if (crash != null) {
                    showCrashDialog(crash)
                } else {
//                    permission()
                    doFinish()
                }
            }
        }
        thread!!.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val locationCAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED
                val callAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED
                val contactAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[5] == PackageManager.PERMISSION_GRANTED
                val writeStorage = grantResults[6] == PackageManager.PERMISSION_GRANTED
                val recordAudio = grantResults[7] == PackageManager.PERMISSION_GRANTED
                if (locationAccepted && locationCAccepted && cameraAccepted && callAccepted && contactAccepted && readStorage && writeStorage && recordAudio) {
//                    getData()
                    Log.e("TAG", "Permission Granted")
                    doFinish()
                } else {
                    Log.e("TAG", "Permission Denied")
                    showMessageOKCancel(
                        ""
                    ) { dialog, which -> }
                }
            }
        }
    }


    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(resources.getString(R.string.allow_all_permission))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes)
        ) { dialog, which ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.data = Uri.parse("package:$packageName")
                startActivity(i)
                finish()
            }
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.No)
        ) { dialog, which ->
            dialog.cancel()
            finish()
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }


    fun showCrashDialog(report: String) {
        b = AlertDialog.Builder(this)
        b.setTitle("App Crashed")
        b.setMessage(
            """
            Oops! The app crashed in 
            
            
            Model:${Build.MODEL}
            VERSION NAME:${BuildConfig.VERSION_NAME}
            VERSION CODE:${BuildConfig.VERSION_CODE}
            Manufacturer: ${Build.MANUFACTURER}
            Product: ${Build.PRODUCT}
            Version:${Build.VERSION.SDK_INT}
            
            due to below reason:
            
            $report
            """.trimIndent()
        )
        val ocl =
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/html"
                    i.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@logictrixtech.com"))
                    i.putExtra(
                        Intent.EXTRA_TEXT, """
     Oops! The app crashed in 
     
     
     Model:${Build.MODEL}
     VERSION NAME:${BuildConfig.VERSION_NAME}
     VERSION CODE:${BuildConfig.VERSION_CODE}
     Manufacturer: ${Build.MANUFACTURER}
     Product: ${Build.PRODUCT}
     Version:${Build.VERSION.SDK_INT}
     
     due to below reason:
     
     $report
     """.trimIndent()
                    )
                    i.putExtra(Intent.EXTRA_SUBJECT, "App Crashed")
                    this@SplashScreen.startActivity(Intent.createChooser(i, "Send Mail via:"))
                    finish()
                } else {
                   doFinish()
                }
                dialog.dismiss()
            }
        b.setCancelable(false)
        b.setPositiveButton("Send Report", ocl)
        b.setNegativeButton("Restart", ocl)
        runOnUiThread { b.create().show() }
    }


}