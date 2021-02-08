package com.example.fetchapp.fetch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchapp.R

class ItemAdapter(private val itemList: ArrayList<Item>) : StickyHeaderAdapter,
    ListAdapter<Item, ItemAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.id == newItem.id
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemList.get(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Item) {

            val id = itemView.findViewById<TextView>(R.id.item_id)
            val listId = itemView.findViewById<TextView>(R.id.list_id)
            val name = itemView.findViewById<TextView>(R.id.name)

            id.text = item.id.toString()
            listId.text = item.listId.toString()
            name.text = item.name
        }
    }

    override fun getHeaderId(position: Int): Long = getItem(position).listId.toLong()

    override fun onCreateHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HeaderViewHolder).bind(getItem(position).listId)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val header = itemView.findViewById<TextView>(R.id.header)

        fun bind(listId: Int) {
            val res = itemView.context.resources
            header.text = when (listId) {
                1 -> res.getString(R.string.listId_1)
                2 -> res.getString(R.string.listId_2)
                3 -> res.getString(R.string.listId_3)
                4 -> res.getString(R.string.listId_4)
                else -> ""
            }
        }
    }
}