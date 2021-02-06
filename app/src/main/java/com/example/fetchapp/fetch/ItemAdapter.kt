package com.example.fetchapp.fetch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchapp.R

class ItemAdapter(private val repoList: ArrayList<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(repoList.get(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Item) {

            val id = itemView.findViewById<TextView>(R.id.id)
            val listId = itemView.findViewById<TextView>(R.id.list_id)
            val name = itemView.findViewById<TextView>(R.id.name)

            id.text = item.id.toString()
            listId.text = item.listId.toString()
            name.text = item.name
        }
    }
}