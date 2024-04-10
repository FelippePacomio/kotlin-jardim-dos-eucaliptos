package com.example.pi_5_semestre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class EventsAdapter constructor(
    private val eventsList: List<EventsItem>
) :
    RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_events, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventsAdapter.MyViewHolder, position: Int) {
        holder.eventsTitle.text = eventsList[position].eventsTitle
        holder.eventsImage.setImageResource(eventsList[position].eventsImage)
      /*  holder.cardView.setOnClickListener {
            Toast.makeText(this, eventsList[position].eventsTitle, Toast.LENGTH_SHORT).show()
        }*/
            val currentItem = eventsList[position]
            holder.itemView.setOnClickListener {
                listener?.onItemClick(currentItem)
            }
    }

    interface OnItemClickListener {
        fun onItemClick(item: EventsItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
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