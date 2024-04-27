package com.adts.app.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.adts.app.R
import com.adts.app.adapter.ChapterAdapter
import com.adts.app.databinding.ActivityCourseDetailsBinding
import com.adts.app.model.CChapter
import com.adts.app.model.DataX
import com.adts.app.model.PurchaseCourseModel
import com.adts.app.model.RegistrationModel
import com.adts.app.network.ApiCall
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import com.bumptech.glide.Glide
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import retrofit2.Response
import java.io.File


class CourseDetails : AppCompatActivity(), PaymentResultWithDataListener, ApiCallback {
    private lateinit var binding: ActivityCourseDetailsBinding
    var courseDetail: DataX? = null
    private val myApp = MyApp(this)
    private lateinit var chapterAdapter: ChapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        courseDetail = intent.getSerializableExtra("courseDetail") as DataX

        Checkout.preload(applicationContext)

        onClick()

        if (courseDetail!!.IsBuy) {
            binding.myChapterRv.visibility = View.VISIBLE
            binding.buy.visibility = View.GONE
//            setChapter()
        } else {
            binding.myChapterRv.visibility = View.GONE
            binding.buy.visibility = View.VISIBLE
        }

        binding.coursename.text = courseDetail!!.CourseName
        binding.hrs.text = courseDetail!!.CourseDuration
        binding.level.text = courseDetail!!.CoursePrice

        try {
            val courseDesc: String = courseDetail!!.CourseDescription
            if (courseDesc.isNotEmpty()) {
                val htmlString = courseDetail!!.CourseDescription.replace("\\<.*?\\>", "")
                binding.decContain.text = Html.fromHtml(htmlString)
            }

        } catch (e: Exception) {

        }

        try {
            if (courseDetail!!.CourseImage != null) {
                Glide.with(this).load(courseDetail!!.CourseImage).into(binding.imgCourse)
            }
        } catch (e: Exception) {

        }

    }

    private fun onClick() {
        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }


        binding.buy.setOnClickListener {
            val price: String = courseDetail!!.CoursePrice
            val vibration = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibration.vibrate(100)
            if (price > 1.toString()) {
                alertDialog()

            } else {
                val userId = MyApp(this).getSharedPrefInteger(MyApp.USER_ID).toString()
                val courseId = courseDetail?.CourseId.toString()
                ApiCall.instance?.applyCourse(userId, courseId, this)
            }
        }
    }

    private fun alertDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setMessage("Please don't press back button after starting payment Process..")
        dialog.setPositiveButton("Ok") { dialogInterface, i ->
            dialogInterface.dismiss()
            makePayment()
        }
        dialog.show()
    }
    private fun applyCourse(paymentId: String, orderId: String) {
        val userId = MyApp(this).getSharedPrefInteger(MyApp.USER_ID).toString()
        val courseId = courseDetail?.CourseId.toString()
        myApp.showAlrtDialg(this, "")
        ApiCall.instance?.applyCourse(userId, courseId, this)
        ApiCall.instance?.PurchaseCourse(
            paymentId,
            "",
            orderId,
            binding.level.text.toString(),
            "success",
            courseId,
            userId,
            "success",
            this
        )
    }

    private fun makePayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        val email = myApp.getSharedPrefString(MyApp.USER_Email)
        val mobile = myApp.getSharedPrefString(MyApp.USER_Number)
        val amount = courseDetail!!.CoursePrice.toDouble() * 100

        try {
            val options = JSONObject()
            options.put("name", this.resources.getString(R.string.app_name))
            options.put("description", "Course Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR")
            options.put("amount", "" + amount)
            options.put("send_sms_hash", true)

            val prefill = JSONObject()
            val checkout = Checkout()
            checkout.setKeyID("rzp_live_1vguroTydtTnpo")
//            checkout.setKeyID("rzp_test_BQaHPWD5yCNx0h")
            prefill.put("email", email)
            prefill.put("contact", mobile)

            options.put("prefill", prefill)
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, PaymentData: PaymentData) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()
        applyCourse(PaymentData.paymentId, "")
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        try {
            val json = JSONObject(p1)

            var description = ""
            description = if (p0 == 3) {
                json.getString("description")

            } else {
                val error = json.getJSONObject("error")
                error.getString("description")
            }

            Toast.makeText(this, description, Toast.LENGTH_LONG).show()

            val userId = MyApp(this).getSharedPrefInteger(MyApp.USER_ID).toString()
            val courseId = courseDetail?.CourseId.toString()
            ApiCall.instance?.PurchaseCourse(
                "",
                "",
                "",
                binding.level.text.toString(),
                "failure",
                courseId,
                userId,
                description,
                this
            )

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Payment has been Decline by the Bank / User back the payment method  ",
                Toast.LENGTH_LONG
            ).show()
            Log.e("TAG", "onPaymentError: $e")
        }
    }

    override fun onSuccess(type: String, data: Any?) {
        myApp.dismisAlrtDialog()
        if (type == "buyCourse") {
            val response: Response<RegistrationModel> = data as Response<RegistrationModel>
            if (response.isSuccessful) {
                if (response.body()!!.message == "successfully Apply") {
                    MyApp(this).getSharedPrefInteger(MyApp.USER_ID).toString()
                    Toast.makeText(this, "Successfully Applied this Course", Toast.LENGTH_SHORT)
                        .show()
                    binding.myChapterRv.visibility = View.VISIBLE
                    binding.buy.visibility = View.GONE
//                    setChapter()
                }
            }
        } else if (type == "Purchase_Course") {
            val response: Response<PurchaseCourseModel> = data as Response<PurchaseCourseModel>
            if (response.isSuccessful) {
                if (response.body()!!.message == "success") {

                }
            }
        }
    }

    override fun onFailure(data: Any?) {
        myApp.dismisAlrtDialog()
    }

/*    private fun setChapter() {
        chapterAdapter =
            ChapterAdapter(this, courseDetail!!.cChapters, object : ChapterAdapter.ClickListener {
                override fun clicked(view: View, posi: CChapter, position: Int, type: String) {
                    if (type == "PDF") {
                        val pdfurl = courseDetail!!.cChapters[position].ChapterPdf
                        dowloandPdf(pdfurl)
                    } else {
                        startActivity(
                            Intent(
                                this@CourseDetails,
                                PlayYoutubeVideos::class.java
                            ).putExtra("showVideo", courseDetail!!.cChapters[position].ChapterVideo)
                        )

                    }

                }
            })
        binding.myChapterRv.adapter = chapterAdapter

    }*/


    private fun dowloandPdf(pdfUrl: String) {

        PRDownloader.initialize(applicationContext)
        val appPath: String = this.filesDir.absolutePath
        Log.e("", appPath)

        val downloadId = PRDownloader.download(pdfUrl, appPath, "chapter.pdf")
            .build()
            .setOnStartOrResumeListener {
                myApp.showAlrtDialg(this, "")
            }
            .setOnPauseListener { }
            .setOnCancelListener { }
            .setOnProgressListener {
                Log.e("TAG", "dowloandPdf: $it")
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    myApp.dismisAlrtDialog()
                    startActivity(
                        Intent(
                            this@CourseDetails,
                            PdfViewer::class.java
                        ).putExtra("viewPdf", appPath + File.separator + "chapter.pdf")
                    )
                }

                override fun onError(error: Error?) {
                    myApp.dismisAlrtDialog()
                }
            })

/*


        try {
            var downloadManager: DownloadManager? = null
            downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloaduri: Uri = Uri.parse(pdfUrl)
            val request: DownloadManager.Request = DownloadManager.Request(downloaduri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setMimeType("pdf")
                .setDestinationInExternalFilesDir(this@CourseDetails,filesDir.absolutePath,File.separator.toString() + "chapter " + ".pdf")
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            downloadManager.enqueue(request)
            myApp.dismisAlrtDialog()
            Toast.makeText(this,"pdf start downloading",Toast.LENGTH_LONG).show()
        } catch (e: java.lang.Exception) {
        }
*/


    }


}