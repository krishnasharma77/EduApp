package com.adts.app.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.adts.app.adapter.ChapterAdapter
import com.adts.app.databinding.ActivityMyCourseDetailsBinding
import com.adts.app.model.CChapter
import com.adts.app.model.DataXXXX
import com.adts.app.network.MyApp
import com.bumptech.glide.Glide
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import java.io.File

class MyCourseDetails : AppCompatActivity() {
    private lateinit var binding: ActivityMyCourseDetailsBinding
    private lateinit var courseDetail: DataXXXX
    private val myApp = MyApp(this)
    private lateinit var chapterAdapter: ChapterAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        courseDetail = intent.getSerializableExtra("myCourseDetail") as DataXXXX
        onClick()
        binding.mycoursename.text = courseDetail.CourseName
        binding.duration.text = courseDetail.CourseDuration
        try {
            val htmlString = courseDetail.CourseDescription.replace("\\<.*?\\>", "")
            binding.decContain.text = Html.fromHtml(htmlString)
        }
        catch (e:Exception){

        }

        try {
            if (courseDetail.CourseImage.isNotEmpty()) {
                val img = courseDetail.CourseImage
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .replace("\\", "")
                Glide.with(this).load(img).into(binding.imgCourse)
            }
        }catch (e:Exception){

        }

        setChapter()
    }

    private fun onClick() {
        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }
    }


    private fun setChapter() {
        chapterAdapter =
            ChapterAdapter(this, courseDetail.cChapters, object : ChapterAdapter.ClickListener {
                override fun clicked(view: View, posi: CChapter, position: Int, type: String) {
                    if (type == "PDF") {
                        val pdfurl = courseDetail.cChapters[position].ChapterPdf
                        dowloandPdf(pdfurl)
                    } else {
                        startActivity(
                            Intent(
                                this@MyCourseDetails,
                                PlayYoutubeVideos::class.java
                            ).putExtra("showVideo", courseDetail.cChapters[position].ChapterVideo)
                        )

                    }
                }
            })
        binding.myChapterRv.adapter = chapterAdapter

    }



    private fun dowloandPdf(pdfUrl: String) {

        PRDownloader.initialize(applicationContext);
        val appPath: String = this.filesDir.absolutePath
        Log.e("",appPath)

        val downloadId = PRDownloader.download(pdfUrl, appPath, "chapter.pdf")
            .build()
            .setOnStartOrResumeListener {
                myApp.showAlrtDialg(this,"")
            }
            .setOnPauseListener { }
            .setOnCancelListener { }
            .setOnProgressListener {
                Log.e("TAG", "dowloandPdf: $it")
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    myApp.dismisAlrtDialog()
                    startActivity(Intent(
                        this@MyCourseDetails,
                        PdfViewer::class.java
                    ).putExtra("viewPdf", appPath + File.separator + "chapter.pdf")
                    )
                }
                override fun onError(error: Error?) {
                    myApp.dismisAlrtDialog()
                }
            } )

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