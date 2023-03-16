package com.example.myapplication.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.ShortenUrl
import com.google.android.material.snackbar.Snackbar

class HistoryRVAdapter(private val context: Context) : RecyclerView.Adapter<HistoryRVAdapter.HistoryViewModel>() {

    inner class HistoryViewModel(view: View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.shared_link_title)
        val shortedLink : TextView = view.findViewById(R.id.shorten_link)
        val copyButton : ImageView = view.findViewById(R.id.copy_short_link)
    }

    // Using diff utils to all changes in the list
    private val differCallBack = object : DiffUtil.ItemCallback<ShortenUrl>(){
        override fun areItemsTheSame(oldItem: ShortenUrl, newItem: ShortenUrl): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ShortenUrl, newItem: ShortenUrl): Boolean {
            return oldItem == newItem
        }
    }

    val myDifferList = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewModel {
        return HistoryViewModel(LayoutInflater.from(context).inflate(R.layout.search_history_rv, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewModel, position: Int) {
        val current = myDifferList.currentList[position]
        holder.apply {
            title.text = current.title
            shortedLink.text = current.shortLink
            copyButton.setOnClickListener { copyOnClick(current.shortLink, itemView) }
        }

    }

    private fun copyOnClick(shortLink: String, itemView: View) {
        val clipBoard : ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClipData = ClipData.newPlainText("Short Url", shortLink)
        clipBoard.setPrimaryClip(myClipData)

        Snackbar.make(itemView,"Link Copied",Snackbar.LENGTH_LONG).show()
    }

    override fun getItemCount(): Int {
        return myDifferList.currentList.size
    }
}