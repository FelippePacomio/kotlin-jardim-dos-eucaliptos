package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.ferias, "Confira nossa programação de férias!"))
        imageList.add(SlideModel(R.drawable.facaparte, "Tem vontade de fazer parte da equipe? Junte-se a nós!"))
        imageList.add(SlideModel(R.drawable.psocial, "Confira nossos próximos projetos sociais na comunidade do Jardim dos Eucaliptos"))
        imageList.add(SlideModel(R.drawable.cestabasica, "Entrega de Cestas Básicas Maio 2024"))
        imageList.add(SlideModel(R.drawable.newsimage3, "A equipe está maior! Conheça nossos novos patrocínios!"))
        //imageList.add(SlideModel("https://bit.ly/3fLJf72", "And people do that."))

        val imageSlider = rootView.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP) // for all images

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
