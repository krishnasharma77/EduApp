package com.adts.app.network

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.viewbinding.BuildConfig
import com.adts.app.R
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat


class MyApp(val context: Context) {


    companion object {

        private lateinit var dialog: ProgressDialog
        private lateinit var spotsDialog: SpotsDialog

        var SHARED_PREF_NAME = "EduApp"
        var isLogin = "Login"
        var LOGIN_FROM = "where"
        var USER_ID = "userId"
        var DEVICE_ID = "deviceId"
        var USER_NAME = "nickName"
        var USER_Email = "userEmail"
        var USER_Number = "userNumber"

    }



    fun spinnerStop() {
        try {

            if (dialog.isShowing) {
                dialog.dismiss()

            }
        } catch (e: Exception) {

        }

    }

    fun spinnerStart(context: Context){
        dialog = ProgressDialog(context)
        dialog.setMessage(context.resources.getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(time: String): String {
        var timeDate = time
        if (timeDate.contains("."))
            timeDate = timeDate.substring(0, timeDate.indexOf("."))
        val format = SimpleDateFormat("hh:mm")
        val current = format.format(timeDate.toLong())
        // current = current.replace("/", "-")
        return current

    }

    fun getDate(date: String): String {
        return try {
            var tempDate = date
            if (tempDate.contains("(") && tempDate.contains(")"))
                tempDate = tempDate.substring(tempDate.indexOf("(") + 1, tempDate.indexOf(")"))
            val format = SimpleDateFormat("dd/MM/yyyy")
            val current = format.format(tempDate.toLong())
            current
        } catch (e: Exception) {
            ""
        }

    }

    fun getTimeFromDate(date: String): String {
        return try {
            var tempDate = date
            if (tempDate.contains("(") && tempDate.contains(")"))
                tempDate = tempDate.substring(tempDate.indexOf("(") + 1, tempDate.indexOf(")"))
            val format = SimpleDateFormat("hh:mm")
            val current = format.format(tempDate.toLong())
            current
        } catch (e: Exception) {
            ""
        }

    }

    fun getAppFolder(activity: Context): String? {
        return activity.getExternalFilesDir(null)?.path.toString() + "/EduApp/"
    }

    fun showAlrtDialg(context: Context?, msg: String?) {
        spotsDialog = SpotsDialog(context, msg, R.style.Custom_white)
        spotsDialog.setCancelable(false)
        spotsDialog.show()
    }


    fun dismisAlrtDialog() {
        if (spotsDialog != null) spotsDialog.dismiss()
    }


    @SuppressLint("MissingPermission")
    fun isConnectingToInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun isValidLatLng(lat: Double?, lng: Double?): Boolean {
        lat ?: return false
        lng ?: return false
        if (lat < -90 || lat > 90) {
            return false
        } else if (lng < -180 || lng > 180) {
            return false
        }
        return true
    }



    fun ShowMassage(ctx: Context, msg: String) {
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(null).setMessage(msg)
            .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }

        val alert = builder.create()
        alert.show()
    }

    fun ShowMassage1(ctx1: Context, msg1: String) {
        Toast.makeText(ctx1, "" + msg1, Toast.LENGTH_SHORT).show()
    }


    fun getSharedPrefLong(preffConstant: String): Long {
        var longValue: Long = 0
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        longValue = sp.getLong(preffConstant, 0)
        return longValue
    }

    fun setSharedPrefLong(preffConstant: String, longValue: Long) {
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putLong(preffConstant, longValue)
        editor.commit()
    }

    fun setSharedPrefBoolean(preffConstant: String, value: Boolean) {
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putBoolean(preffConstant, value)
        editor.commit()
    }

    fun getSharedPrefBoolean(preffConstant: String): Boolean {
        var stringValue = false
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        stringValue = sp.getBoolean(preffConstant, false)
        return stringValue
    }

    fun getSharedPrefString(preffConstant: String): String {
        var stringValue: String? = ""
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        stringValue = sp.getString(preffConstant, "")
        return stringValue ?: ""
    }

    fun setSharedPrefString(
        preffConstant: String,
        stringValue: String
    ) {
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putString(preffConstant, stringValue)
        editor.commit()
    }

    fun getSharedPrefInteger(preffConstant: String): Int {
        var intValue = 0
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        intValue = sp.getInt(preffConstant, 0)
        return intValue
    }

    fun setSharedPrefInteger(preffConstant: String, value: Int) {
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putInt(preffConstant, value)
        editor.commit()
    }

    fun getSharedPrefFloat(preffConstant: String): Float {
        var floatValue = 0f
        val sp = context.getSharedPreferences(
            preffConstant, 0
        )
        floatValue = sp.getFloat(preffConstant, 0f)
        return floatValue
    }

    fun setSharedPrefFloat(preffConstant: String, floatValue: Float) {
        val sp = context.getSharedPreferences(
            preffConstant, 0
        )
        val editor = sp.edit()
        editor.putFloat(preffConstant, floatValue)
        editor.commit()
    }


    fun getStatus(name: String): Boolean {
        val status: Boolean
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        status = sp.getBoolean(name, false)
        return status
    }

    fun setStatus(name: String, istrue: Boolean) {
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.putBoolean(name, istrue)
        editor.commit()
    }

    fun logout()
    {
        val sp = context.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        val editor = sp.edit()
        editor.clear()
        editor.commit()
    }



    fun getRealPathFromURI(context: Activity, contentUri: Uri?): String? {

        val filePathColumn = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
        )
        if (BuildConfig.DEBUG && contentUri == null) {
            error("Assertion failed")
        }
        val cursor = contentUri?.let {
            context.contentResolver.query(
                it,
                filePathColumn, null, null, null
            )
        }
        cursor?.let {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val path = cursor.getString(columnIndex)
            cursor.close()
            return path
        }
        cursor!!.close()
        return ""
    }


}