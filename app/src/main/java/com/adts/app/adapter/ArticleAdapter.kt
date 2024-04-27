package com.adts.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.adts.app.R
import com.adts.app.model.DataXXX

class ArticleAdapter(
    var context: Context, var articleList: MutableList<DataXXX>,
    private var clickListener: ClickListener
):
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.resource_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courses.text = articleList[position].ArticleName

        if (articleList[position].ArticleImage.isNotEmpty()) {
            val img = articleList[position].ArticleImage
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
                .replace("\\", "")
            Glide.with(context).load(img).into(holder.courseImg)
        }

        holder.itemView.setOnClickListener {
            clickListener.clicked(it,articleList[position])
        }

    }

    override fun getItemCount(): Int {
        return articleList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var courses: TextView = itemView.findViewById(R.id.course_name)
        var courseImg: ImageView = itemView.findViewById(R.id.img)
    }
    interface ClickListener {
        fun clicked(view: View, posi: DataXXX)
    }
}