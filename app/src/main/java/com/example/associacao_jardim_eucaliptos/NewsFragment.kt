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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)
        newsRecyclerView = rootView.findViewById(R.id.newsRecyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsRecyclerView.setHasFixedSize(true)
        newsAdapter = NewsAdapter(newsList)
        newsRecyclerView.adapter = newsAdapter

        newsAdapter.setOnItemClickListener(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(item: NewsItem) {
                Toast.makeText(requireContext(), "News clicked: ${item.newsTitle}", Toast.LENGTH_SHORT).show()
            }
        })

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
                    val newsImage = newsSnapshot.child("dataImage").getValue(String::class.java)

                    if (newsTitle != null && newsImage != null) {
                        val newsItem = NewsItem(newsImage, newsTitle)
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
}
