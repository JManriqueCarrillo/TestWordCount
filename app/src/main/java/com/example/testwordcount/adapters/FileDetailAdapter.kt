package com.example.testwordcount.adapters

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testwordcount.R
import com.example.testwordcount.adapters.infiniteScroll.VIEW_TYPE

class FileDetailAdapter(
    private val context: Context,
    private val data: MutableList<String?>,
    private val listener: (Any) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE.VIEW_TYPE_ITEM) {
            ViewItemEntry(
                LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
            )
        } else {
            LoadingViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_progress_loading, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE.VIEW_TYPE_ITEM) {

            (holder as ViewItemEntry).bind(data[position]!!)
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EFEFEF"))
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            holder.itemView.setOnClickListener { _ ->
                listener(data[position]!!)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] == null) {
            VIEW_TYPE.VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE.VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<String>) {
        this.data.clear()
        this.data.addAll(newData)
    }

    fun addData(dataViews: List<String?>) {
        this.data.addAll(dataViews)
    }

    fun addLoadingView() {
        data.add(null)
        notifyItemInserted(data.size - 1)
    }

    fun removeLoadingView() {
        if (data.size != 0) {
            data.removeAt(data.size - 1)
            notifyItemRemoved(data.size)
        }
    }

    class ViewItemEntry(binding: View) : RecyclerView.ViewHolder(binding) {
        private var itemTv = binding.findViewById<TextView>(R.id.item_tv)

        fun bind(item: String) {
            itemTv.text = item
        }
    }

    class LoadingViewHolder(binding: View) : RecyclerView.ViewHolder(binding)
}