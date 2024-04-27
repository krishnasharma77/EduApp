package com.adts.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.adts.app.R
import com.adts.app.model.DataX
import com.adts.app.model.MainScreenData
import com.adts.app.model.Media
import com.bumptech.glide.Glide
import okhttp3.MediaType

class AllCoursesAdapter(
    var context: Context,
    var coursesList: MutableList<MainScreenData>,
    private var clickListener: ClickListener,
) :RecyclerView.Adapter<AllCoursesAdapter.ViewHolder>(),Filterable {
    private val mFilter : ItemFilter = ItemFilter()
    var orignalSearchList = coursesList
    var filterSearchList = coursesList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     val view=LayoutInflater.from(context).inflate(R.layout.courses_name_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = filterSearchList[position]

        holder.courses.text=course.courseName
        holder.courseImg.visibility = View.GONE
        holder.videoView.visibility = View.GONE

        // Handle media type
        when (course.media) {
            is Media.Image -> {
                holder.courseImg.visibility = View.VISIBLE
                Glide.with(context).load(course.media.url).into(holder.courseImg)
            }
            is Media.Video -> {
                holder.videoView.visibility = View.VISIBLE
                holder.videoView.setVideoURI(course.media.url.toUri())
                // Start video or set up further controls here
            }
            else -> {
                // If there's no media, no action is needed, as both views are hidden by default
            }
        }

        try {
            if (filterSearchList[position].media!=null){
                Glide.with(context).load(filterSearchList[position].media).into(holder.courseImg)

            }
      }catch (e:Exception){

      }

        holder.itemView.setOnClickListener {
            clickListener.clicked(it,filterSearchList[position])
        }

    }

    override fun getItemCount(): Int {
        return filterSearchList.size
    }


    inner class ItemFilter : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString().lowercase()
            val filterResults = FilterResults()
            val list = orignalSearchList
            var filterableString : String
            val nlist = java.util.ArrayList<MainScreenData>()

            for (i in list.indices)
            {
                filterableString = list[i].courseName
                if (filterableString.lowercase().contains(filterString)){
                    nlist.add(orignalSearchList[i])
                }
            }
            filterResults.values = nlist
            filterResults.count = nlist.size
            return filterResults

        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filterSearchList = results?.values as MutableList<MainScreenData>
            notifyDataSetChanged()
        }


    }

    class ViewHolder (itemView:View):RecyclerView.ViewHolder(itemView) {
        var courses : TextView= itemView.findViewById(R.id.course_name)
        var courseImg : ImageView = itemView.findViewById(R.id.img)
        var videoView : VideoView = itemView.findViewById(R.id.video)
    }

    interface ClickListener {
        fun clicked(view: View, posi: MainScreenData)
    }
    override fun getFilter(): Filter {
        return mFilter
    }
}