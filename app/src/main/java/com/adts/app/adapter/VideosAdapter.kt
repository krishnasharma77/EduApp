package com.adts.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import  com.adts.app.R
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adts.app.activity.PlayVideoActivity
import com.bumptech.glide.Glide
import com.adts.app.model.DataXX
import com.google.android.exoplayer2.*


class VideosAdapter(
    var context: Context,
    var videoslist: MutableList<DataXX>,

    ) :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    //    val cacheDataSourceFactory = DefaultDataSourceFactory(
//        context
//    )
    lateinit var exoPlayer: SimpleExoPlayer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.video_name.text = videoslist[position].ResourseName

        if (videoslist[position].ResourseImage != null && videoslist[position].ResourseImage != "null") {
            Glide.with(context)
                .load(videoslist[position].ResourseImage)
                .into(holder.thumbnail)
        }


        holder.thumbnail.setOnClickListener {
            if (videoslist[position].ResourseVideo != null && videoslist[position].ResourseVideo != "null") {
                context.startActivity(
                    Intent(
                        context,
                        PlayVideoActivity::class.java
                    ).putExtra("showVideo", videoslist[position].ResourseVideo)
                )

            } else {
                Toast.makeText(context,"No Video Found",Toast.LENGTH_SHORT).show()
            }

        }

        /* bmThumbnail = ThumbnailUtils.createVideoThumbnail(videouri.toString(), MediaStore.Video.Thumbnails.MICRO_KIND)!!
         holder.thumbnail.setImageBitmap(bmThumbnail)


         val retriever = MediaMetadataRetriever()
         retriever.setDataSource(videouri)
         val bitmap = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
         setImageBitmap(bitmap)*/
    }

    override fun getItemCount(): Int {
        return videoslist.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var video_name: TextView = itemView.findViewById(R.id.videos_txt)
        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)

    }

    interface ClickListener {
        fun clicked(view: View, posi: DataXX)
    }


}
