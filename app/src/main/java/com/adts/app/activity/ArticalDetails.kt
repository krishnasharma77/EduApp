package com.adts.app.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adts.app.databinding.ActivityArticalDetailsBinding
import com.adts.app.model.DataXXX
import com.adts.app.model.GetArticleModel
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import com.bumptech.glide.Glide
import retrofit2.Response


class ArticalDetails : AppCompatActivity(), ApiCallback {

    private lateinit var binding: ActivityArticalDetailsBinding
    private lateinit var articalDetail: DataXXX
    private val myApp = MyApp(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        articalDetail = intent.getSerializableExtra("myArticalDetails") as DataXXX
        onClick()

        binding.articalName.text = articalDetail.ArticleName

        try {
            val htmlString = articalDetail.ArticleDescription.replace("\\<.*?\\>", "")
            binding.decContain.text = Html.fromHtml(htmlString)
        }catch (e:Exception){

        }

        val link = articalDetail.ArticleLink

        if (link.isNotEmpty())
        {
         binding.Hyperlink.setOnClickListener {
             val i = Intent(Intent.ACTION_VIEW)
             i.data = Uri.parse(link)
             startActivity(i)
         }
        }
        else{
            binding.Hyperlink.visibility = View.GONE

        }
        try {
            if (articalDetail.ArticleImage.isNotEmpty()) {
                val img = articalDetail.ArticleImage
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .replace("\\", "")
                Glide.with(this).load(img).into(binding.imgArtical)
            }
        }catch (e:Exception){

        }


    }



//        if (articalDetail.ArticleLink.isNotEmpty()){
//            val str_links = binding.Hyperlink.text.toString() + " " + articalDetail.ArticleLink
//            binding.Hyperlink.text = str_links
//            binding.Hyperlink.setLinkTextColor(Color.BLUE)
//            binding.Hyperlink.movementMethod = LinkMovementMethod.getInstance()
//
//
//        } else {
//            binding.Hyperlink.visibility = View.GONE
//        }


    private fun onClick() {
        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSuccess(type: String, data: Any?) {
        myApp.dismisAlrtDialog()
        if (type == "getArticle") {
            val response: Response<GetArticleModel> = data as Response<GetArticleModel>
            if (response.isSuccessful) {
                if (response.body()!!.message == "successfully Apply") {
                    Toast.makeText(this, "Successfully Applied this Course", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onFailure(data: Any?) {
        myApp.dismisAlrtDialog()
    }


}