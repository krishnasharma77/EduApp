package com.adts.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.adts.app.R
import com.adts.app.model.DataXX

class AllResourcesAdapter (var context: Context,
                         var resoursesList:MutableList<DataXX>,
                         private var clickListener: ClickListener,
) : RecyclerView.Adapter<AllResourcesAdapter.ViewHolder>(), Filterable {
    private val mFilter: ItemFilter = ItemFilter()
    var orignalSearchList = resoursesList
    var filterSearchList = resoursesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.resource_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courses.text = filterSearchList[position].ResourseName

        if (filterSearchList[position].ResourseImage.isNotEmpty()) {
            val img = filterSearchList[position].ResourseImage
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
                .replace("\\", "")
            Glide.with(context).load(img).into(holder.courseImg)
        }

        holder.itemView.setOnClickListener {
            clickListener.clicked(it, filterSearchList[position])
        }
    }

    override fun getItemCount(): Int {
        return filterSearchList.size
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString().lowercase()
            val filterResults = FilterResults()
            val list = orignalSearchList
            var filterableString: String
            val nlist = java.util.ArrayList<DataXX>()

            for (i in list.indices) {
                filterableString = list[i].ResourseName
                if (filterableString.lowercase().contains(filterString)) {
                    nlist.add(orignalSearchList[i])
                }
            }
            filterResults.values = nlist
            filterResults.count = nlist.size
            return filterResults

        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filterSearchList = results?.values as MutableList<DataXX>
            notifyDataSetChanged()
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courses: TextView = itemView.findViewById(R.id.course_name)
        var courseImg: ImageView = itemView.findViewById(R.id.img)
    }

    interface ClickListener {
        fun clicked(view: View, posi: DataXX)
    }

    override fun getFilter(): Filter {
        return mFilter
    }
}