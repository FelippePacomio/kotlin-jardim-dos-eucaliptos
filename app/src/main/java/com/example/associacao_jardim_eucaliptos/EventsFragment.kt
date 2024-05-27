package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventsFragment : Fragment() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter
    private val eventsList = mutableListOf<EventsItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("events")
        val rootView = inflater.inflate(R.layout.fragment_events, container, false)

        eventsRecyclerView = rootView.findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        eventsRecyclerView.setHasFixedSize(true)

        eventsAdapter = EventsAdapter(eventsList)
        eventsRecyclerView.adapter = eventsAdapter

        fetchEventsFromDatabase(reference)

        return rootView
    }

    private fun fetchEventsFromDatabase(reference: DatabaseReference) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                eventsList.clear()
                for (eventSnapshot in dataSnapshot.children) {
                    val eventTitle = eventSnapshot.child("dataTitle").getValue(String::class.java)
                    val eventImage = eventSnapshot.child("dataImage").getValue(String::class.java)

                    if (eventTitle != null && eventImage != null) {
                        eventsList.add(EventsItem(eventImage, eventTitle))
                    }
                }
                eventsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
