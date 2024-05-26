package com.example.associacao_jardim_eucaliptos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class ManageNewsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideToolbarAndBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showToolbarAndBottomNavigation()
    }
}