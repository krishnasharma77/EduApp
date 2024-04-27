package com.adts.app.activity

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.adts.app.databinding.ActivityPdfViewactivityBinding
import com.adts.app.network.MyApp
import com.github.barteksc.pdfviewer.PDFView
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PdfViewer : Activity() {
    private lateinit var binding: ActivityPdfViewactivityBinding

    private lateinit var pdf: PDFView
    private val myApp = MyApp(this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //secure Window Flag
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        pdf = binding.pdfView
        val pdfUrl = intent.getStringExtra("viewPdf")
        try {
            val fis = FileInputStream(File(pdfUrl))
            pdf.fromStream(fis).load()
/*
            RetrievePdfStream().execute(pdfUrl)
            myApp.showAlrtDialg(this,"")
*/
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load Url :$e", Toast.LENGTH_SHORT).show()
        }
    }

    internal inner class RetrievePdfStream : AsyncTask<String, Void, InputStream>() {
        override fun doInBackground(vararg strings: String): InputStream? {
            var inputStream: InputStream? = null
            try {
                val url = URL(strings[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: IOException) {
                myApp.dismisAlrtDialog()
                return null
            }
            return inputStream
        }

        override fun onPostExecute(inputStream: InputStream?) {
            pdf.fromStream(inputStream).load()
            myApp.dismisAlrtDialog()
        }
    }
}