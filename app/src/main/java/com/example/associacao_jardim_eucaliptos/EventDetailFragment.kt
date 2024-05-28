package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class EventDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_event_detail, container, false)

        val eventTitle = arguments?.getString("eventTitle")
        val eventImage = arguments?.getString("eventImage")
        val eventDescription = arguments?.getString("eventDescription")
        val eventDate = arguments?.getString("eventDate")

        val titleTextView: TextView = rootView.findViewById(R.id.eventDetailTitle)
        val imageView: ImageView = rootView.findViewById(R.id.eventDetailImage)
        val descriptionTextView: TextView = rootView.findViewById(R.id.detailDesc)
        val dateTextView: TextView = rootView.findViewById(R.id.eventDetailDate)

        titleTextView.text = eventTitle
        Glide.with(this).load(eventImage).into(imageView)
        descriptionTextView.text = eventDescription
        dateTextView.text = eventDate

        return rootView
    }

    companion object {
        fun newInstance(eventTitle: String, eventImage: String, eventDescription: String, eventDate: String): EventDetailFragment {
            val fragment = EventDetailFragment()
            val args = Bundle()
            args.putString("eventTitle", eventTitle)
            args.putString("eventImage", eventImage)
            args.putString("eventDescription", eventDescription)
            args.putString("eventDate", eventDate)
            fragment.arguments = args
            return fragment
        }
    }
}
