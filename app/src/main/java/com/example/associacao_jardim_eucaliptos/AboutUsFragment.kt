package com.example.associacao_jardim_eucaliptos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aboutus, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideToolbarAndBottomNavigation()
        val backArrow: ImageView = view.findViewById(R.id.back_arrow)
        backArrow.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val whatsappIcon: ImageView = view.findViewById(R.id.whatsapp_icon)
        whatsappIcon.setOnClickListener {
            val whatsappIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/+5511979503524")
            }
            startActivity(whatsappIntent)
        }

        val instagramIcon: ImageView = view.findViewById(R.id.instagram_icon)
        instagramIcon.setOnClickListener {
            val instagramIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.instagram.com/associacaojdeucaliptos?igsh=MWU5bGc0dHBicnpmeg==")
            }
            startActivity(instagramIntent)
        }
    }
}
