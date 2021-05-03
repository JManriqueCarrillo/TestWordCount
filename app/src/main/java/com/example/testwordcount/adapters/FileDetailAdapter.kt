package com.example.testwordcount.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testwordcount.R

class FileDetailAdapter(
    private val context: Context,
    private val data: MutableList<String>,
    private val listener: (Any) -> Unit
) : RecyclerView.Adapter<FileDetailAdapter.ViewItemEntry>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewItemEntry {
        return ViewItemEntry(LayoutInflater.from(context).inflate(R.layout.row_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewItemEntry, position: Int) {
        holder.bind(data[position])

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EFEFEF"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.itemView.setOnClickListener {
            listener(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<String>){
        this.data.clear()
        this.data.addAll(newData)
    }

    inner class ViewItemEntry(binding: View) : RecyclerView.ViewHolder(binding) {
        private var itemTv = binding.findViewById<TextView>(R.id.item_tv)

        fun bind(item: String) {
            itemTv.text = item
        }
    }
}