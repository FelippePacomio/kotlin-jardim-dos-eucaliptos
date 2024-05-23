package com.example.associacao_jardim_eucaliptos

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

private const val DONATIONS_ARG_PARAM1 = "donations_param1"
private const val DONATIONS_ARG_PARAM2 = "donations_param2"

class DonationsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(DONATIONS_ARG_PARAM1)
            param2 = it.getString(DONATIONS_ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_donations, container, false)

        val chavePixText: TextView = view.findViewById(R.id.tv_chave_pix)
        val pixKeyText: TextView = view.findViewById(R.id.tv_pix_key)
        val pixImageView: ImageView = view.findViewById(R.id.iv_pix_image)
        val cpfCnpjText: TextView = view.findViewById(R.id.tv_cpf_cnpj)

        val copyText = "57.281.735/0001-58"

        // Function to copy text to clipboard
        fun copyToClipboard(text: String) {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PIX Key", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(getContext(), "Chave PIX Copiada!", Toast.LENGTH_SHORT).show()
        }

        // Set click listeners to copy the text to clipboard
        val clickListener = View.OnClickListener {
            copyToClipboard(copyText)
        }

        chavePixText.setOnClickListener(clickListener)
        pixKeyText.setOnClickListener(clickListener)
        pixImageView.setOnClickListener(clickListener)
        cpfCnpjText.setOnClickListener(clickListener)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DonationsFragment().apply {
                arguments = Bundle().apply {
                    putString(DONATIONS_ARG_PARAM1, param1)
                    putString(DONATIONS_ARG_PARAM2, param2)
                }
            }
    }
}
