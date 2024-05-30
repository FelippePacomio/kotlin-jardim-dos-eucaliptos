package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log

class HomeFragment : Fragment() {
    private lateinit var eventsFields: Array<Array<View>>
    private lateinit var newsFields: Array<Array<View>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        initializeViews(rootView)
        fetchEventsFromFirebase(rootView)
        fetchNewsFromFirebase(rootView)

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("events")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageList = ArrayList<SlideModel>()
                for (eventSnapshot in dataSnapshot.children) {
                    val eventTitle = eventSnapshot.child("dataTitle").getValue(String::class.java)
                    val eventImage = eventSnapshot.child("dataImage").getValue(String::class.java)
                    if (eventTitle != null && eventImage != null) {
                        imageList.add(SlideModel(eventImage, eventTitle))
                    }
                }

                val imageSlider = rootView.findViewById<ImageSlider>(R.id.image_slider)
                imageSlider.setImageList(imageList)
                imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return rootView
    }

    private fun initializeViews(rootView: View) {
        eventsFields = arrayOf(
            arrayOf(rootView.findViewById(R.id.eventsImage1), rootView.findViewById(R.id.eventsTitle1)),
            arrayOf(rootView.findViewById(R.id.eventsImage2), rootView.findViewById(R.id.eventsTitle2)),
            arrayOf(rootView.findViewById(R.id.eventsImage3), rootView.findViewById(R.id.eventsTitle3)),
            arrayOf(rootView.findViewById(R.id.eventsImage4), rootView.findViewById(R.id.eventsTitle4))
        )

        newsFields = arrayOf(
            arrayOf(rootView.findViewById(R.id.newsThumbnail1), rootView.findViewById(R.id.newsTitle1)),
            arrayOf(rootView.findViewById(R.id.newsThumbnail2), rootView.findViewById(R.id.newsTitle2)),
            arrayOf(rootView.findViewById(R.id.newsThumbnail3), rootView.findViewById(R.id.newsTitle3)),
            arrayOf(rootView.findViewById(R.id.newsThumbnail4), rootView.findViewById(R.id.newsTitle4))
        )
    }

    private fun fetchEventsFromFirebase(rootView: View) {
        val eventReference = FirebaseDatabase.getInstance().getReference("events")
        eventReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var index = 0
                for (eventSnapshot in dataSnapshot.children) {
                    if (index >= eventsFields.size) break
                    val eventTitle = eventSnapshot.child("dataTitle").getValue(String::class.java)
                    val eventImage = eventSnapshot.child("dataImage").getValue(String::class.java)
                    val eventDescription = eventSnapshot.child("dataDesc").getValue(String::class.java)
                    val eventDate = eventSnapshot.child("dataLang").getValue(String::class.java)
                    if (eventTitle != null && eventImage != null && eventDescription != null && eventDate != null) {
                        setEventDataToCardView(index, eventImage, eventTitle, eventDescription, eventDate)
                        index++
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun fetchNewsFromFirebase(rootView: View) {
        val newsReference = FirebaseDatabase.getInstance().getReference("news")
        newsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var index = 0
                for (newsSnapshot in dataSnapshot.children) {
                    if (index >= newsFields.size) break
                    val newsTitle = newsSnapshot.child("dataTitle").getValue(String::class.java)
                    val newsImage = newsSnapshot.child("dataImage").getValue(String::class.java)
                    val newsDescription = newsSnapshot.child("dataDesc").getValue(String::class.java)
                    val newsDate = newsSnapshot.child("dataLang").getValue(String::class.java)
                    if (newsTitle != null && newsImage != null && newsDescription != null && newsDate != null) {
                        setNewsDataToField(index, newsImage, newsTitle, newsDescription, newsDate)
                        index++
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun setEventDataToCardView(index: Int, imageUrl: String, title: String, description: String, date: String) {
        val eventImageView = eventsFields[index][0] as ImageView
        val eventTitleView = eventsFields[index][1] as TextView

        Glide.with(requireContext())
            .load(imageUrl)
            .into(eventImageView)
        eventTitleView.text = title

        val eventData = EventDetailFragment.newInstance(title, imageUrl, description, date)
        val bundle = Bundle().apply {
            putString("eventTitle", title)
            putString("eventImage", imageUrl)
            putString("eventDescription", description)
            putString("eventDate", date)
        }

        eventsFields[index][0].setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, eventData)
                .addToBackStack(null)
                .commit()
        }

        eventsFields[index][1].setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, eventData)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setNewsDataToField(index: Int, imageUrl: String, title: String, description: String, date: String) {
        val newsThumbnailView = newsFields[index][0] as ImageView
        val newsTitleView = newsFields[index][1] as TextView

        Glide.with(requireContext())
            .load(imageUrl)
            .into(newsThumbnailView)
        newsTitleView.text = title

        val newsData = NewsDetailFragment.newInstance(title, imageUrl, description, date)
        val bundle = Bundle().apply {
            putString("newsTitle", title)
            putString("newsThumbnail", imageUrl)
            putString("newsDescription", description)
            putString("newsDate", date)
        }

        newsFields[index][0].setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newsData)
                .addToBackStack(null)
                .commit()
        }

        newsFields[index][1].setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newsData)
                .addToBackStack(null)
                .commit()
        }
    }
}
