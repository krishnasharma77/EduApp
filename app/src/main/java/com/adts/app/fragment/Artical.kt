package com.adts.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adts.app.activity.ArticalDetails
import com.adts.app.adapter.ArticleAdapter
import com.adts.app.databinding.FragmentArticalBinding
import com.adts.app.model.DataXXX
import com.adts.app.model.GetArticleModel
import com.adts.app.network.ApiCall
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import retrofit2.Response

class Artical : Fragment(),ApiCallback{

    private lateinit var binding: FragmentArticalBinding
    private var articalList: ArrayList<DataXXX> = ArrayList()
    private lateinit var articleAdapter: ArticleAdapter
    private val myApp by lazy { MyApp(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticalBinding.inflate(inflater,container,false)

//        val bundle = Bundle()
//        articalDetail = bundle.getSerializable("articalDesc") as DataXXX
//        articalDetail = intent.getSerializableExtra("articalDesc") as DataXXX
        callApi()

        return (binding.root)
    }

    private fun callApi() {
        myApp.dismisAlrtDialog()
        ApiCall.instance?.getArticle(this)
    }


    override fun onSuccess(type: String, data: Any?) {
        myApp.dismisAlrtDialog()
        if (type=="getArticle")
        {
            val response:Response<GetArticleModel> = data as Response<GetArticleModel>
            if (response.isSuccessful)
            {
                articalList = ArrayList()
                articalList.addAll(response.body()!!.data)
                articleAdapter = ArticleAdapter(requireContext(),articalList,object: ArticleAdapter.ClickListener{
                    override fun clicked(view: View, posi: DataXXX) {
                        startActivity(Intent(activity, ArticalDetails::class.java).putExtra("myArticalDetails",posi))

                    } })
                binding.articalRv.adapter = articleAdapter
            }
        }

    }

    override fun onFailure(data: Any?) {
        myApp.dismisAlrtDialog()

    }

}