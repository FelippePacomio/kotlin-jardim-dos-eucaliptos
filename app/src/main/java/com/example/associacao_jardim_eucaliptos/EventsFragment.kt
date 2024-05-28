package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class EventsFragment : Fragment() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter
    private val eventsList = mutableListOf<EventsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("events")
        val rootView = inflater.inflate(R.layout.fragment_events, container, false)

        eventsRecyclerView = rootView.findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        eventsRecyclerView.setHasFixedSize(true)

        eventsAdapter = EventsAdapter(eventsList) { event ->
            openEventDetailFragment(event)
        }
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
                    val eventDescription = eventSnapshot.child("dataDesc").getValue(String::class.java)
                    val eventDate = eventSnapshot.child("dataLang").getValue(String::class.java)

                    if (eventTitle != null && eventImage != null && eventDescription != null && eventDate != null) {
                        val formattedDate = formatDate(eventDate)
                        eventsList.add(EventsItem(eventImage, eventTitle, eventDescription, formattedDate))
                    }
                }
                eventsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    private fun formatDate(dateString: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            val date = sdf.parse(dateString)

            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
            outputFormat.timeZone = TimeZone.getTimeZone("GMT-3")

            // Translate month names from English to Portuguese
            val formattedDate = outputFormat.format(date!!)
            translateMonthNames(formattedDate)
        } catch (e: Exception) {
            dateString // return original string if parsing fails
        }
    }

    private fun translateMonthNames(dateString: String): String {
        val englishMonthNames = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).dateFormatSymbols.months
        val portugueseMonthNames = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).dateFormatSymbols.months

        var translatedDateString = dateString
        for (i in englishMonthNames.indices) {
            translatedDateString = translatedDateString.replace(englishMonthNames[i], portugueseMonthNames[i], true)
        }

        return translatedDateString
    }


    private fun openEventDetailFragment(event: EventsItem) {
        val fragment = EventDetailFragment.newInstance(event.eventsTitle, event.eventsImage, event.eventDescription, event.eventDate)
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
    }
}
