package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class NewsDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_news_detail, container, false)

        val newsTitle = arguments?.getString("newsTitle")
        val newsThumbnail = arguments?.getString("newsThumbnail")
        val newsDescription = arguments?.getString("newsDescription")
        val newsDate = arguments?.getString("newsDate")

        val titleTextView: TextView = rootView.findViewById(R.id.newsDetailTitle)
        val imageView: ImageView = rootView.findViewById(R.id.newsDetailImage)
        val descriptionTextView: TextView = rootView.findViewById(R.id.detailDesc)
        val dateTextView: TextView = rootView.findViewById(R.id.newsDetailDate)

        titleTextView.text = newsTitle
        Glide.with(this).load(newsThumbnail).into(imageView)
        descriptionTextView.text = newsDescription
        dateTextView.text = newsDate

        return rootView
    }

    companion object {
        fun newInstance(newsTitle: String, newsThumbnail: String, newsDescription: String, newsDate: String): NewsDetailFragment {
            val fragment = NewsDetailFragment()
            val args = Bundle()
            args.putString("newsTitle", newsTitle)
            args.putString("newsThumbnail", newsThumbnail)
            args.putString("newsDescription", newsDescription)
            args.putString("newsDate", newsDate)
            fragment.arguments = args
            return fragment
        }
    }
}
