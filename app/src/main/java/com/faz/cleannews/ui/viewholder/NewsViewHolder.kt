package com.faz.cleannews.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faz.cleannews.R
import com.faz.cleannews.data.model.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_feed.view.*

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(news: News?) {
        if (news != null) {
            itemView.txt_news_name.text = news.title
            if (!news.image.isNullOrEmpty())
                Picasso.get().load(news.image).into(itemView.img_news_banner)
        }
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feed, parent, false)
            return NewsViewHolder(view)
        }
    }
}
