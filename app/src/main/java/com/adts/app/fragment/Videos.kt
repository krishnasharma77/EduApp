package com.adts.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.adts.app.adapter.VideosAdapter
import com.adts.app.databinding.FragmentVideosBinding
import com.adts.app.model.DataXX
import com.adts.app.model.GetAllResourcesModel
import com.adts.app.network.ApiCall
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import com.google.android.exoplayer2.SimpleExoPlayer
import retrofit2.Response

class Videos : Fragment(), ApiCallback {
    private lateinit var binding: FragmentVideosBinding
    private var videosList: ArrayList<DataXX> = ArrayList()
    var temp_layout: ScrollView? = null
    var mViewPager: ViewPager? = null
    private lateinit var videosAdapter: VideosAdapter
    private val myApp by lazy { MyApp(requireContext()) }
    private lateinit var exoPlayer: SimpleExoPlayer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideosBinding.inflate(inflater, container, false)
        callApi()
        return (binding.root)
    }

  /*  override fun onResume() {
        super.onResume()

        val scrollBounds = Rect()
        temp_layout?.getHitRect(scrollBounds)

        temp_layout?.getViewTreeObserver()
            ?.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
                if (mViewPager != null) {
                    if (mViewPager!!.getLocalVisibleRect(scrollBounds)) {
                        if (!mViewPager!!.getLocalVisibleRect(scrollBounds) || scrollBounds.height() < mViewPager!!.getHeight()) {
                            Log.i("TAG", "BTN APPEAR PARCIALY")
                        } else {
                            Log.i("TAG", "BTN APPEAR FULLY!!!")
                        }
                    } else {
                        videosAdapter.pauseVideo()
                        Log.i("TAG", "No")
                    }
                }
            })
    }*/


    /* override fun onPause() {


 //        videosAdapter.releaseVideo()
         Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show()
         super.onPause()
     }*/


    private fun callApi() {
        myApp.dismisAlrtDialog()
        ApiCall.instance?.getResources(this)
    }


    override fun onSuccess(type: String, data: Any?) {
        myApp.dismisAlrtDialog()
        if (type == "allresourses") {
            val response: Response<GetAllResourcesModel> = data as Response<GetAllResourcesModel>
            if (response.isSuccessful) {
                videosList = ArrayList()
                videosList.addAll(response.body()!!.data)
                videosAdapter = VideosAdapter(requireContext(), videosList)
                binding.videosRv.adapter = videosAdapter
            }
        }

    }

    override fun onFailure(data: Any?) {
        TODO("Not yet implemented")
    }

    /*override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) {
           try {
               videosAdapter.pauseVideo()
           }catch (e:Exception){}
        }

    }

    override fun onFailure(data: Any?) {
        myapp.dismisAlrtDialog()
    }

    override fun onStop() {
        super.onStop()
        try {
            videosAdapter.pauseVideo()
        }catch (e:Exception){}
        Log.e("TAG", "onStop: ")
    }*/
}

