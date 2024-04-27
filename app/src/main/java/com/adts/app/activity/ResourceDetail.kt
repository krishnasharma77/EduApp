package com.adts.app.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.text.Html
import com.bumptech.glide.Glide
import com.adts.app.databinding.ActivityResourceDetailBinding
import com.adts.app.model.DataXX

class ResourceDetail : AppCompatActivity() {
    private lateinit var binding: ActivityResourceDetailBinding
    var resourseDetail: DataXX? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resourseDetail = intent.getSerializableExtra("resourseDetail") as DataXX
        onClick()

        binding.coursename.text = resourseDetail!!.ResourseName
//        binding.date.text = resourseDetail!!.CreatedDate

        try {
            val htmlString = resourseDetail!!.ResourseDescription.replace("\\<.*?\\>", "");
            binding.decContain.text = Html.fromHtml(htmlString)
        } catch (e: Exception) {

        }

        try {
            if (resourseDetail!!.ResourseImage.isNotEmpty()) {
                val img = resourseDetail!!.ResourseImage
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .replace("\\", "")
                Glide.with(this).load(img).into(binding.imgCourse)
            }
        }catch (e:Exception){

        }
    }
    private fun onClick() {
        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }
        binding.buy.setOnClickListener {
            val vibration = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibration.vibrate(100);
           // makePayment()
        }
    }
}