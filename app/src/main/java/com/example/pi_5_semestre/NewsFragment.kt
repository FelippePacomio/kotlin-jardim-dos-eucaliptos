package com.example.pi_5_semestre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsFragment : Fragment() {

    private var newsList: List<NewsItem> = listOf()
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)
        newsList = generateNewsItems()
        newsRecyclerView = rootView.findViewById(R.id.newsRecyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(newsList)
        newsRecyclerView.adapter = newsAdapter
        return rootView
    }

    private fun generateNewsItems(): List<NewsItem> {
        val newsItems = mutableListOf<NewsItem>()
        newsItems.add(NewsItem(R.drawable.newsimage1, "Confira nossa agenda para a Páscoa 2024!"))
        newsItems.add(NewsItem(R.drawable.newsimage2, "Aprenda a fazer um pão de queijo com gostinho de tapioca!"))
        newsItems.add(NewsItem(R.drawable.newsimage3, "A equipe está maior! Conheça nossos novos patrocínios!"))
        newsItems.add(NewsItem(R.drawable.newsimage4, "Ainda não ouviu falar sobre nossa associação? Saiba um pouquinho sobre nossa história!"))
        return newsItems
    }
}
