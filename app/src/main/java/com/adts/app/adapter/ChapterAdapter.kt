package com.adts.app.adapter

import android.content.Context
import android.text.Html
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adts.app.R
import com.adts.app.model.CChapter


class ChapterAdapter(
    var context: Context,
    var chapterList: List<CChapter>,
    private var clickListener: ClickListener
) :
    RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chapter_data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mydownloand: Long = 0

        holder.courses.text = chapterList[position].ChapterName

    try {
        if (chapterList[position].ChapterDescription.isNotEmpty()) {
            val htmlString = chapterList[position].ChapterDescription.replace("\\<.*?\\>", "")
            holder.tv_desc.visibility = View.VISIBLE
            holder.tv_desc.text = Html.fromHtml(htmlString)
        }

    }
    catch (e: Exception) {

    }




        holder.expendview.setOnClickListener {

            if (holder.hiddenView.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    holder.cardView,
                    AutoTransition()
                )
                holder.hiddenView.visibility = View.GONE
                holder.expendview.setImageResource(R.drawable.ic_baseline_expand_more_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    holder.cardView,
                    AutoTransition()
                )
                holder.hiddenView.visibility = View.VISIBLE
                holder.expendview.setImageResource(R.drawable.ic_baseline_expand_less_24)
            }
        }

        if (chapterList[position].ChapterVideo == null || chapterList[position].ChapterVideo == "null"){
            holder.videoView.visibility = View.GONE
        }

        if (chapterList[position].ChapterPdf == null || chapterList[position].ChapterPdf == "null"){
            holder.pdfView.visibility = View.GONE
        }

        holder.videoView.setOnClickListener {
            clickListener.clicked(it, chapterList[position], position, "Video")
        }

        holder.pdfView.setOnClickListener {
            clickListener.clicked(it, chapterList[position], position, "PDF")

        }
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courses: TextView = itemView.findViewById(R.id.heading)
        var expendview: ImageButton = itemView.findViewById(R.id.arrow_button)
        var hiddenView: LinearLayout = itemView.findViewById(R.id.hidden_view)
        var cardView: CardView = itemView.findViewById(R.id.base_cardview)
        var tv_desc: TextView = itemView.findViewById(R.id.chapter_desc)
        var pdfView: Button = itemView.findViewById(R.id.pdf_btn)
        var videoView: Button = itemView.findViewById(R.id.Video_btn)
    }

    interface ClickListener {
        fun clicked(view: View, posi: CChapter, position: Int, type: String)
    }

}