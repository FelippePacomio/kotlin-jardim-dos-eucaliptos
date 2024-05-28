package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class NewsFragment : Fragment() {

    private var newsList: MutableList<NewsItem> = mutableListOf()
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)
        newsRecyclerView = rootView.findViewById(R.id.newsRecyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsRecyclerView.setHasFixedSize(true)
        newsAdapter = NewsAdapter(newsList) { news ->
            openNewsDetailFragment(news)
        }
        newsRecyclerView.adapter = newsAdapter

        databaseReference = FirebaseDatabase.getInstance().getReference("news")
        fetchNewsFromDatabase(databaseReference)

        return rootView
    }

    private fun fetchNewsFromDatabase(reference: DatabaseReference) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                newsList.clear()
                for (newsSnapshot in dataSnapshot.children) {
                    val newsTitle = newsSnapshot.child("dataTitle").getValue(String::class.java)
                    val newsThumbnail = newsSnapshot.child("dataImage").getValue(String::class.java)
                    val newsDescription = newsSnapshot.child("dataDesc").getValue(String::class.java)
                    val newsDate = newsSnapshot.child("dataLang").getValue(String::class.java)

                    if (newsTitle != null && newsThumbnail != null && newsDescription != null && newsDate != null) {
                        val newsItem = NewsItem(newsThumbnail, newsTitle, newsDescription, newsDate)
                        newsList.add(newsItem)
                    }
                }
                newsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load news", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openNewsDetailFragment(news: NewsItem) {
        val fragment = NewsDetailFragment.newInstance(news.newsTitle, news.newsThumbnail, news.newsDescription, news.newsDate)
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
    }
}
