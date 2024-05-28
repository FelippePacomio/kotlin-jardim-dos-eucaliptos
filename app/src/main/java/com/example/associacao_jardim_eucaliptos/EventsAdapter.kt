package com.example.associacao_jardim_eucaliptos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventsAdapter(
    private val eventsList: List<EventsItem>,
    private val onItemClickListener: (EventsItem) -> Unit
) : RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_events, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventsList[position]
        holder.eventsTitle.text = currentItem.eventsTitle
        Glide.with(holder.itemView.context).load(currentItem.eventsImage).into(holder.eventsImage)

        holder.itemView.setOnClickListener {
            onItemClickListener(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventsTitle: TextView = itemView.findViewById(R.id.eventsTitle)
        val eventsImage: ImageView = itemView.findViewById(R.id.eventsImage)
        val cardView: CardView = itemView.findViewById(R.id.eventsCardView)
    }
}
