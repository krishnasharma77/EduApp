package com.adts.app.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.adts.app.databinding.ActivityPlayYoutubeVideosBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class PlayYoutubeVideos : AppCompatActivity() {
    lateinit var youtubePlayerView: YouTubePlayerView
    lateinit var url:String
//    var url: String = "NYWQgEdZzAI"
    private lateinit var binding:ActivityPlayYoutubeVideosBinding
    private lateinit var videouri:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayYoutubeVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        videouri = intent.getStringExtra("showVideo").toString()
         url = videouri.replace("https://www.youtube.com/watch?v=","")
        initUI()
        onClick()

    }

    private fun initUI() {
        try {
            binding.youtubePlayerView.enterFullScreen()
            binding.youtubePlayerView.getPlayerUiController().showMenuButton(false)
            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(url, 0.0f)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }

    private fun onClick() {
        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }
    }
}
