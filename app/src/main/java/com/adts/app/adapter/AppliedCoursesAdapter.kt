package com.adts.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adts.app.R
import com.adts.app.model.DataXXXX

class AppliedCoursesAdapter(var context: Context,
                            var appliedCourseList:MutableList<DataXXXX>,
                            private var clickListener: ClickListener
):
    RecyclerView.Adapter<AppliedCoursesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.courses_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courses.text = appliedCourseList[position].CourseName
        try {
            if (appliedCourseList[position].CourseImage.isNotEmpty()) {
                val img = appliedCourseList[position].CourseImage
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .replace("\\", "")
                Glide.with(context).load(img).into(holder.courseImg)
            }
        }catch (e:Exception){

        }
        holder.itemView.setOnClickListener {
            clickListener.clicked(it,appliedCourseList[position])

        }
    }

    override fun getItemCount(): Int {
        return appliedCourseList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courses: TextView = itemView.findViewById(R.id.course_name)
        var courseImg: ImageView = itemView.findViewById(R.id.img)
    }

    interface ClickListener {
        fun clicked(view: View, posi: DataXXXX)
    }
}